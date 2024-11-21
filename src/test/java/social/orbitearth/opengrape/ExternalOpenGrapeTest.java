package social.orbitearth.opengrape;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static social.orbitearth.opengrape.OpenGrapeTestAssertions.*;

public class ExternalOpenGrapeTest
{
    private OpenGrape open_grape;

    @BeforeEach public void setUp() throws Exception
    {
        open_grape = new OpenGrape();
    }

    @Test public void givenShortenedUrl_whenParseOGTag_thenCorrect() throws Exception
    {
        String url = "https://bit.ly/3SGKNmU";

        var og = open_grape.fetch(url);

        thenTitleIs("Open Graph protocol", og);
        thenImageValueIs("https://ogp.me/logo.png", og);
        thenDescriptionValueIs("The Open Graph protocol enables any web page to become a rich object in a social graph.",
                og);
    }

    @Test void fetch_twitter_loadsOpenGraphData() throws Exception
    {
        var og = open_grape.fetch("https://x.com/PopBase/status/1858024727589314714");

        thenTitleIs("Pop Base (@PopBase) on X", og);
        thenImageValueIs("https://pbs.twimg.com/media/GckHpBta8AA9WAk.jpg:large", og);
        thenDescriptionValueIs("Miss Denmark wins Miss Universe 2024.", og);
    }

    @Test void fetch_youtube_loadsOpenGraphData() throws Exception
    {
        var og = open_grape.fetch("https://www.youtube.com/watch?v=ntEk6m-I7gU");

        thenTitleIs("ASMR Laying down in Nature, Energy clearing, Personal attention.", og);
        thenImageValueIs("https://i.ytimg.com/vi/ntEk6m-I7gU/maxresdefault.jpg", og);
        thenDescriptionValueIs("Tuning fork, singing bowl , wooden frog! All your faves in this video!I love " +
                               "outdoors ASMR, Lay down with me by the lake and relax, I will help you let go of...",
                og);
    }
}
