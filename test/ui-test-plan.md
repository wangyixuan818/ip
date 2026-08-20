# Console UI Test Plan

Record manual or automated console UI tests here. The `test-ui` skill executes cases in the order listed and stops at the first failure.

## Run information

- Run command: `javac -d out src/main/java/*.java && java -cp out Noms`
- Working directory: `<repository root or other directory>`
- Output comparison: exact, except for line-ending differences
- Shared process: no, unless a test case explicitly says otherwise

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
```
```

<!-- Copy the structure above for each additional test case. -->
