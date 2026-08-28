package noms.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import noms.exception.NomsException;
import noms.storage.Storage;
import noms.task.TaskList;
import noms.task.ToDo;
import noms.ui.Ui;

/**
 * Tests {@link UnmarkCommand#execute}, the counterpart to {@link MarkCommand}.
 * Starting from a task that is already done, the tests assert that executing
 * unmark clears the completion state both in the list and on disk, and that
 * an invalid task number is rejected.
 */
public class UnmarkCommandTest {

    @TempDir
    private Path tempDir;

    private final PrintStream realOut = System.out;
    private Ui ui;
    private Storage storage;
    private TaskList tasks;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
        ui = new Ui();
        storage = new Storage(tempDir.toString(), "noms.txt");
        tasks = new TaskList();
        ToDo todo = new ToDo("read book");
        todo.markAsDone(); // start done so unmark has an effect to observe
        tasks.add(todo);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(realOut);
    }

    @Test
    public void execute_validNumber_marksTaskNotDone() throws NomsException {
        new UnmarkCommand("unmark 1").execute(tasks, ui, storage);

        assertEquals(" ", tasks.get(0).getStatusIcon());
    }

    @Test
    public void execute_validNumber_persistsNotDoneState() throws NomsException, IOException {
        new UnmarkCommand("unmark 1").execute(tasks, ui, storage);

        assertEquals(" ", storage.load().get(0).getStatusIcon());
    }

    @Test
    public void execute_missingNumber_throwsNomsException() {
        assertThrows(NomsException.class,
                () -> new UnmarkCommand("unmark").execute(tasks, ui, storage));
    }
}
