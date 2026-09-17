# Noms User Guide

Noms is a hungry little task monster that helps you record, find, schedule,
complete, and remove tasks. Your tasks are saved automatically, so they will
still be on the menu the next time you open Noms.

![The Noms chatbot showing a conversation and a list of tasks](Ui.png)

## Quick start

1. Launch Noms.
2. Type a command in the text box at the bottom of the window.
3. Press <kbd>Enter</kbd> or select **Send**.
4. Type `bye` when you are finished.

> [!IMPORTANT]
> Enter dates as `yyyy-mm-dd`, including leading zeroes. For example, enter
> 6 September 2026 as `2026-09-06`.

## Command summary

| Action | Command format |
| --- | --- |
| Add a todo | `todo <description>` |
| Add a deadline | `deadline <description> /by yyyy-mm-dd` |
| Add an event | `event <description> /from yyyy-mm-dd /to yyyy-mm-dd` |
| Show every task | `list` |
| Mark a task as done | `mark <task number>` |
| Mark a task as not done | `unmark <task number>` |
| Delete a task | `delete <task number>` |
| Find tasks by description | `find <keyword>` |
| Show tasks on a date | `on yyyy-mm-dd` |
| Change a deadline date | `snooze <task number> /by yyyy-mm-dd` |
| Change an event's start date | `snooze <task number> /from yyyy-mm-dd` |
| Change both event dates | `snooze <task number> /from yyyy-mm-dd /to yyyy-mm-dd` |
| Exit Noms | `bye` |

Words in angle brackets, such as `<description>`, are placeholders. Replace
them with your own value and do not type the angle brackets.

## Adding tasks

Noms supports three kinds of tasks:

- **Todo** (`[T]`): a task without a date.
- **Deadline** (`[D]`): a task that must be completed by one date.
- **Event** (`[E]`): a task that takes place over a range of dates.

### Adding a todo

Use `todo <description>` for a task without a date.

```text
todo borrow a library book
```

### Adding a deadline

Use `deadline <description> /by yyyy-mm-dd` for a task with a due date.

```text
deadline submit report /by 2026-09-20
```

### Adding an event

Use `event <description> /from yyyy-mm-dd /to yyyy-mm-dd` for an activity
with a start and end date.

```text
event project retreat /from 2026-10-03 /to 2026-10-05
```

New tasks are added to the end of the task list and saved automatically.

## Viewing tasks

### Listing every task

Use `list` to display all tasks and their task numbers.

```text
list
```

The symbols beside each task show its type and status. `[ ]` means the task is
not done, while `[X]` means it is done. For example:

```text
1.[T][ ] borrow a library book
2.[D][X] submit report (by: Sep 20 2026)
```

> [!TIP]
> Run `list` before using a command that needs a task number. Deleting a task
> renumbers the tasks after it.

### Viewing tasks on a date

Use `on yyyy-mm-dd` to show deadlines due on that date and events taking place
on that date. An event is included on its start date, end date, and every date
in between. Todos are not included because they have no date.

```text
on 2026-10-04
```

## Finding tasks

Use `find <keyword>` to search task descriptions. The search is
case-insensitive, accepts more than one word, and includes partial matches.

```text
find project
find SUBMIT REPORT
```

The numbers in search results number the matches themselves. Use `list` to
check a task's actual task number before marking, deleting, or snoozing it.

## Updating task status

Use the task number shown by `list` to mark a task as done:

```text
mark 2
```

The task's status changes from `[ ]` to `[X]`. To put it back on the menu,
use:

```text
unmark 2
```

These commands work with todos, deadlines, and events.

## Snoozing dated tasks

Use `snooze` to reschedule a deadline or event. Snoozing keeps the task's
description, completion status, and position in the list unchanged.

To replace a deadline's due date:

```text
snooze 2 /by 2026-09-27
```

To move an event to a new start date while keeping its original duration:

```text
snooze 3 /from 2026-10-10
```

To replace both an event's start and end dates:

```text
snooze 3 /from 2026-10-10 /to 2026-10-12
```

Todos cannot be snoozed because they do not have dates.

## Deleting tasks

Use `delete <task number>` to remove a task permanently.

```text
delete 1
```

Noms shows the removed task and the number of tasks remaining. The remaining
tasks are then renumbered.

## Exiting Noms

Use `bye` to close Noms safely.

```text
bye
```

All changes to the task list are saved as they are made. Noms loads the saved
list automatically the next time it starts.

## Troubleshooting

- If Noms rejects a date, check that it is a real calendar date in
  `yyyy-mm-dd` format, such as `2026-02-28`.
- If Noms reports that a task number is out of range, run `list` and use a
  number currently shown there.
- If a command is not understood, compare it with the formats in the
  [command summary](#command-summary). Keep `/by`, `/from`, and `/to` in the
  positions shown.
