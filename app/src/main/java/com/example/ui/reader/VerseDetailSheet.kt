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
import com.example.data.local.HighlightEntity
import com.example.data.local.NoteEntity
import com.example.data.model.BibleVerse
import com.example.data.model.CrossReference
import com.example.data.model.HighlightTag
import com.example.data.model.Translation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerseDetailSheet(
    verse: BibleVerse,
    parallelVerses: Map<Translation, String>,
    crossReferences: List<CrossReference>,
    notes: List<NoteEntity>,
    highlight: HighlightEntity?,
    onDismiss: () -> Unit,
    onAddHighlight: (HighlightTag) -> Unit,
    onRemoveHighlight: () -> Unit,
    onAddNote: (content: String, visibility: String) -> Unit,
    onDeleteNote: (String) -> Unit,
    onBookmark: (isReadLater: Boolean) -> Unit,
    onAskAiAboutVerse: (String) -> Unit,
    onNavigateToCrossReference: (String) -> Unit
) {
    var selectedSheetTab by remember { mutableStateOf(0) }
    val tabs = listOf("Parallel", "Cross-Refs", "Study & Notes")

    var noteInputText by remember { mutableStateOf("") }
    var noteVisibility by remember { mutableStateOf("private") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("verse_detail_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header: Verse Ref & Quick AI Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = verse.reference,
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Viewing in ${verse.translation.displayName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledTonalButton(
                        onClick = {
                            onDismiss()
                            onAskAiAboutVerse(verse.reference)
                        },
                        modifier = Modifier.testTag("ask_ai_from_verse_btn"),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AutoAwesome,
                            contentDescription = "Ask AI",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Ask AI",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_sheet_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close Sheet")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Verse Text Quote Box
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "\"${verse.text}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.Serif,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Primary Tabs
            TabRow(
                selectedTabIndex = selectedSheetTab,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedSheetTab == index,
                        onClick = { selectedSheetTab = index },
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedSheetTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedSheetTab == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.testTag("verse_tab_$index")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Content
            when (selectedSheetTab) {
                0 -> {
                    // Parallel Translations Tab (BSB, WEB, KJV)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 380.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                "Side-by-Side Public Domain Translations",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        items(Translation.values()) { trans ->
                            val translationText = parallelVerses[trans] ?: verse.text
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (trans == verse.translation) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (trans == verse.translation) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = trans.displayName,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = trans.code,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = translationText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = FontFamily.Serif,
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }
                        item { Spacer(modifier = Modifier.height(20.dp)) }
                    }
                }

                1 -> {
                    // Cross-References Tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 380.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                "Scripture Cross-References",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (crossReferences.isEmpty()) {
                            item {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                                ) {
                                    Text(
                                        "No manual cross-references recorded for this specific verse in prototype corpus. Tap 'Ask AI' to explore connected passages.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        } else {
                            items(crossReferences) { xref ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            onDismiss()
                                            onNavigateToCrossReference(xref.targetRef)
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Link,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = xref.targetRef,
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = xref.previewText,
                                                style = MaterialTheme.typography.bodySmall,
                                                fontFamily = FontFamily.Serif,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.Default.ChevronRight,
                                            contentDescription = "Jump",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                        item { Spacer(modifier = Modifier.height(20.dp)) }
                    }
                }

                2 -> {
                    // Study & Notes Tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 420.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Highlighting Section
                        item {
                            Text(
                                "Highlight with Color Tag",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                HighlightTag.values().forEach { tag ->
                                    val isTagged = highlight?.colorTag == tag.label
                                    Surface(
                                        shape = CircleShape,
                                        color = tag.composeColor,
                                        border = if (isTagged) androidx.compose.foundation.BorderStroke(2.5.dp, MaterialTheme.colorScheme.primary) else null,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .clickable { onAddHighlight(tag) }
                                            .testTag("tag_circle_${tag.name}")
                                    ) {
                                        if (isTagged) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = Color.Black,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                                if (highlight != null) {
                                    IconButton(
                                        onClick = onRemoveHighlight,
                                        modifier = Modifier.size(36.dp).testTag("remove_highlight_btn")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.FormatColorReset,
                                            contentDescription = "Clear Highlight",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }

                        // Bookmark & Read Later Actions
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onBookmark(false) },
                                    modifier = Modifier.weight(1f).testTag("bookmark_btn")
                                ) {
                                    Icon(imageVector = Icons.Outlined.BookmarkBorder, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Bookmark", style = MaterialTheme.typography.labelMedium)
                                }
                                OutlinedButton(
                                    onClick = { onBookmark(true) },
                                    modifier = Modifier.weight(1f).testTag("read_later_btn")
                                ) {
                                    Icon(imageVector = Icons.Outlined.Schedule, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Read Later", style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }

                        // Personal Notes on this Verse
                        item {
                            Text(
                                "Personal Notes (${notes.size})",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = noteInputText,
                                onValueChange = { noteInputText = it },
                                placeholder = { Text("Write your devotional reflection or question...") },
                                modifier = Modifier.fillMaxWidth().testTag("verse_note_input"),
                                minLines = 2,
                                maxLines = 4
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    FilterChip(
                                        selected = noteVisibility == "private",
                                        onClick = { noteVisibility = "private" },
                                        label = { Text("Private") },
                                        leadingIcon = {
                                            Icon(imageVector = Icons.Outlined.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
                                        },
                                        modifier = Modifier.testTag("note_visibility_private")
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    FilterChip(
                                        selected = noteVisibility == "group",
                                        onClick = { noteVisibility = "group" },
                                        label = { Text("Group Shared") },
                                        leadingIcon = {
                                            Icon(imageVector = Icons.Outlined.Groups, contentDescription = null, modifier = Modifier.size(14.dp))
                                        },
                                        modifier = Modifier.testTag("note_visibility_group")
                                    )
                                }
                                Button(
                                    onClick = {
                                        if (noteInputText.isNotBlank()) {
                                            onAddNote(noteInputText, noteVisibility)
                                            noteInputText = ""
                                        }
                                    },
                                    modifier = Modifier.testTag("save_verse_note_btn")
                                ) {
                                    Text("Save Note")
                                }
                            }
                        }

                        // Existing notes list
                        items(notes) { n ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = if (n.visibility == "private") MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
                                            ) {
                                                Text(
                                                    text = if (n.visibility == "private") "Private" else "Shared to Group",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = n.content, style = MaterialTheme.typography.bodyMedium)
                                    }
                                    IconButton(
                                        onClick = { onDeleteNote(n.id) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Note", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                        item { Spacer(modifier = Modifier.height(20.dp)) }
                    }
                }
            }
        }
    }
}
