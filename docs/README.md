# Noms User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

// Product intro goes here

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Snoozing dated tasks

Use `snooze` to change the date of an existing deadline or event. The task
number is the number shown by the `list` command.

- Deadline: `snooze <task number> /by yyyy-mm-dd`
- Event with the same duration: `snooze <task number> /from yyyy-mm-dd`
- Event with new start and end dates:
  `snooze <task number> /from yyyy-mm-dd /to yyyy-mm-dd`

For example, `snooze 2 /by 2026-09-20` changes task 2's deadline to
20 September 2026:

```text
 Nom nom! Noms has snoozed this task:
   [D][ ] submit report (by: Sept 20 2026)
____________________________________________________________
```

Todos cannot be snoozed because they do not have dates. Snoozing a task keeps
its description, completion status, and position in the task list unchanged.

## Feature ABC

// Feature details


## Feature XYZ

// Feature details
