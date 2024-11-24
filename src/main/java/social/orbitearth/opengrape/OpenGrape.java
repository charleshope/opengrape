package social.orbitearth.opengrape;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenGrape
{
    private final static Logger logger = LoggerFactory.getLogger(OpenGrape.class);

    private final UserAgentFactory user_agent_factory;

    OpenGrape(UserAgentFactory user_agent_factory)
    {
        this.user_agent_factory = user_agent_factory;
    }

    public OpenGrape()
    {
        this(new UserAgentFactory());
    }

    @SuppressWarnings("ConstantValue")
    private Map<OpenGrapeMetadata, String> parse(Document document)
    {
        Map<OpenGrapeMetadata, String> open_grape_metadata = new HashMap<>();
        Elements meta_elements = document.select("meta");
        logger.debug("Found {} meta elements.", meta_elements.size());
        for (Element meta_element : meta_elements)
        {
            Attribute property = meta_element.attribute("property");
            if (property == null)
                continue;
            String property_value = property.getValue();
            if (!property_value.startsWith("og:"))
                continue;

            Attribute content = meta_element.attribute("content");
            if (content == null)
                continue;
            String content_value = content.getValue();

            logger.debug("Found a meta element with property=\"{}\", content={{}}", property_value, content_value);
            property_value = property_value.substring(3); // Remove the "og:" prefix
            OpenGrapeMetadata metadata = OpenGrapeMetadata.withValue(property_value);
            if (metadata != null)
                open_grape_metadata.put(metadata, content_value);
            else
                logger.warn("Invalid OG property {}", property_value);
        }

        return open_grape_metadata;
    }

    /**
     * @return Map of the Open Graph metadata, keyed by the {@link OpenGrapeMetadata} enums.
     */
    public Map<OpenGrapeMetadata, String> fetch(URI uri) throws IOException
    {
        String user_agent = user_agent_factory.getUserAgent(uri);
        Document document = Jsoup.connect(uri.toString())
          .userAgent(user_agent)
          .get();

        return parse(document);
    }

    /**
     * @return Map of the Open Graph metadata, keyed by the {@link OpenGrapeMetadata} enums.
     */
    public Map<OpenGrapeMetadata, String> fetch(String url)
            throws IOException, URISyntaxException
    {
        return fetch(new URI(url));
    }

    /**
     * Loads the OG metadata of a url.
     * @param args url to load
     */
    public static void main(String[] args) throws Exception
    {
        System.out.println(new OpenGrape().fetch(new URI(args[0])));
    }
}
