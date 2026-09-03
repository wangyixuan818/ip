package noms.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
     * Writes the given tasks to disk, one per line, overwriting any
     * previous contents of the save file. Creates the parent directory
     * first if it does not already exist.
     *
     * @param tasks the current task list to save
     * @throws IOException if the file or its parent directory cannot be written
     */
    public void save(List<Task> tasks) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        StringBuilder content = new StringBuilder();
        for (Task task : tasks) {
            content.append(task.toFileFormat()).append(System.lineSeparator());
        }

        Files.writeString(filePath, content.toString());
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
        String[] fields = line.split(" \\| ");
        String type = fields[0];
        boolean isDone = fields[1].equals("1");
        String description = Task.unescape(fields[2]);

        Task task;
        switch (type) {
            case "T":
                task = new ToDo(description);
                break;
            case "D":
                task = new Deadline(description, LocalDate.parse(Task.unescape(fields[3])));
                break;
            case "E":
                task = new Event(description,
                        LocalDate.parse(Task.unescape(fields[3])),
                        LocalDate.parse(Task.unescape(fields[4])));
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
