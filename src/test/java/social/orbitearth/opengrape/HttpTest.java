package social.orbitearth.opengrape;

import java.io.IOException;
import java.util.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import fi.iki.elonen.NanoHTTPD;

import static org.junit.jupiter.api.Assertions.fail;

abstract public class HttpTest
{
    private static int FAKE_SERVER_PORT = 12345;

    /**
     * Sent by the last request to the fake server.
     */
    protected static Map<String, List<String>> parameters = new HashMap<>();

    /**
     * Sent by the last request to the fake server.
     */
    protected static Map<String, String> headers = new HashMap<>();
    private FakeServer fake_server;

    protected FakeServer givenHTTPServer(NanoHTTPD.Response.Status status, String content) throws IOException
    {
        fake_server = new FakeServer(status, content, FAKE_SERVER_PORT);
        FAKE_SERVER_PORT++;
        return fake_server;
    }

    protected FakeServer givenHTTPServer(String content) throws IOException
    {
        return givenHTTPServer(NanoHTTPD.Response.Status.OK, content);
    }

    protected FakeServer getFakeServer()
    {
        if (fake_server == null)
            throw new IllegalStateException("No fake server was instantiated. Did you call givenHTTPServer() yet?");
        return fake_server;
    }

    public static class FakeServer extends NanoHTTPD
    {
        private final int port;
        private final String content;
        private final NanoHTTPD.Response.Status status;

        FakeServer(Response.Status status, String content, int port) throws IOException
        {
            super(port);
            this.content = content;
            this.status = status;
            this.port = port;
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        }

        @Override public Response serve(IHTTPSession session)
        {
            try
            {
                session.parseBody(new HashMap<>());
            }
            catch (IOException | ResponseException e)
            {
                fail(e.getMessage());
            }

            parameters = session.getParameters();
            headers = session.getHeaders();
            return newFixedLengthResponse(status, "text/html", content);
        }

        public int getPort()
        {
            return port;
        }

        public String getServerURL()
        {
            return "http://127.0.0.1:" + getPort();
        }
    }

    @BeforeEach public void setUp() throws Exception
    {
        parameters.clear();
        headers.clear();
    }

    @AfterEach public void tearDown()
    {
        if (fake_server != null)
            fake_server.stop();
    }
}
