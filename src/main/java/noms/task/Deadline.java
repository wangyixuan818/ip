package noms.task;

import java.time.LocalDate;

import noms.util.DateUtil;

/** Represents a task that must be completed by a specific date. */
public class Deadline extends Task {

    protected final LocalDate by;

    /**
     * Creates a deadline with the given description and due date.
     *
     * @param description the text describing the task
     * @param by the date the task is due
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /** Returns true if this deadline falls on the given date. */
    public boolean occursOn(LocalDate date) {
        return by.equals(date);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + DateUtil.format(by) + ")";
    }

    @Override
    public String toFileFormat() {
        // LocalDate.toString() writes ISO yyyy-MM-dd, which round-trips
        // exactly with LocalDate.parse when the save file is read back.
        return "D | " + super.toFileFormat() + " | " + escape(by.toString());
    }
}
