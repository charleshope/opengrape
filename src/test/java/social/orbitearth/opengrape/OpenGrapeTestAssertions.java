package social.orbitearth.opengrape;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SameParameterValue")
public class OpenGrapeTestAssertions
{
    protected static void thenTitleIs(String title, OpenGrape og)
    {
        assertEquals(title, og.getValue(OpenGrapeMetadata.TITLE));
    }

    protected static void thenImageValueIs(String image, OpenGrape og)
    {
        assertEquals(image, og.getValue(OpenGrapeMetadata.IMAGE));
    }

    protected static void thenDescriptionValueIs(String image, OpenGrape og)
    {
        assertEquals(image, og.getValue(OpenGrapeMetadata.DESCRIPTION));
    }
}
