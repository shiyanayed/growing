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
  - 📗 **"Scripture says"**: What is explicitly affirmed in the biblical text using grammatical-historical exegesis, complete with verified citations.
  - 📘 **"Theological interpretation"**: Deductions and systematic theology synthesized across historical Christian orthodoxy (e.g. Nicene, Chalcedonian, Apostles' creeds, historic Reformation confessions).
  - 📙 **"Debated among traditions"**: Objective, fair presentation of legitimate historical differences (e.g., Reformed vs. Arminian soteriology, Credobaptist vs. Paedobaptist ecclesiology, Millennial eschatology).
- **Context-Aware Inquiries**: Automatically pulls in the currently viewed chapter and verse, with direct scripture links back into the reading view.
- **Three-Layer Anti-Hallucination Engine**:
  1. *Local Grounding*: Real passage text injected directly into prompt constraints.
  2. *Structural Canon Validation*: Zero-network 66-book canon table verifies book, chapter, and verse existence.
  3. *External Cross-Check*: Secondary references cross-checked and marked with verification chips.

### 3. Structured Study Guides & Curriculum
- **Foundational Curriculum**: Includes a 19-lesson systematic study guide covering:
  1. The Gospel of Grace
  2. Justification by Faith
  3. Redemption in Christ
  4. Regeneration & The New Birth
  5. Adoption into God's Family
  6. The Holy Trinity & Nature of God
  7. The Person & Work of Jesus Christ
  8. The Holy Spirit & Divine Guidance
  9. The Authority & Sufficiency of Scripture
  10. Intimacy with God in Prayer
  11. Sanctification & Walking in the Spirit
  12. Biblical Repentance & Mortification of Sin
  13. Secure Identity in Christ
  14. The Local Church & Spiritual Community
  15. Calling & Missional Ambassadorship
  16. **The Atonement of Christ** (*Penal substitution, Christus Victor, reconciliation*)
  17. **Suffering & Theodicy** (*Lament, God's sovereignty, eternal glory*)
  18. **The Sacraments: Baptism & The Lord's Supper** (*Covenant signs, communion*)
  19. **Eschatology: The Blessed Hope** (*Bodily resurrection, new heavens and new earth*)
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

## Theological Governance & Method ("About Our Method")

The application enforces a strict hermeneutical method to safeguard teachers, small groups, and believers from machine hallucination and modern ideological revisionism:

1. **"Scripture Says" Criteria**:
   - Strictly derived from grammatical-historical exegesis of canonical texts.
   - Restricts assertions to explicit propositions stated in the biblical text.
   - Refuses to label inferences or deductions as direct scriptural sayings.
2. **"Theological Interpretation" Criteria**:
   - Formulated in harmony with the historic Ecumenical Creeds: *The Apostles' Creed*, *The Nicene-Constantinopolitan Creed (381)*, and *The Definition of Chalcedon (451)*.
   - Informed by historical consensus across classical Christian orthodoxy (e.g. *Augustine, Athanasius, Anselm, Aquinas, Luther, Calvin, Cranmer, Owen, Wesley, Bavinck*).
   - Covers central dogmas: the Trinity, the hypostatic union of Christ, original sin, justification by grace through faith, bodily resurrection, and general revelation.
3. **"Debated Among Traditions" Criteria**:
   - Secondary and tertiary matters where godly, orthodox Christian communions differ historically.
   - Balanced presentation of:
     - **Soteriology**: Reformed (Monergism, Particular Atonement) vs. Arminian/Wesleyan (Synergism, Prevenient Grace) vs. Eastern Orthodox (Theosis/Synergy).
     - **Ecclesiology & Sacraments**: Credobaptist (Believer's immersion, memorialism/spiritual presence) vs. Paedobaptist (Infant covenant baptism, real spiritual presence/sacramental seal).
     - **Eschatology**: Historic Premillennialism, Amillennialism, Postmillennialism, and Dispensationalism.
     - **Spiritual Gifts**: Continuationist vs. Cessationist perspectives.

---

## Biblical Data & Lexicon Sources

All scripture texts and original language lexicons bundled in **Growing Deep** use open, verified public-domain or permissive scholarly sources:

1. **Biblical Texts**:
   - **Berean Standard Bible (BSB)**: Used by public domain dedication / royalty-free permission ([Berean.Bible](https://berean.bible)).
   - **World English Bible (WEB)**: Dedicated to the Public Domain ([WorldEnglish.Bible](https://worldenglish.bible)).
   - **King James Version (KJV)**: Public Domain globally (except Crown copyright limitations inside the UK).
2. **Greek & Hebrew Strong's Lexicon Data**:
   - Derived from **OpenScriptures Hebrew and Greek Lexicon project** ([OpenScriptures.org](https://openscriptures.github.io/)) and **STEPBible** data repository.
   - Augmented by **James Strong's Exhaustive Concordance of the Bible** (1890, Public Domain).
   - **Licensing**: Lexicon entries, morphological lemmas, and root definitions are released under Creative Commons Attribution 4.0 International (CC BY 4.0) and Public Domain dedications. Redistribution within the local app bundle complies fully with upstream attribution requirements.

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
