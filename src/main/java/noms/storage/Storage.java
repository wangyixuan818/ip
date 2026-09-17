package noms.storage;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import noms.task.Deadline;
import noms.task.Event;
import noms.task.Task;
import noms.task.ToDo;
import noms.ui.Ui;

/**
 * Handles saving Noms' task list to, and loading it back from, a fixed
 * location on the hard disk, so that the list can persist across runs.
 */
public class Storage {
    private static final String FIELD_SEPARATOR_REGEX = " \\| ";
    private static final String COMPLETED_FLAG = "1";
    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";

    private static final int TYPE_FIELD = 0;
    private static final int COMPLETION_FIELD = 1;
    private static final int DESCRIPTION_FIELD = 2;
    private static final int DATE_FIELD = 3;
    private static final int EVENT_END_DATE_FIELD = 4;

    private final Path filePath;
    private final List<String> skippedLines = new ArrayList<>();

    /**
     * Creates a storage that reads from and writes to a file in the given
     * directory. The path is built from separate directory and file name
     * segments (rather than a single string containing a hardcoded
     * separator) so that it resolves correctly on any operating system.
     *
     * @param directory path to the folder containing the save file, relative to the project root
     * @param fileName name of the save file within that folder
     */
    public Storage(String directory, String fileName) {
        this.filePath = Path.of(directory, fileName);
    }

    /**
     * Writes the given tasks to a temporary sibling file before replacing
     * the save file. This keeps the previous file intact if content writing
     * fails and uses an atomic replacement when the filesystem supports it.
     *
     * @param tasks the current task list to save
     * @throws IOException if the file or its parent directory cannot be written
     */
    public void save(List<Task> tasks) throws IOException {
        Path absoluteFilePath = filePath.toAbsolutePath();
        Path parent = absoluteFilePath.getParent();
        Files.createDirectories(parent);

        StringBuilder content = new StringBuilder();
        for (Task task : tasks) {
            content.append(task.toFileFormat()).append(System.lineSeparator());
        }

        Path temporaryFile = Files.createTempFile(
                parent, absoluteFilePath.getFileName().toString() + ".", ".tmp");
        try {
            Files.writeString(temporaryFile, content.toString());
            replaceSaveFile(temporaryFile, absoluteFilePath);
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    /**
     * Replaces the save file with a completed temporary file.
     *
     * @param temporaryFile the fully written temporary file
     * @param targetFile the live save file to replace
     * @throws IOException if neither atomic nor normal replacement succeeds
     */
    protected void replaceSaveFile(Path temporaryFile, Path targetFile) throws IOException {
        try {
            Files.move(temporaryFile, targetFile,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temporaryFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Loads previously saved tasks from disk.
     *
     * If the save file (or its containing folder) does not exist yet — the
     * normal situation the first time Noms is run on a computer — this
     * simply returns an empty list rather than treating it as an error.
     * Any line that cannot be parsed (e.g. from a hand-edited or corrupted
     * save file) is skipped instead of aborting the load, and recorded so
     * the caller can report it through the {@link Ui}; see
     * {@link #getSkippedLines()}. Storage itself never writes to the
     * console, keeping all user-facing output in one place.
     *
     * @return the tasks read from the save file, or an empty list if there is none yet
     * @throws IOException if the save file exists but cannot be read
     */
    public List<Task> load() throws IOException {
        List<Task> tasks = new ArrayList<>();
        skippedLines.clear();

        if (!Files.exists(filePath)) {
            return tasks;
        }

        for (String line : Files.readAllLines(filePath)) {
            if (line.isBlank()) {
                continue;
            }

            try {
                tasks.add(parseLine(line));
            } catch (RuntimeException e) {
                skippedLines.add(line);
            }
        }

        return tasks;
    }

    /**
     * Returns the raw save-file lines skipped during the most recent
     * {@link #load()} because they could not be parsed, in the order they
     * appeared. The list is empty if every line loaded successfully.
     *
     * @return a copy of the skipped lines, so callers cannot alter this Storage
     */
    public List<String> getSkippedLines() {
        return new ArrayList<>(skippedLines);
    }

    /**
     * Parses a single save-file line back into a {@link Task}, reversing
     * the format produced by {@link Task#toFileFormat()}.
     *
     * @throws RuntimeException if the line is missing fields or has an unrecognized type letter
     */
    private Task parseLine(String line) {
        String[] fields = line.split(FIELD_SEPARATOR_REGEX);
        String type = fields[TYPE_FIELD];
        boolean isDone = fields[COMPLETION_FIELD].equals(COMPLETED_FLAG);
        String description = Task.unescape(fields[DESCRIPTION_FIELD]);

        Task task;
        switch (type) {
            case TODO_TYPE:
                task = new ToDo(description);
                break;
            case DEADLINE_TYPE:
                task = new Deadline(description,
                        LocalDate.parse(Task.unescape(fields[DATE_FIELD])));
                break;
            case EVENT_TYPE:
                task = new Event(description,
                        LocalDate.parse(Task.unescape(fields[DATE_FIELD])),
                        LocalDate.parse(Task.unescape(fields[EVENT_END_DATE_FIELD])));
                break;
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
