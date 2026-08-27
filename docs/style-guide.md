# Java and Git style guide

This project (CS2103 iP) follows the se-education.org house conventions
below. These are graded, not just stylistic preferences — code and commits
should follow them from the start, not just at the refactoring increments.

Full source guides:

- Java (intermediate level): https://se-education.org/guides/conventions/java/intermediate.html
- Git: https://se-education.org/guides/conventions/git.html

This file is a condensed summary for quick reference; re-check the source
guides directly if a rule's exact wording matters or this summary seems out
of date. Project-specific Git policy (tag type, when to commit/push) is in
[AGENTS.md](../AGENTS.md) rather than repeated here.

## Java naming

- Packages: all lowercase (e.g. `todobuddy.ui`)
- Classes/Enums: PascalCase nouns (`Line`, `AudioSystem`)
- Variables/methods: camelCase; methods are verbs (`getName()`, `computeTotalWidth()`)
- Constants: `UPPER_SNAKE_CASE` (`MAX_ITERATIONS`)
- Booleans: prefix `is`/`has`/`was` (`isVisible`, `hasData`)
- Collections: plural names (`points`, `values`)
- No uppercase acronyms mid-name: `exportHtmlSource()` not `exportHTMLSource()`
- Test methods: `featureUnderTest_testScenario_expectedBehavior()`
- Loop indices: `i, j, k` fine for short/nested scopes only; larger-scope variables get descriptive names

## Java layout

- 4-space indent, no tabs
- Line length: soft limit <110 chars, hard limit 120
- Wrapped lines indent 8 spaces; break after commas, before operators
- K&R/Egyptian braces (opening brace on the same line)
- Always use braces for loop/if bodies, even single-statement ones; conditional body never on the same line as `if`
- One blank line between logical sections within a block
- Array brackets attach to the type: `int[] a`, not `int a[]`
- No wildcard imports; explicit imports only, ordered: static, `java.*`, `javax.*`, `org.*`, `com.*`, `javafx.*`
- Every class in a package — not yet applicable to this codebase; it still predates the A-Packages increment, so today's classes are in the default package
- Class variables never public (unless a pure data class) — use accessors
- Variables initialized at declaration, in the smallest possible scope

## Java comments/Javadoc

- English only, American spelling
- Javadoc required on public classes and nontrivial public methods (skip for getters/setters, overrides with inherited doc, tests)
- First Javadoc sentence is a short summary; `@param` for all parameters or none; blank line between the description and the tags

## Git commits

- Subject line: imperative mood, capitalized, no trailing period, soft limit 50 chars / hard limit 72
- Optional scope prefix, e.g. `Person class: Remove static imports`
- Blank line between subject and body; body wrapped at 72 chars
- Body explains WHAT and WHY, not HOW — the diff already shows how
- Branch names: kebab-case; issue branches as `issueNumber-keywords-from-title` (e.g. `1234-ui-freeze-error`)
