package noms;

import java.io.IOException;

import noms.command.Command;
import noms.exception.NomsException;
import noms.parser.Parser;
import noms.storage.Storage;
import noms.task.TaskList;
import noms.ui.Ui;

/**
 * Entry point and top-level coordinator for the Noms task manager.
 *
 * Noms itself no longer contains any UI, parsing, or storage logic: it
 * simply wires together a {@link Ui}, a {@link Storage}, and a
 * {@link TaskList}, and drives the read-evaluate-respond loop, delegating
 * each concern to the class that owns it.
 */
public class Noms {
    private static final String SAVE_DIRECTORY = "data";
    private static final String SAVE_FILE_NAME = "noms.txt";

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates a Noms instance backed by the save file in the given
     * directory, loading any previously saved tasks. If the file cannot be
     * read, Noms starts with an empty list and reports the problem rather
     * than crashing.
     *
     * @param directory folder containing the save file, relative to the project root
     * @param fileName name of the save file within that folder
     */
    public Noms(String directory, String fileName) {
        ui = new Ui();
        storage = new Storage(directory, fileName);

        TaskList loaded;
        try {
            loaded = new TaskList(storage.load());
            for (String spoiledLine : storage.getSkippedLines()) {
                ui.showError("Noms found a spoiled entry in the save file and skipped it: " + spoiledLine);
            }
        } catch (IOException e) {
            loaded = new TaskList();
            ui.showError("Noms couldn't load the saved menu, starting with an empty plate: " + e.getMessage());
        }
        tasks = loaded;
    }

    /**
     * Runs the main loop: greets the user, then repeatedly reads a command,
     * turns it into a {@link Command}, executes it, and continues until an
     * exit command runs or the input ends. Any command error is reported
     * and the loop keeps going.
     */
    public void run() {
        ui.showWelcome();

        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            String fullCommand = ui.readCommand();
            try {
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (NomsException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.close();
    }

    public static void main(String[] args) {
        new Noms(SAVE_DIRECTORY, SAVE_FILE_NAME).run();
    }
}
