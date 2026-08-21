# Console UI Test Plan

Record manual or automated console UI tests here. The `test-ui` skill executes cases in the order listed and stops at the first failure.

## Run information

- Run command: `javac -d out src/main/java/*.java && java -cp out Noms`
- Working directory: `<repository root or other directory>`
- Output comparison: exact, except for line-ending differences
- Shared process: no, unless a test case explicitly says otherwise
- User input is not echoed by the application; test output contains Noms' responses only.

## Test cases

### TC-001: Add and list all task types

**Aim:**

Verify that todos, deadlines, and events are parsed into the correct subclasses and displayed with their type and date/time details.

**Inputs:**

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

**Expected output:**

```text
The output must include:

- `[T][ ] borrow book`
- `[D][ ] return book (by: Sunday)`
- `[E][ ] project meeting (from: Mon 2pm to: 4pm)`
- all three entries in the `list` output
- the normal goodbye message

The complete output, including prompts and separators, is compared exactly by the `test-ui` skill.

### TC-002: Mark and unmark a typed task

**Aim:**

Verify that marking and unmarking preserves the task type and deadline details.

**Inputs:**

```text
deadline submit report /by 11/10/2019 5pm
mark 1
unmark 1
list
bye
```

**Expected output:**

```text
The deadline is shown as `[D][X] submit report (by: 11/10/2019 5pm)` after marking and
`[D][ ] submit report (by: 11/10/2019 5pm)` after unmarking.
```

### TC-003: Reject malformed typed commands

**Aim:**

Verify that missing descriptions or date/time sections are rejected without adding tasks.

**Inputs:**

```text
todo
deadline return book
event meeting /from 2pm
list
bye
```

**Expected output:**

```text
Each malformed command is rejected, and `list` shows no tasks.
For the empty-description cases, the error messages include:

- `OOPS! This todo is missing its main ingredient: a description.`
- `OOPS! This deadline is missing its main ingredient: a description.`
- `OOPS! This event is missing its main ingredient: a description.`
- Each message also includes the corresponding valid command format.
```
```

### TC-004: Reject unknown commands

**Aim:**

Verify that an unrecognised command produces a helpful Noms-style error and
does not terminate the program.

**Inputs:**

```text
blah
bye
```

**Expected output:**

```text
The `blah` command produces:

`OOPS! Grrr... Noms couldn't understand that command.`

followed by a list of valid commands. Noms then accepts `bye` normally.
```

### TC-005: Reject malformed deadlines

**Aim:**

Verify that deadlines without a valid `/by` section are rejected with a helpful
format explanation.

**Inputs:**

```text
deadline return book
deadline return book /by
bye
```

**Expected output:**

```text
Each malformed deadline produces:

`OOPS! This deadline recipe is incomplete.`
`Try: deadline <description> /by <date/time>`

Noms then accepts `bye` normally.
```

### TC-006: Reject malformed events

**Aim:**

Verify that events without valid `/from` and `/to` sections are rejected with a
helpful format explanation.

**Inputs:**

```text
event meeting
event meeting /from 2pm
event meeting /from /to 4pm
bye
```

**Expected output:**

```text
Each malformed event produces:

`OOPS! This event recipe needs more ingredients.`
`Try: event <description> /from <date/time> /to <date/time>`

Noms then accepts `bye` normally.
```

### TC-007: Reject invalid mark and unmark task numbers

**Aim:**

Verify that missing, non-numeric, and out-of-range task numbers are handled
without terminating Noms.

**Inputs:**

```text
mark
mark abc
mark 0
unmark
unmark xyz
unmark 99
bye
```

**Expected output:**

```text
Each invalid command produces a helpful Noms-style error and Noms then accepts
the next command normally.
```

### TC-008: Add more than 100 tasks

**Aim:**

Verify that the task list uses a dynamically sized Java collection and can
continue accepting tasks beyond the old fixed capacity.

**Inputs:**

```text
Add 100 valid todo tasks, then enter:
todo task 101
list
bye
```

**Expected output:**

```text
The `list` output includes `task 101`, and Noms then accepts `bye` normally.
```

### TC-009: Delete a task and renumber the list

**Aim:**

Verify that deleting a task removes it, displays the deleted task, and
renumbers the remaining tasks.

**Inputs:**

```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
delete 2
list
bye
```

**Expected output:**

```text
The deletion confirmation includes:

`Noted. Noms has taken this task off the menu:`
`[D][ ] return book (by: June 6th)`
`Now you have 2 tasks in the list.`

The subsequent list contains `read book` as task 1 and `project meeting` as
task 2. The deleted deadline does not appear.
```

### TC-010: Reject invalid delete task numbers

**Aim:**

Verify that malformed or out-of-range delete commands do not terminate Noms or
change the task list.

**Inputs:**

```text
todo buy milk
delete
delete abc
delete 0
delete 2
list
bye
```

**Expected output:**

```text
Each invalid delete command produces a helpful Noms-style error. The task list
still contains `buy milk` as task 1.
```

### TC-011: Reject an empty command

**Aim:**

Verify that an empty input is rejected with a specific message and that Noms
continues accepting commands afterwards.

**Inputs:**

```text

list
bye
```

**Expected output:**

```text
`OOPS! Noms needs a command. Try feeding me a todo, deadline, event, list, mark, unmark, delete, or bye.`

The empty command does not add a task, `list` shows an empty task list, and
Noms then accepts `bye` normally.
```

<!-- Copy the structure above for each additional test case. -->
