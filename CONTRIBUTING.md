# Contributing to Growing Deep

Thank you for your interest in contributing to **Growing Deep**! We are committed to building an editorial-grade, trustworthy, and distraction-free Bible study and small group discipleship platform.

---

## 1. Doctrinal & Hermeneutical Principles

Contributors to theological content, lesson curricula, or AI assistant prompts must abide by our core guiding principles:

- **Scripture First**: Explicit propositions of scripture take precedence over human philosophical systems.
- **Historic Ecumenical Orthodoxy**: All content must align with the historic consensus of the Apostles', Nicene-Constantinopolitan, and Chalcedonian creeds.
- **Charity in Differences**: On secondary matters (soteriology, ecclesiology, baptism, eschatology), do not bias the application toward any single denomination. Maintain our three-tier labeling framework:
  1. *Scripture Says* (objective textual affirmation)
  2. *Theological Interpretation* (historic orthodox consensus)
  3. *Debated Among Traditions* (balanced, charitable presentation of distinct views)
- **Zero-Tolerance for Hallucination**: AI assistant additions or prompt changes must preserve local passage grounding and canonical validation.

---

## 2. Codebase Standards

- **Language & UI**: 100% Kotlin with **Jetpack Compose** and **Material Design 3 (M3)**.
- **Architecture**: Clean Architecture / MVVM. UI state flows must use Kotlin `StateFlow` and Compose `collectAsStateWithLifecycle()`.
- **Database**: Local persistence is powered by **Room** (`GrowingDeepDatabase`). Migrations must include full SQL schemas.
- **Test Tags**: Any interactive or navigation Composable must include a `Modifier.testTag("snake_case_id")` for automated verification.
- **Offline First**: All core reader and word study features must function fully without network connectivity.

---

## 3. Development Workflow

1. **Clone & Setup**:
   ```bash
   git clone https://github.com/your-org/growing-deep.git
   cd growing-deep
   ```
2. **Run Compilation Check**:
   ```bash
   gradle :app:compileDebugKotlin
   ```
3. **Run Unit & Smoke Tests**:
   ```bash
   gradle :app:testDebugUnitTest
   ```
4. **Submitting Changes**:
   - Create a feature branch: `git checkout -b feature/your-feature-name`.
   - Ensure all Robolectric smoke tests pass before opening a Pull Request.
   - Write clear, concise PR descriptions explaining the pastoral or functional outcome.
