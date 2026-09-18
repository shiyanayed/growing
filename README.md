# Growing Deep — Bible Study & Small Group Discipleship

**Growing Deep** is a thoughtful, trustworthy, and distraction-free Android application built with Jetpack Compose and Kotlin. Designed for both deep personal devotion and church small-group discipleship, it combines a readable scripture canvas with interactive original language tools (Greek & Hebrew), structured curriculum, small group facilitation, and a grounded AI study companion.

---

## Key Highlights & Core Pillars

### 1. Distraction-Free Scripture Reader
- **Editorial Typography & Styling**: Warm parchment tones, deep navy typography, and generous margins inspired by thoughtful editorial readers.
- **Multiple Public-Domain Translations**: Switch between Berean Standard Bible (BSB), World English Bible (WEB), and King James Version (KJV).
- **Dynamic Font Sizing**: Quick **A-** and **A+** typography scaling for accessible reading in any environment.
- **Original Language Word Study**: Tap the **Word Study** toggle to inspect Greek and Hebrew roots directly beneath English words, including Strong's concordance numbering, transliteration, pronunciation, lexical definition, and theological insights.
- **Parallel Translations & Cross-References**: Examine parallel renderings across translations and explore related scriptural passages with one-tap navigation.

### 2. Guarded AI Theological Assistant
- **Strict Epistemic Categories**: The AI companion separates every response into three distinct labels:
  - 📗 **"Scripture says"**: What is explicitly affirmed in the biblical text, complete with citations.
  - 📘 **"Theological interpretation"**: Deductions and systematic theology synthesized across Christian orthodoxy.
  - 📙 **"Debated among traditions"**: Objective, fair presentation of legitimate historical differences (e.g., Reformed vs. Arminian, Covenant vs. Dispensational).
- **Context-Aware Inquiries**: Automatically pulls in the currently viewed chapter and verse, with direct scripture links back into the reading view.

### 3. Structured Study Guides & Curriculum
- **Foundational Curriculum**: Includes a 15-lesson systematic study guide covering *The Gospel, Justification, Redemption, Regeneration, Adoption, The Nature of God, Christ, The Holy Spirit, Scripture, Prayer, Sanctification, Repentance, Identity in Christ, The Church, and Calling & Mission*.
- **Consistent Lesson Layout**: Each lesson includes clear objectives, primary scripture passages, bulleted teaching points, and numbered discussion questions (restarting at 1 for every lesson).
- **Leader Mode**: Leaders can toggle on facilitation tips, suggested answers, historical nuances, and discussion pointers.
- **Custom Guide Builder & Document Export**: Leaders can create custom study guides and export formatted curricula to printable plain text or shareable documents.

### 4. Church & Small Group Discipleship
- **Group Cohorts**: Create or join fellowship groups with shareable join codes (e.g., `DEEP-7749`).
- **Curriculum Assignment & Scheduling**: Assign specific study guides with weekly meeting rhythms.
- **Interactive Discussion Board**: Group members post reflections linked directly to lesson discussion questions.
- **Leader Dashboard**: Track member completion status for each lesson to aid in pastoral care and group preparation.

### 5. Personal Devotional Sanctuary & Privacy
- **Privacy Commitment**: Clear privacy boundaries ensure personal notes and highlights remain strictly private to the user, group discussions remain within the small group, and reflections are never used to train third-party models.
- **Theological Color-Coded Highlighting**: Organize verses by categories like *Promises*, *Commands*, *Questions to Research*, *Praise & Worship*, and *Identity in Christ*.
- **Habit & Streak Tracking**: Track daily reading streaks, mark verses for a "Read Later" queue, and automatically resume at the last read position.

---

## Technical Architecture

- **Language & UI**: 100% Kotlin with modern **Jetpack Compose** and **Material Design 3 (M3)**.
- **State Management**: Reactive MVVM architecture using Kotlin `StateFlow` and Compose `collectAsStateWithLifecycle()`.
- **Local Persistence**: Integrated **Room Database** (`GrowingDeepDatabase`) storing:
  - Highlighting records and color categories
  - Personal devotional notes
  - Bookmarks and Read-Later queues
  - Reading progress and streak metrics
  - Study guides and lesson plans
  - Small group memberships, assignments, completions, and discussion posts
- **AI Integration**: Powered by Google's Gemini API via server-side endpoints with structured prompts enforcing theological nuance and grounding.

---

## Project Structure

```
app/src/main/java/com/example/
├── MainActivity.kt                      # Main activity with 5-tab bottom navigation
├── data/
│   ├── local/                           # Room database entities, DAOs, and database definition
│   │   ├── GrowingDeepDatabase.kt
│   │   ├── Entities.kt                  # Notes, Highlights, Bookmarks, Groups, Guides, Lessons
│   │   └── Daos.kt                      # Reactive Flow-based data access objects
│   ├── model/                           # Domain models, Strong's lexicon, Bible text data
│   │   ├── BibleModels.kt               # BibleVerse, CrossReference, Translation enum
│   │   ├── StrongsDictionary.kt         # Greek & Hebrew roots, morphology, definitions
│   │   └── ScriptureContent.kt          # Pre-loaded public domain scriptures & sample texts
│   └── repository/                      # BibleRepository handling scripture, word study & cache
├── service/
│   └── GeminiStudyAssistantService.kt   # Theological prompt builder & response parser
└── ui/
    ├── MainViewModel.kt                 # Central ViewModel orchestrating app state
    ├── assistant/                       # AI Study Assistant view & epistemic badges
    ├── common/                          # Reusable chips, badges, and UI components
    ├── groups/                          # Small group discussion & leader dashboard
    ├── guides/                          # Study guide catalog, lesson detail, leader notes
    ├── personal/                        # Personal notes, highlights, bookmarks & streaks
    ├── reader/                          # Bible reading canvas, word study & verse drawer
    └── theme/                           # Parchment, navy, and gold Material 3 theme palette
```

---

## Running & Building

### Requirements
- Android SDK 35 (compileSdk / targetSdk 35, minSdk 26)
- Gradle Kotlin DSL (`build.gradle.kts`)

### Local Testing & Compilation
- **Compile project**:
  ```bash
  gradle :app:compileDebugKotlin
  ```
- **Run unit tests**:
  ```bash
  gradle :app:testDebugUnitTest
  ```
