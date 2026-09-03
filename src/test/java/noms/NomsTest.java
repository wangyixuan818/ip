package noms;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.OutputStream;
import java.io.PrintStream;
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
        String response = noms.getResponse("todo read book");

        assertTrue(response.contains("read book"), "response should mention the added task");
        assertFalse(response.contains("____"), "GUI response should not contain console dividers");
    }

    @Test
    public void getResponse_unknownCommand_returnsNonBlankErrorMessage() {
        String response = noms.getResponse("blahblah");

        assertFalse(response.isBlank(), "an unknown command should still produce a reply");
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
