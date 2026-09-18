package com.example.ui.guides

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.GuideEntity
import com.example.data.local.LessonEntity
import com.example.ui.common.VerseCitationChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuidesScreen(
    guides: List<GuideEntity>,
    selectedGuide: GuideEntity?,
    selectedLesson: LessonEntity?,
    lessons: List<LessonEntity>,
    isLeaderModeActive: Boolean,
    onToggleLeaderMode: (Boolean) -> Unit,
    onSelectGuide: (GuideEntity?) -> Unit,
    onSelectLesson: (LessonEntity?) -> Unit,
    onJumpToScripture: (String) -> Unit,
    onDiscussInGroup: (lessonId: String, questionIndex: Int) -> Unit,
    onCreateCustomGuide: (title: String, level: String, desc: String, lessons: List<LessonEntity>) -> Unit,
    onToggleCompleteLesson: (lessonId: String, completed: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedLevelFilter by remember { mutableStateOf("All") }
    var showCreateGuideDialog by remember { mutableStateOf(false) }

    val levels = listOf("All", "Foundational", "Intermediate", "Advanced")
    val filteredGuides = if (selectedLevelFilter == "All") guides else guides.filter { it.level == selectedLevelFilter }

    if (selectedLesson != null && selectedGuide != null) {
        // Full Lesson Detail View
        LessonDetailView(
            guide = selectedGuide,
            lesson = selectedLesson,
            allLessonsInGuide = lessons,
            isLeaderModeActive = isLeaderModeActive,
            onToggleLeaderMode = onToggleLeaderMode,
            onBack = { onSelectLesson(null) },
            onJumpToScripture = onJumpToScripture,
            onDiscussInGroup = onDiscussInGroup,
            onToggleComplete = { isComplete -> onToggleCompleteLesson(selectedLesson.id, isComplete) },
            onSelectNextLesson = { next -> onSelectLesson(next) },
            onExportGuide = {
                exportGuideDocument(context, selectedGuide, lessons)
            }
        )
    } else if (selectedGuide != null) {
        // Guide Lessons List View
        GuideLessonsListView(
            guide = selectedGuide,
            lessons = lessons,
            isLeaderModeActive = isLeaderModeActive,
            onToggleLeaderMode = onToggleLeaderMode,
            onBack = { onSelectGuide(null) },
            onSelectLesson = onSelectLesson,
            onExportGuide = {
                exportGuideDocument(context, selectedGuide, lessons)
            }
        )
    } else {
        // Main Guides Catalog View
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                "Study Guides Library",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Structured biblical curriculum for devotion & groups",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { showCreateGuideDialog = true },
                            modifier = Modifier.testTag("create_custom_guide_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircleOutline,
                                contentDescription = "Create Custom Guide",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )
            },
            modifier = modifier
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .testTag("guides_catalog_list"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Leader Mode Banner with Toggle
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth().testTag("leader_mode_banner")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.SupervisorAccount,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        "Leader Mode",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        "Unlocks facilitation tips, answer keys, and leader notes",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Switch(
                                checked = isLeaderModeActive,
                                onCheckedChange = onToggleLeaderMode,
                                modifier = Modifier.testTag("leader_mode_switch")
                            )
                        }
                    }
                }

                // Difficulty Level Filter Chips
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        levels.forEach { lvl ->
                            FilterChip(
                                selected = selectedLevelFilter == lvl,
                                onClick = { selectedLevelFilter = lvl },
                                label = { Text(lvl) },
                                modifier = Modifier.testTag("level_chip_$lvl")
                            )
                        }
                    }
                }

                // Guide Cards
                items(filteredGuides) { guide ->
                    GuideCard(
                        guide = guide,
                        onClick = { onSelectGuide(guide) }
                    )
                }
            }
        }
    }

    if (showCreateGuideDialog) {
        CreateGuideDialog(
            onDismiss = { showCreateGuideDialog = false },
            onCreate = { title, level, desc, newLessons ->
                onCreateCustomGuide(title, level, desc, newLessons)
                showCreateGuideDialog = false
            }
        )
    }
}

@Composable
fun GuideCard(
    guide: GuideEntity,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("guide_card_${guide.id}")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (guide.level) {
                        "Foundational" -> MaterialTheme.colorScheme.primaryContainer
                        "Intermediate" -> MaterialTheme.colorScheme.secondaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ) {
                    Text(
                        text = guide.level,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${guide.totalLessons} Lessons",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = guide.title,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = guide.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Explore Curriculum",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideLessonsListView(
    guide: GuideEntity,
    lessons: List<LessonEntity>,
    isLeaderModeActive: Boolean,
    onToggleLeaderMode: (Boolean) -> Unit,
    onBack: () -> Unit,
    onSelectLesson: (LessonEntity) -> Unit,
    onExportGuide: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        guide.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("guide_back_btn")) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onExportGuide, modifier = Modifier.testTag("export_guide_btn")) {
                        Icon(imageVector = Icons.Outlined.Share, contentDescription = "Export Guide Document")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("guide_lessons_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${guide.level} Coursework",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = guide.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            items(lessons) { lesson ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectLesson(lesson) }
                        .testTag("lesson_row_${lesson.orderIndex}")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${lesson.orderIndex}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = lesson.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = lesson.objective,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Open Lesson",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LessonDetailView(
    guide: GuideEntity,
    lesson: LessonEntity,
    allLessonsInGuide: List<LessonEntity>,
    isLeaderModeActive: Boolean,
    onToggleLeaderMode: (Boolean) -> Unit,
    onBack: () -> Unit,
    onJumpToScripture: (String) -> Unit,
    onDiscussInGroup: (lessonId: String, questionIndex: Int) -> Unit,
    onToggleComplete: (Boolean) -> Unit,
    onSelectNextLesson: (LessonEntity) -> Unit,
    onExportGuide: () -> Unit
) {
    var isCompleted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Lesson ${lesson.orderIndex}: ${lesson.title}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = guide.title,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("lesson_detail_back_btn")) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onExportGuide, modifier = Modifier.testTag("export_lesson_btn")) {
                        Icon(imageVector = Icons.Outlined.Share, contentDescription = "Export Lesson")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("lesson_detail_scroll_view"),
            contentPadding = PaddingValues(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Objective Card
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth().testTag("lesson_objective_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Flag,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Lesson Objective",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = lesson.objective,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // Key Scriptures
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Key Scriptures",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        lesson.keyVerses.forEach { vRef ->
                            VerseCitationChip(
                                verseRef = vRef,
                                onClick = { onJumpToScripture(vRef) }
                            )
                        }
                    }
                }
            }

            // Teaching Points
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Teaching Points",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    lesson.teachingPoints.forEach { point ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "• ",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = point,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 22.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Discussion Questions (Numbered, restarting at 1)
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Discussion Questions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    lesson.discussionQuestions.forEachIndexed { index, question ->
                        val questionNumber = index + 1
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "$questionNumber",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = question,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 22.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = { onDiscussInGroup(lesson.id, questionNumber) },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.testTag("discuss_q_${questionNumber}_btn")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Forum,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Discuss in Group", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Leader Mode Section (Facilitation Tips & Suggested Answers)
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isLeaderModeActive) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.surfaceVariant,
                    border = if (isLeaderModeActive) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                    modifier = Modifier.fillMaxWidth().testTag("leader_notes_section")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.School,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Leader Facilitation Notes",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Switch(
                                checked = isLeaderModeActive,
                                onCheckedChange = onToggleLeaderMode
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (isLeaderModeActive) {
                            Text(
                                text = lesson.leaderNotes,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                lineHeight = 22.sp
                            )
                        } else {
                            Text(
                                text = "Leader mode is toggled off. Enable the switch above to view suggested answers, historical nuances, and group facilitation guides.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Complete Lesson & Next Lesson
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            isCompleted = !isCompleted
                            onToggleComplete(isCompleted)
                        },
                        modifier = Modifier.weight(1f).testTag("toggle_complete_lesson_btn")
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Outlined.CheckCircleOutline,
                            contentDescription = null,
                            tint = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isCompleted) "Completed" else "Mark Complete")
                    }

                    val nextLesson = allLessonsInGuide.find { it.orderIndex == lesson.orderIndex + 1 }
                    if (nextLesson != null) {
                        Button(
                            onClick = { onSelectNextLesson(nextLesson) },
                            modifier = Modifier.weight(1f).testTag("next_lesson_btn")
                        ) {
                            Text("Next Lesson")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreateGuideDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, level: String, desc: String, lessons: List<LessonEntity>) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var level by remember { mutableStateOf("Foundational") }
    var desc by remember { mutableStateOf("") }
    var lesson1Title by remember { mutableStateOf("") }
    var lesson1Objective by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Custom Study Guide") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Guide Title (e.g. Gospel of Mark)") },
                    modifier = Modifier.fillMaxWidth().testTag("new_guide_title_input")
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Foundational", "Intermediate", "Advanced").forEach { lvl ->
                        FilterChip(
                            selected = level == lvl,
                            onClick = { level = lvl },
                            label = { Text(lvl, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Overview & Target Audience") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                HorizontalDivider()
                Text("Lesson 1 Details:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = lesson1Title,
                    onValueChange = { lesson1Title = it },
                    label = { Text("Lesson 1 Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = lesson1Objective,
                    onValueChange = { lesson1Objective = it },
                    label = { Text("Lesson 1 Objective") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val starterLesson = LessonEntity(
                            id = "",
                            guideId = "",
                            orderIndex = 1,
                            title = lesson1Title.ifBlank { "Introduction" },
                            objective = lesson1Objective.ifBlank { "Understand the historical foundation and central message." },
                            keyVerses = listOf("John 1:1-4"),
                            teachingPoints = listOf(
                                "God reveals Himself through His inspired Word.",
                                "Faith seeks understanding through careful, prayerful reading."
                            ),
                            discussionQuestions = listOf(
                                "What stood out most to you from this passage?",
                                "How does this truth influence your daily decisions this week?"
                            ),
                            leaderNotes = "Leader Tip: Open with prayer and encourage everyone to share openly without fear of judgment."
                        )
                        onCreate(title, level, desc, listOf(starterLesson))
                    }
                },
                enabled = title.isNotBlank(),
                modifier = Modifier.testTag("confirm_create_guide_btn")
            ) {
                Text("Create Guide")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// Function to export guide as clean printable document
fun exportGuideDocument(context: android.content.Context, guide: GuideEntity, lessons: List<LessonEntity>) {
    val builder = StringBuilder()
    builder.appendLine("==================================================")
    builder.appendLine("GROWING DEEP BIBLE STUDY CURRICULUM")
    builder.appendLine("Title: ${guide.title.uppercase()}")
    builder.appendLine("Level: ${guide.level} • Total Lessons: ${guide.totalLessons}")
    builder.appendLine("==================================================")
    builder.appendLine(guide.description)
    builder.appendLine()

    lessons.forEach { lesson ->
        builder.appendLine("--------------------------------------------------")
        builder.appendLine("LESSON ${lesson.orderIndex}: ${lesson.title.uppercase()}")
        builder.appendLine("Objective: ${lesson.objective}")
        builder.appendLine("Key Verses: ${lesson.keyVerses.joinToString(", ")}")
        builder.appendLine("--------------------------------------------------")
        builder.appendLine("TEACHING POINTS:")
        lesson.teachingPoints.forEach { p ->
            builder.appendLine("  • $p")
        }
        builder.appendLine()
        builder.appendLine("DISCUSSION QUESTIONS:")
        lesson.discussionQuestions.forEachIndexed { idx, q ->
            builder.appendLine("  ${idx + 1}. $q")
        }
        builder.appendLine()
        builder.appendLine("LEADER FACILITATION NOTES:")
        builder.appendLine("  ${lesson.leaderNotes}")
        builder.appendLine()
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Growing Deep Curriculum - ${guide.title}")
        putExtra(Intent.EXTRA_TEXT, builder.toString())
    }
    context.startActivity(Intent.createChooser(intent, "Export Study Guide Document"))
}
