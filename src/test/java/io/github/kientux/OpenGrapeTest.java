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
    public void givenUrlNeedUserAgent_whenParseOGTag_thenCorrect() throws Exception {
        // reddit URL need User-Agent header or else HTML source won't contain any OG tags
        String url = "https://www.reddit.com/r/PublicFreakout/comments/wrkily/finlands_prime_minister_sanna_marin_is_facing/";
        OpenGrape og = OpenGrape.fetch(url);
        String title = og.getValue(OpenGrapeMetadata.TITLE);
        String image = og.getValue(OpenGrapeMetadata.IMAGE);
        assertEquals("r/PublicFreakout - Finland&#x27;s prime minister Sanna Marin is facing backlash after this video", title);
        assertEquals("https://external-preview.redd.it/UtEJebb4MwCvUmTPWr65wu2WB_w4dj2vuDvJkl3xllM.png?format=pjpg&amp;auto=webp&amp;s=348724e3c8d56cbede3cba151e7af613cba8b052", image);
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
