package noms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests {@link Noms#getResponse}, the integration seam the GUI relies on. It
 * must run the same parse-execute pipeline as the console loop, return clean
 * text with no console dividers, and report when the user asked to exit.
 * Standard output is redirected during each test so the captured prints do not
 * clutter the test log.
 */
public class NomsTest {
    @TempDir
    private Path tempDir;

    private final PrintStream realOut = System.out;
    private Noms noms;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
        noms = new Noms(tempDir.toString(), "noms.txt");
    }

    @AfterEach
    public void tearDown() {
        System.setOut(realOut);
    }

    @Test
    public void getResponse_addTodo_confirmsAndStripsDividers() {
        NomsResponse response = noms.getResponse("todo read book");

        assertTrue(response.text().contains("read book"), "response should mention the added task");
        assertFalse(response.text().contains("____"), "GUI response should not contain console dividers");
        assertEquals(ResponseType.NORMAL, response.type());
        assertFalse(response.isError());
    }

    @Test
    public void getResponse_unknownCommand_returnsNonBlankErrorMessage() {
        NomsResponse response = noms.getResponse("blahblah");

        assertFalse(response.text().isBlank(), "an unknown command should still produce a reply");
        assertEquals(ResponseType.ERROR, response.type());
        assertTrue(response.isError());
    }

    @Test
    public void getResponse_validCommandAfterError_resetsResponseType() {
        noms.getResponse("blahblah");

        NomsResponse response = noms.getResponse("list");

        assertEquals(ResponseType.NORMAL, response.type());
    }

    @Test
    public void getResponse_storageFailure_returnsErrorResponse() throws IOException {
        Path blockedDirectory = tempDir.resolve("blocked-directory");
        Files.writeString(blockedDirectory, "not a directory");
        Noms nomsWithBlockedStorage = new Noms(blockedDirectory.toString(), "noms.txt");

        NomsResponse response = nomsWithBlockedStorage.getResponse("todo read book");

        assertEquals(ResponseType.ERROR, response.type());
    }

    @Test
    public void getResponse_byeCommand_marksExitRequested() {
        assertFalse(noms.isExitRequested(), "exit should not be requested before any command");

        noms.getResponse("bye");

        assertTrue(noms.isExitRequested(), "bye should mark the app as ready to exit");
    }

    @Test
    public void getResponse_nonExitCommand_doesNotRequestExit() {
        noms.getResponse("list");

        assertFalse(noms.isExitRequested(), "a non-exit command should leave the app running");
    }
}
