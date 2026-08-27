package noms.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link TaskList}, focusing on its non-trivial behavior:
 * {@link TaskList#tasksOn(LocalDate)} (the date filter behind the {@code on}
 * command), {@link TaskList#delete(int)} (removal and renumbering), and the
 * defensive copying done by the list-taking constructor and
 * {@link TaskList#asList()}. The simple pass-through accessors are exercised
 * incidentally by these cases.
 */
public class TaskListTest {

    private final Deadline returnBook = new Deadline("return book", LocalDate.of(2019, 12, 1));
    private final Deadline payRent = new Deadline("pay rent", LocalDate.of(2019, 12, 15));
    private final Event conference = new Event("conference",
            LocalDate.of(2019, 11, 30), LocalDate.of(2019, 12, 2));
    private final ToDo chore = new ToDo("standalone chore");

    private TaskList populatedList() {
        TaskList list = new TaskList();
        list.add(chore);
        list.add(returnBook);
        list.add(payRent);
        list.add(conference);
        return list;
    }

    // --- tasksOn ---

    @Test
    public void tasksOn_matchingDeadlineAndEvent_returnedInListOrder() {
        List<Task> matches = populatedList().tasksOn(LocalDate.of(2019, 12, 1));
        // The deadline "return book" and the spanning event "conference"
        // match; the todo and the other deadline do not.
        assertEquals(List.of(returnBook, conference), matches);
    }

    @Test
    public void tasksOn_noMatches_returnsEmptyList() {
        assertEquals(List.of(), populatedList().tasksOn(LocalDate.of(2019, 6, 15)));
    }

    @Test
    public void tasksOn_todoNeverMatches() {
        // A todo has no date, so no query date can match it.
        TaskList list = new TaskList();
        list.add(chore);
        assertEquals(List.of(), list.tasksOn(LocalDate.of(2019, 12, 1)));
    }

    // --- delete ---

    @Test
    public void delete_removesAndReturnsTask() {
        TaskList list = populatedList();
        Task removed = list.delete(1);
        assertSame(returnBook, removed);
        assertEquals(3, list.size());
    }

    @Test
    public void delete_renumbersRemainingTasks() {
        TaskList list = populatedList();
        list.delete(1);
        // After removing index 1, the tasks after it shift down by one.
        assertSame(chore, list.get(0));
        assertSame(payRent, list.get(1));
        assertSame(conference, list.get(2));
    }

    // --- add / size ---

    @Test
    public void add_increasesSizeAndAppends() {
        TaskList list = new TaskList();
        assertEquals(0, list.size());
        list.add(chore);
        assertEquals(1, list.size());
        assertSame(chore, list.get(0));
    }

    // --- defensive copying ---

    @Test
    public void constructor_copiesInputList_soLaterChangesDoNotLeakIn() {
        List<Task> source = new ArrayList<>();
        source.add(chore);
        TaskList list = new TaskList(source);

        source.add(returnBook); // must not affect the already-built TaskList
        assertEquals(1, list.size());
    }

    @Test
    public void asList_returnsCopy_soChangesDoNotLeakOut() {
        TaskList list = new TaskList();
        list.add(chore);

        List<Task> snapshot = list.asList();
        snapshot.clear(); // must not affect the TaskList
        assertEquals(1, list.size());
    }
}
