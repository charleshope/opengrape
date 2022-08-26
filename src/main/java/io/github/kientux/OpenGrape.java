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
import java.util.stream.Collectors;

public class OpenGrape {
    private final Map<OpenGrapeMetadata, String> source;

    public OpenGrape(String htmlString, OpenGrapeParser parser) {
        this.source = parser.parse(htmlString);
    }

    private OpenGrape() {
        source = new HashMap<>();
    }

    public static OpenGrape fetch(String url) throws IOException, OpenGrapeResponseException {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        int statusCode = connection.getResponseCode();
        if (statusCode < 200 || statusCode > 300) {
            throw new OpenGrapeResponseException.UnexpectedStatusCode();
        }
        InputStream inputStream = connection.getInputStream();
        String htmlString = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        return new OpenGrape(htmlString, new DefaultOpenGrapeParser());
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
                }
                if (e.getCause() instanceof OpenGrapeResponseException) {
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
