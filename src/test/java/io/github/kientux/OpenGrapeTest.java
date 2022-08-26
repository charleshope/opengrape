package io.github.kientux;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OpenGrapeTest {

    @Test
    public void givenUrl_whenParseOGTag_thenCorrect() throws Exception {
        String url = "https://opengraphprotocol.org/";
        OpenGrape og = OpenGrape.fetch(url);
        String title = og.getValue(OpenGrapeMetadata.TITLE);
        String description = og.getValue(OpenGrapeMetadata.DESCRIPTION);
        String image = og.getValue(OpenGrapeMetadata.IMAGE);
        assertEquals("Open Graph protocol", title);
        assertEquals("https://ogp.me/logo.png", image);
        assertEquals("The Open Graph protocol enables any web page to become a rich object in a social graph.",
                description);
    }

    @Test
    public void givenShortenedUrl_whenParseOGTag_thenCorrect() throws Exception {
        String url = "https://bit.ly/3SGKNmU";
        OpenGrape og = OpenGrape.fetch(url);
        String title = og.getValue(OpenGrapeMetadata.TITLE);
        String description = og.getValue(OpenGrapeMetadata.DESCRIPTION);
        String image = og.getValue(OpenGrapeMetadata.IMAGE);
        assertEquals("Open Graph protocol", title);
        assertEquals("https://ogp.me/logo.png", image);
        assertEquals("The Open Graph protocol enables any web page to become a rich object in a social graph.",
                description);
    }

    @Test
    public void givenShortenedUrlWithTrailingSlash_whenParseOGTag_thenCorrect() throws Exception {
        String url = "https://bit.ly/3SGKNmU/";
        OpenGrape og = OpenGrape.fetch(url);
        String title = og.getValue(OpenGrapeMetadata.TITLE);
        String description = og.getValue(OpenGrapeMetadata.DESCRIPTION);
        String image = og.getValue(OpenGrapeMetadata.IMAGE);
        assertEquals("Open Graph protocol", title);
        assertEquals("https://ogp.me/logo.png", image);
        assertEquals("The Open Graph protocol enables any web page to become a rich object in a social graph.",
                description);
    }

    @Test
    public void givenUrl_whenParseOGTagWithTimeout_thenCorrect() throws Exception {
        String url = "https://opengraphprotocol.org/";
        OpenGrape og = OpenGrape.fetch(url, 5);
        String title = og.getValue(OpenGrapeMetadata.TITLE);
        String description = og.getValue(OpenGrapeMetadata.DESCRIPTION);
        String image = og.getValue(OpenGrapeMetadata.IMAGE);
        assertEquals("Open Graph protocol", title);
        assertEquals("https://ogp.me/logo.png", image);
        assertEquals("The Open Graph protocol enables any web page to become a rich object in a social graph.",
                description);
    }

    @Test
    public void givenUrlThatNeverReturns_whenParseOGTagWithTimeout_thenReturns() throws Exception {
        String url = "https://www.nasdaq.com/articles/whole-foods-is-sued-over-no-antibiotics-ever-beef-claim";
        OpenGrape og = OpenGrape.fetch(url, 1);
        String title = og.getValue(OpenGrapeMetadata.TITLE);
        String description = og.getValue(OpenGrapeMetadata.DESCRIPTION);
        String image = og.getValue(OpenGrapeMetadata.IMAGE);
        assertNull(title);
        assertNull(image);
        assertNull(description);
    }
}
