package social.orbitearth.opengrape;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;
import static social.orbitearth.opengrape.OpenGrapeTestAssertions.*;

public class InternalOpenGrapeTest extends HttpTest
{
    private static final String USER_AGENT_FROM_FACTORY = "User agent from factory";
    @Mock private UserAgentFactory user_agent_factory;
    private OpenGrape open_grape;

    @BeforeEach public void setUp() throws Exception
    {
        super.setUp();
        openMocks(this);

        open_grape = new OpenGrape(user_agent_factory);

        doReturn(USER_AGENT_FROM_FACTORY)
                .when(user_agent_factory)
                .getUserAgent(any(URI.class));
    }

    @Test void fetch_standardOGBlock_parsedSuccessfully()
            throws IOException, OpenGrapeResponseException, URISyntaxException
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

        var og = open_grape.fetch(fakeServer.getServerURL());

        thenTitleIs("Open Graph protocol", og);
        thenImageValueIs("https://ogp.me/logo.png", og);
        thenDescriptionValueIs("The Open Graph protocol enables any web page to become a rich object in a social graph.",
                og);
    }

    @Test void fetch_sendsUserAgentFromFactory() throws Exception
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

        var og = open_grape.fetch(fakeServer.getServerURL());

        assertEquals(USER_AGENT_FROM_FACTORY, headers.get("user-agent"));
        verify(user_agent_factory)
                .getUserAgent(eq(new URI(fakeServer.getServerURL())));
    }
}
