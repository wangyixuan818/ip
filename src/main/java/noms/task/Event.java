package noms.task;

import java.time.LocalDate;

import noms.util.DateUtil;

/** Represents a task that takes place between two stated dates. */
public class Event extends Task {
    private final LocalDate from;
    private final LocalDate to;

    /**
     * Creates an event with the given description and start and end dates.
     *
     * @param description the text describing the task
     * @param from the date the event starts
     * @param to the date the event ends
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** Returns true if this event spans (inclusively) the given date. */
    public boolean occursOn(LocalDate date) {
        return !date.isBefore(from) && !date.isAfter(to);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + DateUtil.format(from)
                + " to: " + DateUtil.format(to) + ")";
    }

    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat()
                + " | " + escape(from.toString())
                + " | " + escape(to.toString());
    }
}
