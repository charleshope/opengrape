package social.orbitearth.opengrape;

import java.net.URI;

public class UserAgentFactory
{
    static final String DEFAULT_USER_AGENT = "WhatsApp/2.2336.9 N";
    static final String YOUTUBE_USER_AGENT = "facebookexternalhit/1.1";

    public String getUserAgent(URI url)
    {
        String host = url.getHost();

        if (host != null)
            if (host.equals("youtube.com") || host.equals("www.youtube.com") || host.equals("youtu.be"))
                return YOUTUBE_USER_AGENT;

        return DEFAULT_USER_AGENT;
    }
}
