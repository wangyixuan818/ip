# Java and Git style guide

This project (CS2103 iP) follows the se-education.org house conventions. These
are graded, not just stylistic preferences — code and commits should follow them
from the start, not just at the refactoring increments.

## Java coding standard

The authoritative, detailed Java ruleset lives in the project-specific
**`seedu-java-coding-standard`** skill
([.claude/skills/seedu-java-coding-standard/SKILL.md](../.claude/skills/seedu-java-coding-standard/SKILL.md)),
derived from the se-education intermediate Java guide
(https://se-education.org/guides/conventions/java/intermediate.html). Follow that
skill for all Java code in `src/main/java` and `src/test/java`. It is the single
source of truth for naming, layout, imports, comments/Javadoc, statements, and
class design, so those rules are intentionally not duplicated here.

## Git commits

The Java skill deliberately does not cover Git; those conventions
(https://se-education.org/guides/conventions/git.html) are summarized here. The
project-specific Git policy — tag type, and when to commit or push — is in
[AGENTS.md](../AGENTS.md).

- Subject line: imperative mood, capitalized, no trailing period, soft limit 50 chars / hard limit 72
- Optional scope prefix, e.g. `Person class: Remove static imports`
- Blank line between subject and body; body wrapped at 72 chars
- Body explains WHAT and WHY, not HOW — the diff already shows how
- Branch names: kebab-case; issue branches as `issueNumber-keywords-from-title` (e.g. `1234-ui-freeze-error`)
