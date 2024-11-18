package social.orbitearth.opengrape;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static social.orbitearth.opengrape.OpenGrape.DEFAULT_USER_AGENT;
import static social.orbitearth.opengrape.OpenGrapeTestAssertions.*;

public class InternalOpenGrapeTest extends HttpTest
{
    @BeforeEach public void setUp() throws Exception
    {
        super.setUp();
    }

    @Test void fetch_standardOGBlock_parsedSuccessfully() throws IOException, OpenGrapeResponseException
    {
        String content = """
            <meta charset="utf-8">
            <title>The Open Graph protocol</title>
            <meta name="description" content="The Open Graph protocol enables any web page to become a rich object in a social graph.">
            <script type="text/javascript">var _sf_startpt=(new Date()).getTime()</script>
            <link rel="stylesheet" href="base.css" type="text/css">
            <meta property="og:title" content="Open Graph protocol">
            <meta property="og:type" content="website">
            <meta property="og:url" content="https://ogp.me/">
            <meta property="og:image" content="https://ogp.me/logo.png">
            <meta property="og:image:type" content="image/png">
            <meta property="og:image:width" content="300">
            <meta property="og:image:height" content="300">
            <meta property="og:image:alt" content="The Open Graph logo">
            <meta property="og:description" content="The Open Graph protocol enables any web page to become a rich object in a social graph.">
            <meta prefix="fb: https://ogp.me/ns/fb#" property="fb:app_id" content="115190258555800">""";
        FakeServer fakeServer = givenHTTPServer(content);

        OpenGrape og = OpenGrape.fetch(fakeServer.getServerURL());

        thenTitleIs("Open Graph protocol", og);
        thenImageValueIs("https://ogp.me/logo.png", og);
        thenDescriptionValueIs("The Open Graph protocol enables any web page to become a rich object in a social graph.",
                og);
    }

    @Test void fetch_noUserAgentPassed_sendsDefaultUserAgent() throws Exception
    {
        String content = """
            <meta charset="utf-8">
            <title>The Open Graph protocol</title>
            <meta name="description" content="The Open Graph protocol enables any web page to become a rich object in a social graph.">
            <script type="text/javascript">var _sf_startpt=(new Date()).getTime()</script>
            <link rel="stylesheet" href="base.css" type="text/css">
            <meta property="og:title" content="Open Graph protocol">
            <meta property="og:type" content="website">
            <meta property="og:url" content="https://ogp.me/">
            <meta property="og:image" content="https://ogp.me/logo.png">
            <meta property="og:image:type" content="image/png">
            <meta property="og:image:width" content="300">
            <meta property="og:image:height" content="300">
            <meta property="og:image:alt" content="The Open Graph logo">
            <meta property="og:description" content="The Open Graph protocol enables any web page to become a rich object in a social graph.">
            <meta prefix="fb: https://ogp.me/ns/fb#" property="fb:app_id" content="115190258555800">""";
        FakeServer fakeServer = givenHTTPServer(content);

        OpenGrape og = OpenGrape.fetch(fakeServer.getServerURL());

        assertEquals(DEFAULT_USER_AGENT, headers.get("user-agent"));
    }

    @Test void fetch_userAgentPassed_sendsUserAgent() throws Exception
    {
        String content = """
            <meta charset="utf-8">
            <title>The Open Graph protocol</title>
            <meta name="description" content="The Open Graph protocol enables any web page to become a rich object in a social graph.">
            <script type="text/javascript">var _sf_startpt=(new Date()).getTime()</script>
            <link rel="stylesheet" href="base.css" type="text/css">
            <meta property="og:title" content="Open Graph protocol">
            <meta property="og:type" content="website">
            <meta property="og:url" content="https://ogp.me/">
            <meta property="og:image" content="https://ogp.me/logo.png">
            <meta property="og:image:type" content="image/png">
            <meta property="og:image:width" content="300">
            <meta property="og:image:height" content="300">
            <meta property="og:image:alt" content="The Open Graph logo">
            <meta property="og:description" content="The Open Graph protocol enables any web page to become a rich object in a social graph.">
            <meta prefix="fb: https://ogp.me/ns/fb#" property="fb:app_id" content="115190258555800">""";
        FakeServer fakeServer = givenHTTPServer(content);

        OpenGrape.fetch(fakeServer.getServerURL(), "My user agent");

        assertEquals("My user agent", headers.get("user-agent"));
    }
}
