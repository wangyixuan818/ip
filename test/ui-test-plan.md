# Console UI Test Plan

The `test-ui` skill executes these cases in order and stops at the first failure.

## Run information

- Java version: Java 25 (`25.0.3.fx-zulu` on macOS)
- Compile command: `javac -d /tmp/noms-ui-test-out src/main/java/*.java`
- Run command: `java -cp /tmp/noms-ui-test-out Noms`
- Working directory: repository root
- Output comparison: exact, except for line-ending differences
- Shared process: no; every test case starts a fresh process with an empty task list
- User input is not echoed by the application
- Inputs are supplied exactly as shown, including blank lines
- Expected output includes the complete startup banner, responses, separators, and goodbye output

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
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Sunday)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```
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
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [D][ ] submit report (by: 11/10/2019 5pm)
 Now you have 1 tasks in the list.
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] submit report (by: 11/10/2019 5pm)
____________________________________________________________
 OK, I've marked this task as not done yet:
   [D][ ] submit report (by: 11/10/2019 5pm)
____________________________________________________________
 1.[D][ ] submit report (by: 11/10/2019 5pm)
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
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
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 OOPS! This todo is missing its main ingredient: a description.
Try: todo <description>
____________________________________________________________
 OOPS! This deadline recipe is incomplete.
Try: deadline <description> /by <date/time>
____________________________________________________________
 OOPS! This event recipe needs more ingredients.
Try: event <description> /from <date/time> /to <date/time>
____________________________________________________________
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```

### TC-004: Reject unknown commands

**Aim:**

Verify that an unrecognised command produces a helpful Noms-style error and does not terminate the program.

**Inputs:**

```text
blah
bye
```

**Expected output:**

```text
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 OOPS! Grrr... Noms couldn't understand that command.
Try feeding me a todo, deadline, event, list, mark, unmark, delete, or bye.
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```

### TC-005: Reject malformed deadlines

**Aim:**

Verify that deadlines without a valid `/by` section are rejected with a helpful format explanation.

**Inputs:**

```text
deadline return book
deadline return book /by
bye
```

**Expected output:**

```text
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 OOPS! This deadline recipe is incomplete.
Try: deadline <description> /by <date/time>
____________________________________________________________
 OOPS! This deadline recipe is incomplete.
Try: deadline <description> /by <date/time>
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```

### TC-006: Reject malformed events

**Aim:**

Verify that events without valid `/from` and `/to` sections are rejected with a helpful format explanation.

**Inputs:**

```text
event meeting
event meeting /from 2pm
event meeting /from /to 4pm
bye
```

**Expected output:**

```text
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 OOPS! This event recipe needs more ingredients.
Try: event <description> /from <date/time> /to <date/time>
____________________________________________________________
 OOPS! This event recipe needs more ingredients.
Try: event <description> /from <date/time> /to <date/time>
____________________________________________________________
 OOPS! This event recipe needs more ingredients.
Try: event <description> /from <date/time> /to <date/time>
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```

### TC-007: Reject invalid mark and unmark task numbers

**Aim:**

Verify that missing, non-numeric, and out-of-range task numbers are handled without terminating Noms.

**Inputs:**

```text
todo anchor task
mark
mark abc
mark 0
mark 999999999999
unmark
unmark 1.5
unmark 99
list
bye
```

**Expected output:**

```text
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [T][ ] anchor task
 Now you have 1 tasks in the list.
____________________________________________________________
 OOPS! Noms needs to know which task to mark.
Try: mark <task number>
____________________________________________________________
 OOPS! The task number must be a whole number.
Try: mark 1
____________________________________________________________
 OOPS! Task number 0 is out of range.
Choose a task number from 1 to 1.
____________________________________________________________
 OOPS! That task number is too large for Noms.
Choose a task number from 1 to 1.
____________________________________________________________
 OOPS! Noms needs to know which task to unmark.
Try: unmark <task number>
____________________________________________________________
 OOPS! The task number must be a whole number.
Try: unmark 1
____________________________________________________________
 OOPS! Task number 99 is out of range.
Choose a task number from 1 to 1.
____________________________________________________________
 1.[T][ ] anchor task
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```

### TC-008: Add more than 100 tasks

**Aim:**

Verify that the task list uses a dynamically sized Java collection and can continue accepting tasks beyond the old fixed capacity.

**Inputs:**

```text
todo task 1
todo task 2
todo task 3
todo task 4
todo task 5
todo task 6
todo task 7
todo task 8
todo task 9
todo task 10
todo task 11
todo task 12
todo task 13
todo task 14
todo task 15
todo task 16
todo task 17
todo task 18
todo task 19
todo task 20
todo task 21
todo task 22
todo task 23
todo task 24
todo task 25
todo task 26
todo task 27
todo task 28
todo task 29
todo task 30
todo task 31
todo task 32
todo task 33
todo task 34
todo task 35
todo task 36
todo task 37
todo task 38
todo task 39
todo task 40
todo task 41
todo task 42
todo task 43
todo task 44
todo task 45
todo task 46
todo task 47
todo task 48
todo task 49
todo task 50
todo task 51
todo task 52
todo task 53
todo task 54
todo task 55
todo task 56
todo task 57
todo task 58
todo task 59
todo task 60
todo task 61
todo task 62
todo task 63
todo task 64
todo task 65
todo task 66
todo task 67
todo task 68
todo task 69
todo task 70
todo task 71
todo task 72
todo task 73
todo task 74
todo task 75
todo task 76
todo task 77
todo task 78
todo task 79
todo task 80
todo task 81
todo task 82
todo task 83
todo task 84
todo task 85
todo task 86
todo task 87
todo task 88
todo task 89
todo task 90
todo task 91
todo task 92
todo task 93
todo task 94
todo task 95
todo task 96
todo task 97
todo task 98
todo task 99
todo task 100
todo task 101
list
bye
```

**Expected output:**

```text
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 1
 Now you have 1 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 2
 Now you have 2 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 3
 Now you have 3 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 4
 Now you have 4 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 5
 Now you have 5 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 6
 Now you have 6 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 7
 Now you have 7 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 8
 Now you have 8 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 9
 Now you have 9 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 10
 Now you have 10 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 11
 Now you have 11 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 12
 Now you have 12 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 13
 Now you have 13 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 14
 Now you have 14 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 15
 Now you have 15 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 16
 Now you have 16 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 17
 Now you have 17 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 18
 Now you have 18 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 19
 Now you have 19 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 20
 Now you have 20 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 21
 Now you have 21 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 22
 Now you have 22 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 23
 Now you have 23 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 24
 Now you have 24 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 25
 Now you have 25 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 26
 Now you have 26 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 27
 Now you have 27 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 28
 Now you have 28 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 29
 Now you have 29 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 30
 Now you have 30 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 31
 Now you have 31 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 32
 Now you have 32 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 33
 Now you have 33 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 34
 Now you have 34 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 35
 Now you have 35 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 36
 Now you have 36 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 37
 Now you have 37 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 38
 Now you have 38 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 39
 Now you have 39 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 40
 Now you have 40 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 41
 Now you have 41 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 42
 Now you have 42 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 43
 Now you have 43 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 44
 Now you have 44 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 45
 Now you have 45 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 46
 Now you have 46 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 47
 Now you have 47 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 48
 Now you have 48 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 49
 Now you have 49 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 50
 Now you have 50 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 51
 Now you have 51 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 52
 Now you have 52 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 53
 Now you have 53 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 54
 Now you have 54 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 55
 Now you have 55 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 56
 Now you have 56 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 57
 Now you have 57 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 58
 Now you have 58 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 59
 Now you have 59 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 60
 Now you have 60 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 61
 Now you have 61 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 62
 Now you have 62 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 63
 Now you have 63 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 64
 Now you have 64 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 65
 Now you have 65 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 66
 Now you have 66 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 67
 Now you have 67 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 68
 Now you have 68 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 69
 Now you have 69 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 70
 Now you have 70 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 71
 Now you have 71 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 72
 Now you have 72 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 73
 Now you have 73 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 74
 Now you have 74 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 75
 Now you have 75 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 76
 Now you have 76 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 77
 Now you have 77 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 78
 Now you have 78 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 79
 Now you have 79 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 80
 Now you have 80 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 81
 Now you have 81 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 82
 Now you have 82 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 83
 Now you have 83 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 84
 Now you have 84 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 85
 Now you have 85 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 86
 Now you have 86 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 87
 Now you have 87 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 88
 Now you have 88 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 89
 Now you have 89 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 90
 Now you have 90 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 91
 Now you have 91 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 92
 Now you have 92 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 93
 Now you have 93 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 94
 Now you have 94 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 95
 Now you have 95 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 96
 Now you have 96 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 97
 Now you have 97 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 98
 Now you have 98 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 99
 Now you have 99 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 100
 Now you have 100 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] task 101
 Now you have 101 tasks in the list.
____________________________________________________________
 1.[T][ ] task 1
 2.[T][ ] task 2
 3.[T][ ] task 3
 4.[T][ ] task 4
 5.[T][ ] task 5
 6.[T][ ] task 6
 7.[T][ ] task 7
 8.[T][ ] task 8
 9.[T][ ] task 9
 10.[T][ ] task 10
 11.[T][ ] task 11
 12.[T][ ] task 12
 13.[T][ ] task 13
 14.[T][ ] task 14
 15.[T][ ] task 15
 16.[T][ ] task 16
 17.[T][ ] task 17
 18.[T][ ] task 18
 19.[T][ ] task 19
 20.[T][ ] task 20
 21.[T][ ] task 21
 22.[T][ ] task 22
 23.[T][ ] task 23
 24.[T][ ] task 24
 25.[T][ ] task 25
 26.[T][ ] task 26
 27.[T][ ] task 27
 28.[T][ ] task 28
 29.[T][ ] task 29
 30.[T][ ] task 30
 31.[T][ ] task 31
 32.[T][ ] task 32
 33.[T][ ] task 33
 34.[T][ ] task 34
 35.[T][ ] task 35
 36.[T][ ] task 36
 37.[T][ ] task 37
 38.[T][ ] task 38
 39.[T][ ] task 39
 40.[T][ ] task 40
 41.[T][ ] task 41
 42.[T][ ] task 42
 43.[T][ ] task 43
 44.[T][ ] task 44
 45.[T][ ] task 45
 46.[T][ ] task 46
 47.[T][ ] task 47
 48.[T][ ] task 48
 49.[T][ ] task 49
 50.[T][ ] task 50
 51.[T][ ] task 51
 52.[T][ ] task 52
 53.[T][ ] task 53
 54.[T][ ] task 54
 55.[T][ ] task 55
 56.[T][ ] task 56
 57.[T][ ] task 57
 58.[T][ ] task 58
 59.[T][ ] task 59
 60.[T][ ] task 60
 61.[T][ ] task 61
 62.[T][ ] task 62
 63.[T][ ] task 63
 64.[T][ ] task 64
 65.[T][ ] task 65
 66.[T][ ] task 66
 67.[T][ ] task 67
 68.[T][ ] task 68
 69.[T][ ] task 69
 70.[T][ ] task 70
 71.[T][ ] task 71
 72.[T][ ] task 72
 73.[T][ ] task 73
 74.[T][ ] task 74
 75.[T][ ] task 75
 76.[T][ ] task 76
 77.[T][ ] task 77
 78.[T][ ] task 78
 79.[T][ ] task 79
 80.[T][ ] task 80
 81.[T][ ] task 81
 82.[T][ ] task 82
 83.[T][ ] task 83
 84.[T][ ] task 84
 85.[T][ ] task 85
 86.[T][ ] task 86
 87.[T][ ] task 87
 88.[T][ ] task 88
 89.[T][ ] task 89
 90.[T][ ] task 90
 91.[T][ ] task 91
 92.[T][ ] task 92
 93.[T][ ] task 93
 94.[T][ ] task 94
 95.[T][ ] task 95
 96.[T][ ] task 96
 97.[T][ ] task 97
 98.[T][ ] task 98
 99.[T][ ] task 99
 100.[T][ ] task 100
 101.[T][ ] task 101
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```

### TC-009: Delete a task and renumber the list

**Aim:**

Verify that deleting a task removes it, displays the deleted task, and renumbers the remaining tasks.

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
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: June 6th)
 Now you have 2 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
 Noted. Noms has taken this task off the menu:
   [D][ ] return book (by: June 6th)
 Now you have 2 tasks in the list.
____________________________________________________________
 1.[T][ ] read book
 2.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```

### TC-010: Reject invalid delete task numbers

**Aim:**

Verify that malformed or out-of-range delete commands do not terminate Noms or change the task list.

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
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [T][ ] buy milk
 Now you have 1 tasks in the list.
____________________________________________________________
 OOPS! Noms needs to know which task to delete.
Try: delete <task number>
____________________________________________________________
 OOPS! The task number must be a whole number.
Try: delete 1
____________________________________________________________
 OOPS! Task number 0 is out of range.
Choose a task number from 1 to 1.
____________________________________________________________
 OOPS! Task number 2 is out of range.
Choose a task number from 1 to 1.
____________________________________________________________
 1.[T][ ] buy milk
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```

### TC-011: Reject an empty command

**Aim:**

Verify that an empty input is rejected with a specific message and that Noms continues accepting commands afterwards.

**Inputs:**

```text

list
bye
```

**Expected output:**

```text
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 OOPS! Noms needs a command. Try feeding me a todo, deadline, event, list, mark, unmark, delete, or bye.
____________________________________________________________
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```
### TC-012: Preserve task numbering after malformed additions

**Aim:**

Verify that malformed deadline and event commands interleaved with valid additions do not create hidden tasks or disturb task numbering.

**Inputs:**

```text
todo buy groceries
deadline submit report
 deadline ignored
deadline submit report /by Friday
event study session /from 2pm
event study session /from 2pm /to 4pm
list
bye
```

**Expected output:**

```text
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [T][ ] buy groceries
 Now you have 1 tasks in the list.
____________________________________________________________
 OOPS! This deadline recipe is incomplete.
Try: deadline <description> /by <date/time>
____________________________________________________________
 OOPS! This deadline recipe is incomplete.
Try: deadline <description> /by <date/time>
____________________________________________________________
 Got it. I've added this task:
   [D][ ] submit report (by: Friday)
 Now you have 2 tasks in the list.
____________________________________________________________
 OOPS! This event recipe needs more ingredients.
Try: event <description> /from <date/time> /to <date/time>
____________________________________________________________
 Got it. I've added this task:
   [E][ ] study session (from: 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
 1.[T][ ] buy groceries
 2.[D][ ] submit report (by: Friday)
 3.[E][ ] study session (from: 2pm to: 4pm)
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```

### TC-013: Preserve completion state after invalid mark operations

**Aim:**

Verify that invalid mark and unmark commands interleaved with valid state changes do not modify another task or corrupt completion state.

**Inputs:**

```text
todo wash dishes
deadline pay bills /by Monday
mark 1
mark 3
unmark abc
unmark 1
mark 2
list
bye
```

**Expected output:**

```text
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [T][ ] wash dishes
 Now you have 1 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [D][ ] pay bills (by: Monday)
 Now you have 2 tasks in the list.
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] wash dishes
____________________________________________________________
 OOPS! Task number 3 is out of range.
Choose a task number from 1 to 2.
____________________________________________________________
 OOPS! The task number must be a whole number.
Try: unmark 1
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] wash dishes
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] pay bills (by: Monday)
____________________________________________________________
 1.[T][ ] wash dishes
 2.[D][X] pay bills (by: Monday)
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```

### TC-014: Preserve deletion state after invalid delete operations

**Aim:**

Verify that invalid deletions interleaved with a valid deletion and a later addition preserve the correct tasks and contiguous numbering.

**Inputs:**

```text
todo alpha
todo beta
todo gamma
delete abc
delete 2
delete 3
todo delta
list
bye
```

**Expected output:**

```text
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [T][ ] alpha
 Now you have 1 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] beta
 Now you have 2 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] gamma
 Now you have 3 tasks in the list.
____________________________________________________________
 OOPS! The task number must be a whole number.
Try: delete 1
____________________________________________________________
 Noted. Noms has taken this task off the menu:
   [T][ ] beta
 Now you have 2 tasks in the list.
____________________________________________________________
 OOPS! Task number 3 is out of range.
Choose a task number from 1 to 2.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] delta
 Now you have 3 tasks in the list.
____________________________________________________________
 1.[T][ ] alpha
 2.[T][ ] gamma
 3.[T][ ] delta
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```

### TC-015: Continue correctly after empty and unknown commands

**Aim:**

Verify that empty and unknown commands interleaved with valid additions do not terminate Noms or change the task list.

**Inputs:**

```text
todo first

blah
deadline second /by tomorrow
list
bye
```

**Expected output:**

```text
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [T][ ] first
 Now you have 1 tasks in the list.
____________________________________________________________
 OOPS! Noms needs a command. Try feeding me a todo, deadline, event, list, mark, unmark, delete, or bye.
____________________________________________________________
 OOPS! Grrr... Noms couldn't understand that command.
Try feeding me a todo, deadline, event, list, mark, unmark, delete, or bye.
____________________________________________________________
 Got it. I've added this task:
   [D][ ] second (by: tomorrow)
 Now you have 2 tasks in the list.
____________________________________________________________
 1.[T][ ] first
 2.[D][ ] second (by: tomorrow)
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```

### TC-016: Reject extra task-number arguments without changing state

**Aim:**

Verify that mark, unmark, and delete commands with extra arguments are rejected while valid neighbouring commands still update the intended task.

**Inputs:**

```text
todo one
todo two
mark 1 2
mark 1
delete 2 extra
unmark 1
delete 2
list
bye
```

**Expected output:**

```text
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [T][ ] one
 Now you have 1 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] two
 Now you have 2 tasks in the list.
____________________________________________________________
 OOPS! Noms can only mark one task at a time.
Try: mark <task number>
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] one
____________________________________________________________
 OOPS! Noms can only delete one task at a time.
Try: delete <task number>
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] one
____________________________________________________________
 Noted. Noms has taken this task off the menu:
   [T][ ] two
 Now you have 1 tasks in the list.
____________________________________________________________
 1.[T][ ] one
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```

### TC-017: Recover from invalid operations on an empty list

**Aim:**

Verify that invalid task operations on an empty list do not prevent a later valid task from being added, marked, and listed.

**Inputs:**

```text
mark 1
delete 1
unmark 1
todo recovered task
mark 1
list
bye
```

**Expected output:**

```text
____________________________________________________________
 _   _  ___  __  __  ____
| \ | |/ _ \|  \/  |/ ___|
|  \| | | | | |\/| | \___ \
| |\  | |_| | |  | |  ___) |
|_| \_|\___/|_|  |_| |____/
____________________________________________________________
Hello! I'm Noms.
NomNom, have you eaten? What can I do for you?
____________________________________________________________
 OOPS! Noms has no tasks to mark yet.
Add a task first, then try again.
____________________________________________________________
 OOPS! Noms has no tasks to delete yet.
Add a task first, then try again.
____________________________________________________________
 OOPS! Noms has no tasks to unmark yet.
Add a task first, then try again.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] recovered task
 Now you have 1 tasks in the list.
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] recovered task
____________________________________________________________
 1.[T][X] recovered task
____________________________________________________________
Bye~ Hope to see you again soon!
____________________________________________________________
```
