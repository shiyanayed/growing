package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.data.bible.BibleRepository
import com.example.data.model.BibleVerse
import com.example.data.model.Translation
import com.example.ui.AiAssistantUiState
import com.example.ui.BibleReaderUiState
import com.example.ui.assistant.AiStudyScreen
import com.example.ui.reader.BibleReaderScreen
import com.example.ui.reader.WordStudyDialog
import com.example.ui.theme.MyApplicationTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ComposeScreenSmokeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testBibleReaderScreenRendersPassage() {
        val sampleVerses = listOf(
            BibleVerse(
                book = "Romans",
                chapter = 8,
                verse = 28,
                text = "And we know that in all things God works for the good of those who love him.",
                translation = Translation.BSB
            )
        )

        val readerState = BibleReaderUiState(
            currentBook = "Romans",
            currentChapter = 8,
            translation = Translation.BSB,
            verses = sampleVerses,
            isWordStudyModeActive = false
        )

        composeTestRule.setContent {
            MyApplicationTheme {
                BibleReaderScreen(
                    readerState = readerState,
                    streakDays = 5,
                    showPrivacyBanner = false,
                    onDismissPrivacyBanner = {},
                    onSelectBookChapter = { _, _ -> },
                    onSelectTranslation = {},
                    onToggleWordStudyMode = {},
                    onSelectVerse = {},
                    onInspectWord = {},
                    onDismissWordStudy = {},
                    onDismissVerseDetail = {},
                    onAddHighlight = { _, _ -> },
                    onRemoveHighlight = {},
                    onAddNote = { _, _, _ -> },
                    onDeleteNote = { _, _ -> },
                    onBookmark = { _, _, _ -> },
                    onAskAiAboutVerse = {},
                    onSetFontScale = {}
                )
            }
        }

        // Verify chapter title and verse text node is displayed
        composeTestRule.onAllNodesWithText("Romans 8").onFirst().assertIsDisplayed()
        composeTestRule.onNodeWithText("And we know that in all things God works for the good of those who love him.").assertIsDisplayed()
    }

    @Test
    fun testWordStudyDialogDisplaysLexicon() {
        var dismissed = false
        val graceEntry = BibleRepository.wordStudyDictionary["grace"] ?: error("grace entry missing")

        composeTestRule.setContent {
            MyApplicationTheme {
                WordStudyDialog(
                    entry = graceEntry,
                    onDismiss = { dismissed = true }
                )
            }
        }

        // Check dialog tag exists and close button triggers dismiss callback
        composeTestRule.onNodeWithTag("word_study_dialog").assertExists()
        composeTestRule.onNodeWithTag("close_word_study_btn").performClick()
        assert(dismissed)
    }

    @Test
    fun testAiStudyAssistantAboutMethodDialog() {
        val aiState = AiAssistantUiState(
            currentPassageRef = "Romans 8:28",
            isLoading = false
        )

        composeTestRule.setContent {
            MyApplicationTheme {
                AiStudyScreen(
                    state = aiState,
                    onQueryChanged = {},
                    onSendMessage = {},
                    onJumpToScripture = {}
                )
            }
        }

        // Tap the info action button to open About Our Method dialog
        composeTestRule.onNodeWithTag("about_method_btn").performClick()

        // Verify method criteria dialog exists
        composeTestRule.onNodeWithTag("about_method_dialog_content").assertExists()
        composeTestRule.onNodeWithTag("close_about_method_btn").performClick()
    }
}
