package social.orbitearth.opengrape;

import org.junit.jupiter.api.Test;

import static social.orbitearth.opengrape.OpenGrapeTestAssertions.*;

public class ExternalOpenGrapeTest extends HttpTest
{
    @Test public void givenShortenedUrl_whenParseOGTag_thenCorrect() throws Exception
    {
        String url = "https://bit.ly/3SGKNmU";

        OpenGrape og = OpenGrape.fetch(url);

        thenTitleIs("Open Graph protocol", og);
        thenImageValueIs("https://ogp.me/logo.png", og);
        thenDescriptionValueIs("The Open Graph protocol enables any web page to become a rich object in a social graph.",
                og);
    }

    @Test public void givenShortenedUrlWithTrailingSlash_whenParseOGTag_thenCorrect() throws Exception
    {
        String url = "https://bit.ly/3SGKNmU/";

        OpenGrape og = OpenGrape.fetch(url);

        thenTitleIs("Open Graph protocol", og);
        thenImageValueIs("https://ogp.me/logo.png", og);
        thenDescriptionValueIs("The Open Graph protocol enables any web page to become a rich object in a social graph.",
                og);
    }
}
