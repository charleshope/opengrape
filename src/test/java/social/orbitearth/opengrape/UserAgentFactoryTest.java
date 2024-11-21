package social.orbitearth.opengrape;

import java.net.URI;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static social.orbitearth.opengrape.UserAgentFactory.DEFAULT_USER_AGENT;
import static social.orbitearth.opengrape.UserAgentFactory.YOUTUBE_USER_AGENT;

class UserAgentFactoryTest
{
    private UserAgentFactory user_agent_factory = new UserAgentFactory();

    @Test void getUserAgent_randomUrl_defaultUserAgent() throws Exception
    {
        String user_agent = user_agent_factory.getUserAgent(new URI("file:///foo"));
        assertEquals(DEFAULT_USER_AGENT, user_agent);
    }

    @Test void getUserAgent_urlIncludingYoutube_defaultUserAgent() throws Exception
    {
        String user_agent = user_agent_factory.getUserAgent(new URI("https://notreallyyoutube.com/watch?v=ntEk6m-I7gU"));
        assertEquals(DEFAULT_USER_AGENT, user_agent);
    }

    @Test void getUserAgent_longYoutubeUrl_youtubeUserAgent() throws Exception
    {
        String user_agent = user_agent_factory.getUserAgent(new URI("https://youtube.com/watch?v=ntEk6m-I7gU"));
        assertEquals(YOUTUBE_USER_AGENT, user_agent);
    }

    @Test void getUserAgent_wwwLongYoutubeUrl_youtubeUserAgent() throws Exception
    {
        String user_agent = user_agent_factory.getUserAgent(new URI("https://www.youtube.com/watch?v=ntEk6m-I7gU"));
        assertEquals(YOUTUBE_USER_AGENT, user_agent);
    }

    @Test void getUserAgent_shortYoutubeUrl_youtubeUserAgent() throws Exception
    {
        String user_agent = user_agent_factory.getUserAgent(new URI("https://youtu.be/ntEk6m-I7gU?si=2qPNtNYsFCGtjBL1"));
        assertEquals(YOUTUBE_USER_AGENT, user_agent);
    }
}