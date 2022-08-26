package io.github.kientux;

import io.github.kientux.parser.DefaultOpenGrapeParser;
import io.github.kientux.parser.OpenGrapeParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class OpenGrape {
    private final Map<OpenGrapeMetadata, String> source;

    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 12_5_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36";

    public OpenGrape(String htmlString, OpenGrapeParser parser) {
        this.source = parser.parse(htmlString);
    }

    private OpenGrape() {
        source = new HashMap<>();
    }

    public static OpenGrape fetch(String url) throws IOException, OpenGrapeResponseException {
        return fetch(url, DEFAULT_USER_AGENT);
    }

    public static OpenGrape fetch(String url, String userAgent) throws IOException, OpenGrapeResponseException {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        // some URLs need User-Agent header or else HTML source won't contain any OG tags
        connection.setRequestProperty("User-Agent", userAgent);
        int statusCode = connection.getResponseCode();
        if (statusCode < 200 || statusCode > 300) {
            throw new OpenGrapeResponseException.UnexpectedStatusCode();
        }
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        StringBuilder htmlString = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            htmlString.append(line).append("\n");
        }
        return new OpenGrape(htmlString.toString(), new DefaultOpenGrapeParser());
    }

    public static OpenGrape fetch(String url, int timeoutSeconds) throws IOException, OpenGrapeResponseException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<OpenGrape> future = executor.submit(() -> OpenGrape.fetch(url));
        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
        } catch (ExecutionException e) {
            if (e.getCause() != null) {
                if (e.getCause() instanceof IOException) {
                    throw (IOException) e.getCause();
                } else if (e.getCause() instanceof OpenGrapeResponseException) {
                    throw (OpenGrapeResponseException) e.getCause();
                }
            }
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdownNow();
        }
        return new OpenGrape();
    }

    public String getValue(OpenGrapeMetadata metadata) {
        return source.get(metadata);
    }
}
