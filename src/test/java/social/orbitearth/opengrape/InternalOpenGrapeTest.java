package social.orbitearth.opengrape;

import java.net.URI;

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

    @Test void fetch_standardOGBlock_parsedSuccessfully() throws Exception
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

    /**
     * Some Twitter pages are in this alternate format.
     */
    @Test void fetch_twitterFormat2_succeeds() throws Exception
    {
        String content = """
<!DOCTYPE html>
<html dir="ltr" lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport"
          content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=0,viewport-fit=cover"/>
    <link rel="preconnect" href="//abs.twimg.com"/>
    <link rel="dns-prefetch" href="//abs.twimg.com"/>
    <link rel="preconnect" href="//api.twitter.com"/>
    <link rel="dns-prefetch" href="//api.twitter.com"/>
    <link rel="preconnect" href="//api.x.com"/>
    <link rel="dns-prefetch" href="//api.x.com"/>
    <link rel="preconnect" href="//pbs.twimg.com"/>
    <link rel="dns-prefetch" href="//pbs.twimg.com"/>
    <link rel="preconnect" href="//t.co"/>
    <link rel="dns-prefetch" href="//t.co"/>
    <link rel="preconnect" href="//video.twimg.com"/>
    <link rel="dns-prefetch" href="//video.twimg.com"/>
    <meta http-equiv="onion-location"
          content="https://twitter3e4tixl4xyajtrzo62zg5vztmjuricljdp2c5kshju4avyoid.onion/"/>
    <meta property="fb:app_id" content="2231777543"/>
    <meta content="X (formerly Twitter)" property="og:site_name"/>
    <meta name="google-site-verification" content="600dQ0pZYsH2xOFt4hYmf5f5NpjCbWE_qk5Y04dErYM"/>
    <meta name="facebook-domain-verification" content="x6sdcc8b5ju3bh8nbm59eswogvg6t1"/>
    <meta name="mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-title" content="Twitter"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="white"/>
    <link rel="search" type="application/opensearchdescription+xml" href="/opensearch.xml" title="Twitter">
    <link rel="manifest" href="/manifest.json" crossOrigin="use-credentials"/>
    <link rel="search" type="application/opensearchdescription+xml" href="/opensearch.xml" title="Twitter">
    <link rel="shortcut icon" href="//abs.twimg.com/favicons/twitter.3.ico">
    <meta name="theme-color" media="(prefers-color-scheme: light)" content="#FFFFFF"/>
    <meta name="theme-color" media="(prefers-color-scheme: dark)" content="#000000"/>
    <script type="application/ld+json">{
        "@context": "https://schema.org",
        "@type": "WebSite",
        "url": "https://twitter.com/",
        "potentialAction": {
            "@type": "SearchAction",
            "query-input": "required name=search_term_string",
            "target": {
                "@type": "EntryPoint",
                "urlTemplate": "https://twitter.com/search?q={search_term_string}&ref_src=twcamp%5Eseo_searchbox%7Ctwsrc%5Eseo"
            }
        }
    }</script>
    <meta http-equiv="origin-trial"
          content="AlpCmb40F5ZjDi9ZYe+wnr/V8MF+XmY41K4qUhoq+2mbepJTNd3q4CRqlACfnythEPZqcjryfAS1+ExS0FFRcA8AAABmeyJvcmlnaW4iOiJodHRwczovL3R3aXR0ZXIuY29tOjQ0MyIsImZlYXR1cmUiOiJMYXVuY2ggSGFuZGxlciIsImV4cGlyeSI6MTY1NTI1MTE5OSwiaXNTdWJkb21haW4iOnRydWV9"/>
    <style>html, body
    {
        height: 100%;
    }

    ::cue
    {
        white-space: normal
    }</style>
    <meta content="article" property="og:type"/>
    <meta content="https://x.com/EndWokeness/status/1859598454357389763?s=46" property="og:url"/>
    <meta content="End Wokeness (@EndWokeness) on X" property="og:title"/>
    <meta content="BREAKING: Manhattan DA Alvin Bragg&#x27;s assistant robbed, attacked by an illegal immigrant with 5 arrests since 2023.

He&#x27;s a Venezuelan TDA gang member.

Oh, and taxpayers paid for his hotel." property="og:description"/>
    <meta content="https://pbs.twimg.com/amplify_video_thumb/1859598314024448000/img/iaFE3Wbjm1rFXFF5.jpg:large"
          property="og:image"/>
    <meta content="BREAKING: Manhattan DA Alvin Bragg&#x27;s assistant robbed, attacked by an illegal immigrant with 5 arrests since 2023.

He&#x27;s a Venezuelan TDA gang member.

Oh, and taxpayers paid for his hotel." name="description"/>
    <meta content="End Wokeness (@EndWokeness) on X" name="title"/>
    <meta content="twitter://status?id=1859598454357389763" property="al:ios:url"/>
    <meta content="333903271" property="al:ios:app_store_id"/>
    <meta content="X" property="al:ios:app_name"/>
    <meta content="twitter://status?id=1859598454357389763" property="al:android:url"/>
    <meta content="com.twitter.android" property="al:android:package"/>
    <meta content="X" property="al:android:app_name"/>
</head>
<body style="background-color: #FFFFFF;">
<div id="react-root" style="height:100%;display:flex;"></div>""";
        FakeServer fakeServer = givenHTTPServer(content);

        var og = open_grape.fetch(fakeServer.getServerURL());

        thenTitleIs("End Wokeness (@EndWokeness) on X", og);
        thenImageValueIs("https://pbs.twimg.com/amplify_video_thumb/1859598314024448000/img/iaFE3Wbjm1rFXFF5.jpg:large", og);
        thenDescriptionValueIs("""
BREAKING: Manhattan DA Alvin Bragg's assistant robbed, attacked by an illegal immigrant with 5 arrests since 2023.

He's a Venezuelan TDA gang member.

Oh, and taxpayers paid for his hotel.""", og);
    }
}
