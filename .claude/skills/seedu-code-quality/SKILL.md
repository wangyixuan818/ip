---
name: seedu-code-quality
description: The project's code-quality conventions, derived from the CS2103 "Code Quality" topics. Currently covers naming (nouns vs. verbs, self-explaining names, spelling, word order). Apply whenever writing, reviewing, or refactoring code so that names read clearly. More quality areas (e.g. avoiding magic numbers, error handling, comments) will be added here over time.
---

# se-education code-quality conventions

This is the authoritative code-quality standard for this project. It is derived
from the CS2103 "Code Quality" teaching (see the week 4 topic
https://nus-cs2103-ay2627-s1.github.io/website/schedule/week4/topics.html and the
se-education guide https://se-education.org/guides/conventions/), and is graded,
so apply it while writing code rather than only at the refactoring increments.

This skill is deliberately scoped: **right now it covers naming only.** Other
code-quality areas will be added as further sections over time. Until then, for
anything not about naming, fall back to the
[seedu-java-coding-standard](../seedu-java-coding-standard/SKILL.md) skill and the
source guides above.

This complements the Java coding standard: `seedu-java-coding-standard` fixes the
*mechanical* naming form (PascalCase classes, camelCase methods, UPPER_SNAKE
constants). This skill is about whether a name, once correctly cased, actually
*communicates* — the judgement rules below.

## How to use this skill

1. Before writing or editing code, skim the rules below.
2. After editing, self-check every new or renamed identifier against the
   **Review checklist** at the end. Fix any violation before considering the
   work done.
3. Git and general Java layout conventions are **not** part of this skill; they
   live in the [seedu-git-standard](../seedu-git-standard/SKILL.md) and
   [seedu-java-coding-standard](../seedu-java-coding-standard/SKILL.md) skills.

---

## 1. Naming

### Core principle

**Classes and variables are nouns; methods and functions are verbs.**
As Robert C. Martin puts it: "Functions are the verbs of that language, and
classes are the nouns."

- Class / variable: `Task`, `TaskList`, `Person student` — a thing.
- Method: `parseTask()`, `getDescription()`, `removeWhiteSpace()` — an action.
- A method that answers yes/no reads as a question: `isValidInput()`,
  `hasNextCommand()`, `isExit()`.

### 1.1 Distinguish single- vs. multi-valued names

A name should reveal whether it holds one value or many.

```java
Person student;                 // one
ArrayList<Person> students;     // many — plural
```

### 1.2 Use standard, correct spelling

Avoid texting-style spelling, slang, foreign-language words, and
context-specific in-jokes or dated references. Prefer the plain, correctly
spelled word.

```
DaUser.png      ->  User.png        // "Da" is texting slang for "The"
btnSnd          ->  sendButton
```

### 1.3 Names should explain

A name must describe the entity accurately and with enough detail that a reader
does not need the implementation to understand it. Avoid vague fillers like
`temp`, `flag`, `data`, `process`, `handle`, `value` when a specific name is
possible.

```
processInput()  ->  removeWhiteSpaceFromInput()
flag            ->  isValidInput
temp            ->  originalDescription   // name it for what it holds
```

### 1.4 Use a sensible word order

Order the words the way they read naturally in English.

```
bySizeOrder()   ->  orderBySize()
listTaskAll()   ->  listAllTasks()
```

### 1.5 Avoid number- and case-only distinctions

Do not tell related names apart only by a trailing digit, or by capitalization
alone. Name each for its actual role instead.

```
value1, value2  ->  originalValue, finalValue
value / Value   ->  rawValue / parsedValue
```

### 1.6 Balance name length

Avoid both cryptic abbreviation and needless length. Match the length to the
scope: a loop index may be `i`, but a field visible across a class earns a full,
descriptive name. When you do abbreviate, use one consistent abbreviation with a
clear meaning throughout.

### 1.7 Avoid misleading or hard-to-distinguish names

- Related items should share a naming pattern; unrelated items should look
  clearly different.
- Avoid names that differ only by easily confused characters (`0` vs `O`,
  `l` vs `I` vs `1`).
- Keep a pattern consistent across a set: prefer `hexForBlack`, `hexForWhite`,
  `hexForRed` over mixing `colorBlack` with `hexForRed`.

---

## Review checklist (naming)

Before considering a change done, confirm every new or renamed identifier:

- [ ] Classes/variables are nouns; methods are verbs; boolean methods read as
      yes/no questions (`is`/`has`/`can`/`should`).
- [ ] Collections are plural; single values are singular (§1.1).
- [ ] Spelling is standard — no slang, texting shorthand, or in-jokes (§1.2).
- [ ] No vague fillers (`temp`, `flag`, `data`, `process`) where a specific name
      fits; the name explains the entity (§1.3).
- [ ] Words are in natural reading order (§1.4).
- [ ] Related names are not distinguished only by a digit or by case (§1.5).
- [ ] Length suits the scope; abbreviations are consistent (§1.6).
- [ ] Names are not misleading and avoid easily confused characters (§1.7).

---

## Future sections

Additional CS2103 code-quality topics will be added here as they are covered,
e.g. avoiding magic numbers, SLAP / one level of abstraction, error handling,
and comments. Until a topic appears above, treat it as out of scope for this
skill and follow the source guides.
