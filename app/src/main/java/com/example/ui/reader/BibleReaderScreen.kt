package com.example.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.bible.BibleRepository
import com.example.data.model.BibleVerse
import com.example.data.model.HighlightTag
import com.example.data.model.Translation
import com.example.ui.BibleReaderUiState
import com.example.ui.common.PrivacyCommitmentBanner
import com.example.ui.common.TranslationSelectorRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibleReaderScreen(
    readerState: BibleReaderUiState,
    streakDays: Int,
    showPrivacyBanner: Boolean,
    onDismissPrivacyBanner: () -> Unit,
    onSelectBookChapter: (book: String, chapter: Int) -> Unit,
    onSelectTranslation: (Translation) -> Unit,
    onToggleWordStudyMode: () -> Unit,
    onSelectVerse: (BibleVerse) -> Unit,
    onInspectWord: (String) -> Unit,
    onDismissWordStudy: () -> Unit,
    onDismissVerseDetail: () -> Unit,
    onAddHighlight: (verseRef: String, HighlightTag) -> Unit,
    onRemoveHighlight: (verseRef: String) -> Unit,
    onAddNote: (verseRef: String, content: String, visibility: String) -> Unit,
    onDeleteNote: (String, verseRef: String) -> Unit,
    onBookmark: (verseRef: String, passageTitle: String, isReadLater: Boolean) -> Unit,
    onAskAiAboutVerse: (String) -> Unit,
    onSetFontScale: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var showBookPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // Top App Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Book & Chapter Dropdown Trigger
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showBookPicker = true }
                            .testTag("book_chapter_picker_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${readerState.currentBook} ${readerState.currentChapter}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Change Book/Chapter",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    // Actions: Streak & Word Study Mode Toggle & Font Scale
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Streak badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.testTag("streak_badge")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "Reading Streak",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "$streakDays d",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }

                        // Word Study Mode Toggle
                        IconButton(
                            onClick = onToggleWordStudyMode,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (readerState.isWordStudyModeActive)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant
                                )
                                .testTag("toggle_word_study_mode_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Translate,
                                contentDescription = "Word Study Mode (Hebrew/Greek)",
                                tint = if (readerState.isWordStudyModeActive)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Font size down
                        IconButton(
                            onClick = { onSetFontScale(readerState.fontScale - 0.1f) },
                            modifier = Modifier.size(32.dp).testTag("font_scale_down_btn")
                        ) {
                            Text("A-", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }

                        // Font size up
                        IconButton(
                            onClick = { onSetFontScale(readerState.fontScale + 0.1f) },
                            modifier = Modifier.size(32.dp).testTag("font_scale_up_btn")
                        ) {
                            Text("A+", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Translation bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TranslationSelectorRow(
                        current = readerState.translation,
                        onSelect = onSelectTranslation,
                        modifier = Modifier.weight(1f)
                    )

                    if (readerState.isWordStudyModeActive) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                "Word Study Active",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("bible_reader_scroll_view"),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (showPrivacyBanner) {
                item {
                    PrivacyCommitmentBanner(onDismiss = onDismissPrivacyBanner)
                }
            }

            // Word Study Mode Hint Banner
            if (readerState.isWordStudyModeActive) {
                item {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Word Study Mode ON: Tap any word to look up its underlying Greek or Hebrew root, Strong's Concordance definition, and theological meaning.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // Chapter Title Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${readerState.currentBook} ${readerState.currentChapter}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = readerState.translation.displayName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Verses List
            items(readerState.verses) { verse ->
                VerseItem(
                    verse = verse,
                    isWordStudyMode = readerState.isWordStudyModeActive,
                    fontScale = readerState.fontScale,
                    highlightTag = null, // Will match if highlighted
                    onVerseClick = { onSelectVerse(verse) },
                    onWordClick = { word -> onInspectWord(word) }
                )
            }

            // Bottom Chapter Navigators
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = {
                            if (readerState.currentChapter > 1) {
                                onSelectBookChapter(readerState.currentBook, readerState.currentChapter - 1)
                            }
                        },
                        enabled = readerState.currentChapter > 1,
                        modifier = Modifier.testTag("prev_chapter_btn")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Previous Chapter")
                    }

                    Button(
                        onClick = {
                            onSelectBookChapter(readerState.currentBook, readerState.currentChapter + 1)
                        },
                        modifier = Modifier.testTag("next_chapter_btn")
                    ) {
                        Text("Next Chapter")
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }

    // Book & Chapter Picker Dialog
    if (showBookPicker) {
        AlertDialog(
            onDismissRequest = { showBookPicker = false },
            title = { Text("Navigate Scripture", style = MaterialTheme.typography.titleLarge) },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    items(BibleRepository.books) { b ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    onSelectBookChapter(b.name, 1)
                                    showBookPicker = false
                                },
                            color = if (b.name == readerState.currentBook)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = b.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${b.testament} • ${b.chapterCount} Chapters",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showBookPicker = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Word Study Inspector Dialog
    if (readerState.selectedWordStudy != null) {
        WordStudyDialog(
            entry = readerState.selectedWordStudy,
            onDismiss = onDismissWordStudy
        )
    }

    // Verse Detail Side/Bottom Panel
    if (readerState.selectedVerse != null) {
        VerseDetailSheet(
            verse = readerState.selectedVerse,
            parallelVerses = readerState.parallelVerses,
            crossReferences = readerState.crossReferences,
            notes = readerState.notesForSelectedVerse,
            highlight = readerState.highlightForSelectedVerse,
            onDismiss = onDismissVerseDetail,
            onAddHighlight = { tag -> onAddHighlight(readerState.selectedVerse.reference, tag) },
            onRemoveHighlight = { onRemoveHighlight(readerState.selectedVerse.reference) },
            onAddNote = { content, vis -> onAddNote(readerState.selectedVerse.reference, content, vis) },
            onDeleteNote = { noteId -> onDeleteNote(noteId, readerState.selectedVerse.reference) },
            onBookmark = { isReadLater -> onBookmark(readerState.selectedVerse.reference, "${readerState.currentBook} ${readerState.currentChapter}", isReadLater) },
            onAskAiAboutVerse = onAskAiAboutVerse,
            onNavigateToCrossReference = { targetRef ->
                val parts = targetRef.split(" ")
                if (parts.size >= 2) {
                    val book = if (parts.size == 3) "${parts[0]} ${parts[1]}" else parts[0]
                    val chapter = parts.last().split(":").firstOrNull()?.toIntOrNull() ?: 1
                    onSelectBookChapter(book, chapter)
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VerseItem(
    verse: BibleVerse,
    isWordStudyMode: Boolean,
    fontScale: Float,
    highlightTag: HighlightTag?,
    onVerseClick: () -> Unit,
    onWordClick: (String) -> Unit
) {
    val baseFontSize = 18.sp * fontScale
    val baseLineHeight = 28.sp * fontScale

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = highlightTag?.composeColor?.copy(alpha = 0.25f) ?: Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onVerseClick() }
            .testTag("verse_item_${verse.verse}")
            .padding(vertical = 4.dp, horizontal = 6.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            // Verse Number
            Text(
                text = "${verse.verse}",
                fontSize = 13.sp * fontScale,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .width(28.dp)
                    .padding(top = 4.dp)
            )

            // Verse Words / Interactive Text
            if (isWordStudyMode) {
                // Word Study Mode: individual words clickable
                val words = verse.text.split(" ")
                Column(modifier = Modifier.weight(1f)) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        words.forEach { word ->
                            val cleanWord = word.lowercase().replace(Regex("[^a-z]"), "")
                            val hasDictionaryEntry = BibleRepository.wordStudyDictionary.containsKey(cleanWord)
                            Text(
                                text = word,
                                fontSize = baseFontSize,
                                lineHeight = baseLineHeight,
                                fontFamily = FontFamily.Serif,
                                color = if (hasDictionaryEntry) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                                fontWeight = if (hasDictionaryEntry) FontWeight.SemiBold else FontWeight.Normal,
                                textDecoration = if (hasDictionaryEntry) androidx.compose.ui.text.style.TextDecoration.Underline else null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .clickable {
                                        if (hasDictionaryEntry) {
                                            onWordClick(cleanWord)
                                        } else {
                                            onVerseClick()
                                        }
                                    }
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = verse.text,
                    fontSize = baseFontSize,
                    lineHeight = baseLineHeight,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
