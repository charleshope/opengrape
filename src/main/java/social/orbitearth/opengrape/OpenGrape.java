package social.orbitearth.opengrape;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import social.orbitearth.opengrape.parser.DefaultOpenGrapeParser;
import social.orbitearth.opengrape.parser.OpenGrapeParser;

public class OpenGrape
{
    private final static Logger logger = LoggerFactory.getLogger(OpenGrape.class);

    private final OpenGrapeParser parser;
    private final UserAgentFactory user_agent_factory;

    OpenGrape(UserAgentFactory user_agent_factory)
    {
        parser = new DefaultOpenGrapeParser();
        this.user_agent_factory = user_agent_factory;
    }

    public OpenGrape()
    {
        this(new UserAgentFactory());
    }

    /**
     * @return Map of the Open Graph metadata, keyed by the {@link OpenGrapeMetadata} enums.
     * @throws OpenGrapeResponseException when the host returned an HTTP status not 2xx.
     */
    public Map<OpenGrapeMetadata, String> fetch(URI uri) throws IOException, OpenGrapeResponseException
    {
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        String userAgent = user_agent_factory.getUserAgent(uri);
        connection.setRequestProperty("User-Agent", userAgent);
        int statusCode = connection.getResponseCode();
        if (statusCode < 200 || statusCode > 300)
        {
            logger.warn("Could not load {} for parsing. HTTP status code {}", uri, statusCode);
            throw new OpenGrapeResponseException.UnexpectedStatusCode(statusCode);
        }
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        StringBuilder htmlString = new StringBuilder();
        while ((line = reader.readLine()) != null)
            htmlString.append(line).append("\n");

        return parser.parse(htmlString.toString());
    }

    /**
     * @return Map of the Open Graph metadata, keyed by the {@link OpenGrapeMetadata} enums.
     * @throws OpenGrapeResponseException when the host returned an HTTP status not 2xx.
     */
    public Map<OpenGrapeMetadata, String> fetch(String url)
            throws IOException, OpenGrapeResponseException, URISyntaxException
    {
        return fetch(new URI(url));
    }

    /**
     * Loads the OG metadata of a url.
     * @param args url to load
     */
    public static void main(String[] args) throws OpenGrapeResponseException, IOException, URISyntaxException
    {
        System.out.println(new OpenGrape().fetch(new URI(args[0])));
    }
}
