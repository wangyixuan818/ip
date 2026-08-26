import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Handles saving Noms' task list to a fixed location on the hard disk, so
 * that the list can persist across runs.
 *
 * Loading previously saved tasks back in is not yet implemented; this class
 * currently only covers the "save on every change" half of that behaviour.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a storage that reads from and writes to the given file path.
     *
     * @param filePath path to the save file, relative to the project root
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
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
}
