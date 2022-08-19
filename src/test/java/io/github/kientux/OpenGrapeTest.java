package io.github.kientux;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class OpenGrapeTest {

    @Test
    public void givenUrl_whenParseOGTag_thenCorrect() {
        String url = "https://opengraphprotocol.org/";
        try {
            OpenGrape og = OpenGrape.fetch(url);
            String title = og.getValue(OpenGrapeMetadata.TITLE);
            String description = og.getValue(OpenGrapeMetadata.DESCRIPTION);
            String image = og.getValue(OpenGrapeMetadata.IMAGE);
            assertEquals("Open Graph protocol", title);
            assertEquals("https://ogp.me/logo.png", image);
            assertEquals("The Open Graph protocol enables any web page to become a rich object in a social graph.", description);
        } catch (IOException | OpenGrapeResponseException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void givenShortenedUrl_whenParseOGTag_thenCorrect() {
        String url = "https://bit.ly/3SGKNmU";
        try {
            OpenGrape og = OpenGrape.fetch(url);
            String title = og.getValue(OpenGrapeMetadata.TITLE);
            String description = og.getValue(OpenGrapeMetadata.DESCRIPTION);
            String image = og.getValue(OpenGrapeMetadata.IMAGE);
            assertEquals("Open Graph protocol", title);
            assertEquals("https://ogp.me/logo.png", image);
            assertEquals("The Open Graph protocol enables any web page to become a rich object in a social graph.", description);
        } catch (IOException | OpenGrapeResponseException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void givenShortenedUrlWithTrailingSlash_whenParseOGTag_thenCorrect() {
        String url = "https://bit.ly/3SGKNmU/";
        try {
            OpenGrape og = OpenGrape.fetch(url);
            String title = og.getValue(OpenGrapeMetadata.TITLE);
            String description = og.getValue(OpenGrapeMetadata.DESCRIPTION);
            String image = og.getValue(OpenGrapeMetadata.IMAGE);
            assertEquals("Open Graph protocol", title);
            assertEquals("https://ogp.me/logo.png", image);
            assertEquals("The Open Graph protocol enables any web page to become a rich object in a social graph.", description);
        } catch (IOException | OpenGrapeResponseException e) {
            e.printStackTrace();
            fail();
        }
    }
}
