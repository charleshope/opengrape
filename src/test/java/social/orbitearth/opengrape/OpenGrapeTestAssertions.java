package social.orbitearth.opengrape;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SameParameterValue")
public class OpenGrapeTestAssertions
{
    protected static void thenTitleIs(String title, Map<OpenGrapeMetadata, String> og)
    {
        assertEquals(title, og.get(OpenGrapeMetadata.TITLE));
    }

    protected static void thenImageValueIs(String image, Map<OpenGrapeMetadata, String> og)
    {
        assertEquals(image, og.get(OpenGrapeMetadata.IMAGE));
    }

    protected static void thenDescriptionValueIs(String image, Map<OpenGrapeMetadata, String> og)
    {
        assertEquals(image, og.get(OpenGrapeMetadata.DESCRIPTION));
    }
}
