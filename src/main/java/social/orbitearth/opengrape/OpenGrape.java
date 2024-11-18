package social.orbitearth.opengrape;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import social.orbitearth.opengrape.parser.DefaultOpenGrapeParser;
import social.orbitearth.opengrape.parser.OpenGrapeParser;

public class OpenGrape
{
    private final static Logger logger = LoggerFactory.getLogger(OpenGrape.class);
    private final Map<OpenGrapeMetadata, String> source;

    static final String DEFAULT_USER_AGENT = "WhatsApp/2.2336.9 N";

    public OpenGrape(String htmlString, OpenGrapeParser parser)
    {
        this.source = parser.parse(htmlString);
    }

    private OpenGrape()
    {
        source = new HashMap<>();
    }

    public static OpenGrape fetch(String url) throws IOException, OpenGrapeResponseException
    {
        return fetch(url, DEFAULT_USER_AGENT);
    }

    public static OpenGrape fetch(String url, String userAgent) throws IOException, OpenGrapeResponseException
    {
        if (url.endsWith("/"))
        {
            url = url.substring(0, url.length() - 1);
        }
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        // some URLs need User-Agent header or else HTML source won't contain any OG tags
        connection.setRequestProperty("User-Agent", userAgent);
        int statusCode = connection.getResponseCode();
        if (statusCode < 200 || statusCode > 300)
        {
            logger.warn("Could not load {} for parsing. HTTP status code {}", url, statusCode);
            throw new OpenGrapeResponseException.UnexpectedStatusCode(statusCode);
        }
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        StringBuilder htmlString = new StringBuilder();
        while ((line = reader.readLine()) != null)
        {
            htmlString.append(line).append("\n");
        }
        return new OpenGrape(htmlString.toString(), new DefaultOpenGrapeParser());
    }

    public static OpenGrape fetch(String url, int timeoutSeconds) throws IOException, OpenGrapeResponseException
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<OpenGrape> future = executor.submit(() -> OpenGrape.fetch(url));
        try
        {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        }
        catch (TimeoutException e)
        {
            future.cancel(true);
        }
        catch (ExecutionException e)
        {
            logger.warn("Unable to load {} for parsing.", url, e);
            if (e.getCause() != null)
            {
                if (e.getCause() instanceof IOException)
                {
                    throw (IOException) e.getCause();
                }
                else if (e.getCause() instanceof OpenGrapeResponseException)
                {
                    throw (OpenGrapeResponseException) e.getCause();
                }
            }
        }
        catch (Exception e)
        {
            logger.warn("Unable to load {} for parsing.", url, e);
        }
        finally
        {
            executor.shutdownNow();
        }
        return new OpenGrape();
    }

    public String getValue(OpenGrapeMetadata metadata)
    {
        return source.get(metadata);
    }

    /**
     * Loads the OG metadata of a url.
     * @param args url to load
     */
    public static void main(String[] args) throws OpenGrapeResponseException, IOException
    {
        OpenGrape fetched = OpenGrape.fetch(args[0], "WhatsApp/2.2336.9 N");
        System.out.println(fetched.source);
    }
}
