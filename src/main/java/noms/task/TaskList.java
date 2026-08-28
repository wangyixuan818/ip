package noms.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds Noms' tasks and provides the operations for changing and querying
 * them (adding, deleting, retrieving, and finding tasks on a date).
 *
 * Keeping this logic in its own class means the rest of the program works
 * with intention-revealing methods such as {@code add} and {@code delete}
 * instead of manipulating a raw list directly.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the given tasks, typically the ones
     * loaded from the save file at startup.
     *
     * @param tasks the initial tasks; the list is copied so later changes
     *              here do not affect the caller's list
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns the number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the given zero-based index.
     *
     * @param index position of the task, from 0 to {@code size() - 1}
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given zero-based index. Tasks
     * after it shift down to fill the gap, renumbering the list.
     *
     * @param index position of the task to remove
     * @return the task that was removed
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the deadlines and events that occur on the given date, in
     * their original list order. Todos never match because they have no
     * date.
     *
     * @param date the date to filter by
     */
    public List<Task> tasksOn(LocalDate date) {
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            boolean occurs = task instanceof Deadline d && d.occursOn(date)
                    || task instanceof Event e && e.occursOn(date);
            if (occurs) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns the tasks whose description contains the given keyword, in
     * their original list order. Matching is case-insensitive, so
     * {@code find BOOK} still matches a task described as "read book".
     *
     * @param keyword the text to search for within each task's description
     */
    public List<Task> find(String keyword) {
        String needle = keyword.toLowerCase();
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(needle)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns the tasks as a plain list, for displaying or saving. The
     * returned list is a copy, so modifying it does not change this
     * TaskList.
     */
    public List<Task> asList() {
        return new ArrayList<>(tasks);
    }
}
