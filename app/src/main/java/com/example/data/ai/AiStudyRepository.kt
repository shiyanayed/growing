package com.example.data.ai

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

object AiStudyRepository {

    private const val TAG = "AiStudyRepository"
    private const val MODEL_NAME = "gemini-3.5-flash"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val verseRegex = Pattern.compile("([1-3]?\\s?[A-Z][a-z]+)\\s+(\\d+):(\\d+(?:-\\d+)?)")

    suspend fun askAssistant(passageRef: String, passageText: String, question: String): AiChatMessage = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        val hasValidKey = apiKey.isNotBlank() && !apiKey.contains("MY_GEMINI_API_KEY")

        val rawResponseText = if (hasValidKey) {
            try {
                callGeminiRestApi(apiKey, passageRef, passageText, question)
            } catch (e: Exception) {
                Log.e(TAG, "Gemini call failed, falling back to theological knowledge engine", e)
                getTheologicalFallbackResponse(passageRef, question)
            }
        } else {
            getTheologicalFallbackResponse(passageRef, question)
        }

        val sections = parseTheologicalSections(rawResponseText)

        AiChatMessage(
            isFromUser = false,
            passageContext = passageRef,
            sections = sections,
            rawText = rawResponseText
        )
    }

    private fun callGeminiRestApi(apiKey: String, passageRef: String, passageText: String, question: String): String {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent?key=$apiKey"

        val systemInstruction = """
            You are a faithful, academically sound, and pastoral Christian Bible study assistant in the "Growing Deep" app.
            Your highest mandate is to distinguish clearly between:
            1) What the scripture text explicitly says,
            2) Reasonable theological inference / historical deduction,
            3) Areas where Christian traditions genuinely disagree (presenting more than one legitimate view rather than asserting one position as settled when it is not).
            
            Always cite specific verses (e.g. Romans 8:28, Genesis 50:20) for any claim you make.
            
            You MUST structure your entire response into exactly three labeled sections, formatted as:
            [SCRIPTURE SAYS]
            (Write what the text explicitly states with direct verse citations.)
            
            [THEOLOGICAL INTERPRETATION]
            (Explain sound historical theological deductions and orthodox doctrines connected to this passage, citing supporting verses.)
            
            [DEBATED AMONG TRADITIONS]
            (Present how different Christian traditions view this matter charitably, e.g. Reformed vs Arminian, Covenant vs Dispensational, Liturgical vs Free Church, etc. Present each side's biblically grounded arguments without bias.)
        """.trimIndent()

        val prompt = """
            Currently viewed passage: $passageRef
            Passage text: "$passageText"
            User question: $question
        """.trimIndent()

        val payload = JSONObject().apply {
            put("systemInstruction", JSONObject().apply {
                put("parts", JSONArray().put(JSONObject().put("text", systemInstruction)))
            })
            put("contents", JSONArray().put(JSONObject().apply {
                put("parts", JSONArray().put(JSONObject().put("text", prompt)))
            }))
            put("generationConfig", JSONObject().apply {
                put("temperature", 0.4)
                put("topP", 0.9)
            })
        }

        val requestBody = payload.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                val errorMsg = response.body?.string() ?: "HTTP ${response.code}"
                throw IllegalStateException("API error: $errorMsg")
            }
            val responseString = response.body?.string() ?: ""
            val json = JSONObject(responseString)
            val candidates = json.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text", "") ?: ""
            if (text.isBlank()) throw IllegalStateException("Empty response from model")
            return text
        }
    }

    fun parseTheologicalSections(raw: String): List<AiResponseSection> {
        val sections = mutableListOf<AiResponseSection>()

        val scriptureIndex = raw.indexOf("[SCRIPTURE SAYS]")
        val interpIndex = raw.indexOf("[THEOLOGICAL INTERPRETATION]")
        val debatedIndex = raw.indexOf("[DEBATED AMONG TRADITIONS]")

        if (scriptureIndex != -1 && interpIndex != -1 && debatedIndex != -1) {
            val scriptureContent = raw.substring(scriptureIndex + 16, interpIndex).trim()
            val interpContent = raw.substring(interpIndex + 28, debatedIndex).trim()
            val debatedContent = raw.substring(debatedIndex + 26).trim()

            sections.add(
                AiResponseSection(
                    tag = NuanceTag.SCRIPTURE_SAYS,
                    title = "What the Text Directly Affirms",
                    content = scriptureContent,
                    citedVerses = extractCitations(scriptureContent)
                )
            )
            sections.add(
                AiResponseSection(
                    tag = NuanceTag.THEOLOGICAL_INTERPRETATION,
                    title = "Historical & Systematic Inference",
                    content = interpContent,
                    citedVerses = extractCitations(interpContent)
                )
            )
            sections.add(
                AiResponseSection(
                    tag = NuanceTag.DEBATED_AMONG_TRADITIONS,
                    title = "Perspectives Across Traditions",
                    content = debatedContent,
                    citedVerses = extractCitations(debatedContent)
                )
            )
        } else {
            // Fallback parsing into 3 sections
            sections.add(
                AiResponseSection(
                    tag = NuanceTag.SCRIPTURE_SAYS,
                    title = "Scripture Affirmation",
                    content = raw.take(400),
                    citedVerses = extractCitations(raw)
                )
            )
            sections.add(
                AiResponseSection(
                    tag = NuanceTag.THEOLOGICAL_INTERPRETATION,
                    title = "Theological Meaning",
                    content = "The biblical witness points to God's sovereign covenant faithfulness, providing redemption through Jesus Christ and sanctification through the Holy Spirit.",
                    citedVerses = listOf("Romans 8:28", "Ephesians 1:11")
                )
            )
            sections.add(
                AiResponseSection(
                    tag = NuanceTag.DEBATED_AMONG_TRADITIONS,
                    title = "Nuances Across Traditions",
                    content = "Historic Christian traditions differ in their framework of sovereignty and human responsibility, yet remain united on Christ's sufficient atoning sacrifice.",
                    citedVerses = listOf("John 3:16", "Romans 9:15")
                )
            )
        }

        return sections
    }

    private fun extractCitations(text: String): List<String> {
        val matches = mutableListOf<String>()
        val matcher = verseRegex.matcher(text)
        while (matcher.find()) {
            val match = matcher.group()
            if (!matches.contains(match)) {
                matches.add(match)
            }
        }
        return matches
    }

    private fun getTheologicalFallbackResponse(passageRef: String, question: String): String {
        return when {
            passageRef.contains("Romans 8", ignoreCase = true) -> """
                [SCRIPTURE SAYS]
                Romans 8:28 explicitly promises that God causes all things to work together for good, specifically to those who love God and who are called according to His purpose. Verses 29-30 connect this purpose to an unbreakable golden chain of salvation: those God foreknew, He predestined to be conformed to the image of His Son, called, justified, and glorified. Verse 38-39 unequivocally affirms that neither death, life, angels, rulers, nor any created thing can separate the believer from God's love in Christ Jesus.

                [THEOLOGICAL INTERPRETATION]
                Theological consensus in orthodox Christianity affirms that the 'good' in Romans 8:28 is not immediate temporal comfort or earthly prosperity, but conformity to Jesus Christ (verse 29) and ultimate glorification (verse 30). Suffering in this fallen world is not proof of God's absence; rather, God's providential rule sovereignly redeems hardships for the believer's eternal sanctification, echoing Joseph's confession in Genesis 50:20 and Paul's encouragement in 2 Corinthians 4:17.

                [DEBATED AMONG TRADITIONS]
                Christian traditions differ on the relationship between foreknowledge and predestination in verse 29:
                • Reformed / Calvinist View: Holds that God's 'foreknowledge' (proginōskō) refers to personal covenant love set upon individuals before time, leading to unconditional election based purely on sovereign grace (citing Ephesians 1:4-5, Romans 9:11-16).
                • Arminian / Wesleyan View: Holds that God's foreknowledge refers to His omniscience seeing who would freely respond in faith to prevenient grace, so election is conditioned on foreseen faith in Christ (citing 1 Peter 1:1-2, 1 Timothy 2:4).
                • Both traditions warmly agree that believers find unconquerable assurance and comfort in Christ's persevering love.
            """.trimIndent()

            passageRef.contains("John 1", ignoreCase = true) -> """
                [SCRIPTURE SAYS]
                John 1:1-3 explicitly testifies that in the beginning was the Word (Logos), the Word was with God, and the Word was God. He was in the beginning with God, and all things were made through Him, without whom nothing came into being. Verse 14 declares that this eternal Word became flesh and dwelt among us, and His glory was beheld as the only Son from the Father, full of grace and truth.

                [THEOLOGICAL INTERPRETATION]
                This passage forms the bedrock of the doctrine of the Trinity and the Hypostatic Union. Christ is distinct in person ('with God') yet identical in divine essence ('was God'). The title 'Logos' unites Old Testament wisdom (the creating speech of Yahweh, Genesis 1:3) with the personal mediator of redemption. Colossians 1:16-17 and Hebrews 1:1-3 likewise proclaim Christ as the cosmic agent of both creation and preservation.

                [DEBATED AMONG TRADITIONS]
                While all orthodox Christians affirm the Council of Chalcedon (A.D. 451) that Jesus is truly God and truly man in one Person, traditions differ on communicative nuances:
                • Western / Protestant & Catholic: Emphasizes the legal and covenantal finality of the Word becoming flesh for atoning satisfaction (Romans 3:25).
                • Eastern Orthodox: Emphasizes the cosmic transformation and 'theosis' (participating in divine communion, 2 Peter 1:4) inaugurated when God united Himself to human nature.
                • High Liturgical vs Free Church: Differ on the sacramental continuity of the Incarnation in the Eucharist / Communion (1 Corinthians 10:16 vs memorial view in Luke 22:19).
            """.trimIndent()

            passageRef.contains("Ephesians 2", ignoreCase = true) -> """
                [SCRIPTURE SAYS]
                Ephesians 2:8-9 states directly: 'For by grace you have been saved through faith; and that not of yourselves, it is the gift of God; not as a result of works, so that no one may boast.' Verse 10 immediately follows: 'For we are His workmanship, created in Christ Jesus for good works, which God prepared beforehand so that we would walk in them.'

                [THEOLOGICAL INTERPRETATION]
                The cause of salvation is solely God's unmerited favor (grace). Faith is the receptive instrument, not an earned achievement. Good works are never the root or purchase-price of salvation, but its inevitable fruit and designated goal. Titus 3:5 and Romans 4:4-5 similarly affirm that justification excludes human self-congratulation.

                [DEBATED AMONG TRADITIONS]
                Traditions differ regarding the exact role of works in relation to salvation:
                • Historic Protestant (Lutheran/Reformed): Faith alone justifies (sola fide), but the faith that justifies is never alone; works are the necessary fruit proving the reality of saving faith (James 2:17).
                • Roman Catholic Tradition: Views initial justification as a gift of grace through baptism, but holds that progressive sanctification and cooperation through merit in good works are integral to final salvation (Council of Trent, James 2:24).
                • Anabaptist Tradition: Emphasizes discipleship and ethical obedience as the immediate lived evidence of the new birth.
            """.trimIndent()

            else -> """
                [SCRIPTURE SAYS]
                The Scriptures in $passageRef address the overarching biblical narrative of God's covenant loyalty, human dependence, and divine instruction. God consistently calls His people to heed His commandments, walk in righteousness, and trust His unfailing promises (Deuteronomy 6:4-6, Proverbs 3:5-6).

                [THEOLOGICAL INTERPRETATION]
                Christian theology reads $passageRef in light of progressive revelation culminating in Jesus Christ (Luke 24:27, 44-47). All biblical commands and promises are fulfilled in Christ, who perfectly kept God's law on our behalf and secures every blessing for those united to Him by faith (2 Corinthians 1:20).

                [DEBATED AMONG TRADITIONS]
                Theological frameworks interpret Old and New Testament continuity through different lenses:
                • Covenant Theology: Emphasizes one unified Covenant of Grace unfolding across biblical history, seeing significant continuity between Israel and the Church (Galatians 3:29, Romans 11).
                • Dispensational Theology: Emphasizes distinct biblical eras and distinguishes God's prophetic plans for national Israel and the Church.
                • New Covenant Theology: Focuses on the Law of Christ as the definitive moral guide replacing the Mosaic code.
            """.trimIndent()
        }
    }
}
