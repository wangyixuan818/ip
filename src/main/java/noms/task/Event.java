package noms.task;

import java.time.LocalDate;

import noms.util.DateUtil;

/** Represents a task that takes place between two stated dates. */
public class Event extends Task {
    private final LocalDate startDate;
    private final LocalDate endDate;

    /**
     * Creates an event with the given description and start and end dates.
     *
     * @param description the text describing the task
     * @param startDate the date the event starts
     * @param endDate the date the event ends
     */
    public Event(String description, LocalDate startDate, LocalDate endDate) {
        super(description);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /** Returns true if this event spans (inclusively) the given date. */
    public boolean occursOn(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + DateUtil.format(startDate)
                + " to: " + DateUtil.format(endDate) + ")";
    }

    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat()
                + " | " + escape(startDate.toString())
                + " | " + escape(endDate.toString());
    }
}
