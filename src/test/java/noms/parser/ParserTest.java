package noms.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import noms.command.AddCommand;
import noms.command.Command;
import noms.command.CommandType;
import noms.command.DeleteCommand;
import noms.command.ExitCommand;
import noms.command.ListCommand;
import noms.command.MarkCommand;
import noms.command.OnCommand;
import noms.command.UnmarkCommand;
import noms.exception.EmptyCommandException;
import noms.exception.EmptyDescriptionException;
import noms.exception.InvalidDateException;
import noms.exception.InvalidDeadlineException;
import noms.exception.InvalidEventException;
import noms.exception.InvalidTaskNumberException;
import noms.exception.NomsException;
import noms.exception.UnknownCommandException;
import noms.task.Deadline;
import noms.task.Event;
import noms.task.Task;
import noms.task.ToDo;

/**
 * Tests {@link Parser}, the class that interprets raw command lines. These
 * methods carry the bulk of Noms' input-handling logic, so they are the
 * highest-value target for testing: a bug here silently mishandles user
 * commands. The cases cover command recognition, task parsing, task-number
 * validation, and date extraction, along with the top-level dispatch.
 */
public class ParserTest {

    // --- getCommandType ---

    @Test
    public void getCommandType_knownCommandAnyCase_returnsType() throws EmptyCommandException {
        assertEquals(CommandType.LIST, Parser.getCommandType("list"));
        assertEquals(CommandType.TODO, Parser.getCommandType("TODO read book"));
        assertEquals(CommandType.BYE, Parser.getCommandType("  bye  "));
    }

    @Test
    public void getCommandType_unknownCommand_returnsNull() throws EmptyCommandException {
        assertNull(Parser.getCommandType("blah do something"));
    }

    @Test
    public void getCommandType_blankInput_throwsEmptyCommandException() {
        assertThrows(EmptyCommandException.class, () -> Parser.getCommandType(""));
        assertThrows(EmptyCommandException.class, () -> Parser.getCommandType("    "));
    }

    // --- parseTask: valid input ---

    @Test
    public void parseTask_todo_returnsToDoWithDescription() throws NomsException {
        Task task = Parser.parseTask("todo read book");
        assertInstanceOf(ToDo.class, task);
        assertEquals("read book", task.getDescription());
    }

    @Test
    public void parseTask_deadline_returnsDeadlineWithDate() throws NomsException {
        Task task = Parser.parseTask("deadline return book /by 2019-06-06");
        assertInstanceOf(Deadline.class, task);
        assertEquals("return book", task.getDescription());
        assertEquals("[D][ ] return book (by: Jun 06 2019)", task.toString());
    }

    @Test
    public void parseTask_event_returnsEventWithRange() throws NomsException {
        Task task = Parser.parseTask("event project meeting /from 2019-08-06 /to 2019-08-07");
        assertInstanceOf(Event.class, task);
        assertEquals("project meeting", task.getDescription());
        assertEquals("[E][ ] project meeting (from: Aug 06 2019 to: Aug 07 2019)", task.toString());
    }

    // --- parseTask: invalid input ---

    @Test
    public void parseTask_todoWithoutDescription_throwsEmptyDescriptionException() {
        assertThrows(EmptyDescriptionException.class, () -> Parser.parseTask("todo"));
        assertThrows(EmptyDescriptionException.class, () -> Parser.parseTask("todo    "));
    }

    @Test
    public void parseTask_deadlineWordOnly_throwsEmptyDescriptionException() {
        assertThrows(EmptyDescriptionException.class, () -> Parser.parseTask("deadline"));
    }

    @Test
    public void parseTask_deadlineWithoutBySection_throwsInvalidDeadlineException() {
        assertThrows(InvalidDeadlineException.class,
                () -> Parser.parseTask("deadline return book"));
    }

    @Test
    public void parseTask_deadlineWithEmptyDate_throwsInvalidDeadlineException() {
        assertThrows(InvalidDeadlineException.class,
                () -> Parser.parseTask("deadline return book /by "));
    }

    @Test
    public void parseTask_deadlineWithUnparseableDate_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class,
                () -> Parser.parseTask("deadline return book /by Sunday"));
    }

    @Test
    public void parseTask_eventWordOnly_throwsEmptyDescriptionException() {
        assertThrows(EmptyDescriptionException.class, () -> Parser.parseTask("event"));
    }

    @Test
    public void parseTask_eventMissingToSection_throwsInvalidEventException() {
        assertThrows(InvalidEventException.class,
                () -> Parser.parseTask("event meeting /from 2019-08-06"));
    }

    @Test
    public void parseTask_eventWithUnparseableDate_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class,
                () -> Parser.parseTask("event meeting /from 2019-08-06 /to nextweek"));
    }

    // --- parseTaskNumber ---

    @Test
    public void parseTaskNumber_validInRange_returnsNumber() throws InvalidTaskNumberException {
        assertEquals(2, Parser.parseTaskNumber("mark 2", "mark", 3));
    }

    @Test
    public void parseTaskNumber_emptyList_throwsInvalidTaskNumberException() {
        assertThrows(InvalidTaskNumberException.class,
                () -> Parser.parseTaskNumber("mark 1", "mark", 0));
    }

    @Test
    public void parseTaskNumber_missingNumber_throwsInvalidTaskNumberException() {
        assertThrows(InvalidTaskNumberException.class,
                () -> Parser.parseTaskNumber("mark", "mark", 1));
    }

    @Test
    public void parseTaskNumber_extraArguments_throwsInvalidTaskNumberException() {
        assertThrows(InvalidTaskNumberException.class,
                () -> Parser.parseTaskNumber("mark 1 2", "mark", 2));
    }

    @Test
    public void parseTaskNumber_nonNumeric_throwsInvalidTaskNumberException() {
        assertThrows(InvalidTaskNumberException.class,
                () -> Parser.parseTaskNumber("mark abc", "mark", 1));
    }

    @Test
    public void parseTaskNumber_belowRange_throwsInvalidTaskNumberException() {
        assertThrows(InvalidTaskNumberException.class,
                () -> Parser.parseTaskNumber("mark 0", "mark", 1));
    }

    @Test
    public void parseTaskNumber_aboveRange_throwsInvalidTaskNumberException() {
        assertThrows(InvalidTaskNumberException.class,
                () -> Parser.parseTaskNumber("mark 99", "mark", 1));
    }

    @Test
    public void parseTaskNumber_overflowsInt_throwsInvalidTaskNumberException() {
        // Digits that exceed Integer.MAX_VALUE trip NumberFormatException,
        // which the method reports as "too large" rather than crashing.
        assertThrows(InvalidTaskNumberException.class,
                () -> Parser.parseTaskNumber("mark 999999999999", "mark", 1));
    }

    // --- parseOnDate ---

    @Test
    public void parseOnDate_validDate_returnsDate() throws NomsException {
        assertEquals(LocalDate.of(2019, 12, 1), Parser.parseOnDate("on 2019-12-01"));
    }

    @Test
    public void parseOnDate_missingDate_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class, () -> Parser.parseOnDate("on"));
    }

    @Test
    public void parseOnDate_unparseableDate_throwsInvalidDateException() {
        assertThrows(InvalidDateException.class, () -> Parser.parseOnDate("on Sunday"));
    }

    // --- parse: top-level dispatch to the right Command ---

    @Test
    public void parse_recognisedCommands_returnMatchingCommandType() throws NomsException {
        assertInstanceOf(ExitCommand.class, Parser.parse("bye"));
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 1"));
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 1"));
        assertInstanceOf(OnCommand.class, Parser.parse("on 2019-12-01"));
        assertInstanceOf(AddCommand.class, Parser.parse("todo read book"));
    }

    @Test
    public void parse_unknownCommand_throwsUnknownCommandException() {
        assertThrows(UnknownCommandException.class, () -> Parser.parse("blah"));
    }

    @Test
    public void parse_blankCommand_throwsEmptyCommandException() {
        assertThrows(EmptyCommandException.class, () -> Parser.parse("   "));
    }

    @Test
    public void parse_returnsCommandType() throws NomsException {
        // Sanity check that parse yields the Command supertype callers rely on.
        Command command = Parser.parse("list");
        assertInstanceOf(Command.class, command);
    }
}
