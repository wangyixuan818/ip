package noms.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import noms.task.Deadline;
import noms.task.Event;
import noms.task.Task;
import noms.task.ToDo;

/**
 * Tests {@link Storage}, the persistence layer. These cases use a JUnit
 * {@link TempDir temporary directory} so no real save file is touched.
 *
 * Persistence is critical, core logic: it must round-trip every task type
 * without loss, tolerate a corrupted save file rather than crash on startup,
 * and preserve descriptions containing the {@code |}/{@code \} characters
 * used by the file format itself. Each of those properties is checked here.
 */
public class StorageTest {

    @TempDir
    private Path tempDir;

    private Storage storageIn(Path dir) {
        return new Storage(dir.toString(), "noms.txt");
    }

    @Test
    public void load_missingFile_returnsEmptyList() throws IOException {
        // First run on a fresh machine: no save file yet is not an error.
        assertEquals(List.of(), storageIn(tempDir).load());
    }

    @Test
    public void saveThenLoad_roundTripsAllTaskTypes() throws IOException {
        Storage storage = storageIn(tempDir);
        Deadline deadline = new Deadline("return book", LocalDate.of(2019, 6, 6));
        deadline.markAsDone();
        List<Task> original = List.of(
                new ToDo("read book"),
                deadline,
                new Event("meeting", LocalDate.of(2019, 8, 6), LocalDate.of(2019, 8, 7)));

        storage.save(original);
        List<Task> loaded = storage.load();

        // Compare via the save-file form: it captures type, done-flag, and dates.
        assertEquals(fileForms(original), fileForms(loaded));
    }

    @Test
    public void saveThenLoad_preservesPipeAndBackslashInDescription() throws IOException {
        Storage storage = storageIn(tempDir);
        List<Task> original = List.of(
                new ToDo("buy milk | bread"),
                new Deadline("audit C:\\logs\\a\\|b", LocalDate.of(2019, 11, 8)));

        storage.save(original);
        List<Task> loaded = storage.load();

        assertEquals("buy milk | bread", loaded.get(0).getDescription());
        assertEquals("audit C:\\logs\\a\\|b", loaded.get(1).getDescription());
    }

    @Test
    public void load_unparseableLine_isSkippedAndRecorded() throws IOException {
        Path file = tempDir.resolve("noms.txt");
        Files.writeString(file,
                "T | 0 | good task\n"
                        + "GARBAGE LINE\n"
                        + "D | 1 | pay rent | 2019-12-15\n");
        Storage storage = storageIn(tempDir);

        List<Task> loaded = storage.load();

        // The two good tasks load; the bad line is skipped, not fatal.
        assertEquals(2, loaded.size());
        assertEquals("good task", loaded.get(0).getDescription());
        assertEquals("pay rent", loaded.get(1).getDescription());
        assertEquals(List.of("GARBAGE LINE"), storage.getSkippedLines());
    }

    @Test
    public void load_blankLines_areIgnored() throws IOException {
        Path file = tempDir.resolve("noms.txt");
        Files.writeString(file, "\nT | 0 | read book\n\n");
        Storage storage = storageIn(tempDir);

        List<Task> loaded = storage.load();

        assertEquals(1, loaded.size());
        assertEquals(List.of(), storage.getSkippedLines());
    }

    /** Maps tasks to their save-file lines, for comparing task lists by content. */
    private static List<String> fileForms(List<Task> tasks) {
        return tasks.stream().map(Task::toFileFormat).toList();
    }
}
