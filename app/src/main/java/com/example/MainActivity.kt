package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppTab
import com.example.ui.MainViewModel
import com.example.ui.assistant.AiStudyScreen
import com.example.ui.groups.GroupsScreen
import com.example.ui.guides.GuidesScreen
import com.example.ui.personal.PersonalDevotionScreen
import com.example.ui.reader.BibleReaderScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                GrowingDeepApp(viewModel = viewModel)
            }
        }
    }
}

data class NavigationTabItem(
    val tab: AppTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun GrowingDeepApp(viewModel: MainViewModel) {
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val isLeaderMode by viewModel.isLeaderModeActive.collectAsStateWithLifecycle()
    val showPrivacyBanner by viewModel.showPrivacyBanner.collectAsStateWithLifecycle()

    val readerState by viewModel.readerState.collectAsStateWithLifecycle()
    val readingProgress by viewModel.readingProgress.collectAsStateWithLifecycle()
    val allHighlights by viewModel.allHighlights.collectAsStateWithLifecycle()
    val allNotes by viewModel.allNotes.collectAsStateWithLifecycle()
    val allBookmarks by viewModel.allBookmarks.collectAsStateWithLifecycle()

    val assistantState by viewModel.assistantState.collectAsStateWithLifecycle()

    val allGuides by viewModel.allGuides.collectAsStateWithLifecycle()
    val selectedGuide by viewModel.selectedGuide.collectAsStateWithLifecycle()
    val selectedLesson by viewModel.selectedLesson.collectAsStateWithLifecycle()
    val lessonsInSelectedGuide by viewModel.getLessonsForSelectedGuide().collectAsState(initial = emptyList())

    val activeGroup by viewModel.activeGroup.collectAsStateWithLifecycle()
    val activeGroupMembers by viewModel.activeGroupMembers.collectAsStateWithLifecycle()
    val activeGroupAssignment by viewModel.activeGroupAssignment.collectAsStateWithLifecycle()
    val activeGroupCompletions by viewModel.activeGroupCompletions.collectAsStateWithLifecycle()
    val discussionPosts by viewModel.getDiscussionPostsForLesson("lesson_foundational_1").collectAsState(initial = emptyList())

    val navItems = listOf(
        NavigationTabItem(AppTab.READER, "Bible", Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
        NavigationTabItem(AppTab.ASSISTANT, "AI Study", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
        NavigationTabItem(AppTab.GUIDES, "Guides", Icons.Filled.School, Icons.Outlined.School),
        NavigationTabItem(AppTab.GROUPS, "Groups", Icons.Filled.Groups, Icons.Outlined.Groups),
        NavigationTabItem(AppTab.DEVOTION, "Devotion", Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("main_bottom_navigation")
            ) {
                navItems.forEach { item ->
                    val isSelected = activeTab == item.tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectTab(item.tab) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_tab_${item.tab.name}")
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (activeTab) {
                AppTab.READER -> {
                    BibleReaderScreen(
                        readerState = readerState,
                        streakDays = readingProgress?.streakDays ?: 12,
                        showPrivacyBanner = showPrivacyBanner,
                        onDismissPrivacyBanner = { viewModel.dismissPrivacyBanner() },
                        onSelectBookChapter = { book, chapter -> viewModel.loadChapter(book, chapter) },
                        onSelectTranslation = { trans -> viewModel.setTranslation(trans) },
                        onToggleWordStudyMode = { viewModel.toggleWordStudyMode() },
                        onSelectVerse = { verse -> viewModel.selectVerse(verse) },
                        onInspectWord = { word -> viewModel.inspectWord(word) },
                        onDismissWordStudy = { viewModel.dismissWordStudy() },
                        onDismissVerseDetail = { viewModel.dismissVerseDetail() },
                        onAddHighlight = { vRef, tag -> viewModel.addHighlight(vRef, tag) },
                        onRemoveHighlight = { vRef -> viewModel.removeHighlight(vRef) },
                        onAddNote = { vRef, content, vis -> viewModel.addNote(vRef, content, vis) },
                        onDeleteNote = { noteId, vRef -> viewModel.deleteNote(noteId, vRef) },
                        onBookmark = { vRef, title, isReadLater -> viewModel.bookmarkCurrentPassage(vRef, title, isReadLater) },
                        onAskAiAboutVerse = { vRef ->
                            viewModel.selectTab(AppTab.ASSISTANT)
                            viewModel.askAssistant("Explain the theological and historical meaning of $vRef")
                        },
                        onSetFontScale = { scale -> viewModel.setFontScale(scale) }
                    )
                }

                AppTab.ASSISTANT -> {
                    AiStudyScreen(
                        state = assistantState,
                        onQueryChanged = { query -> viewModel.updateAssistantQuery(query) },
                        onSendMessage = { prompt -> viewModel.askAssistant(prompt) },
                        onJumpToScripture = { ref ->
                            viewModel.jumpToScriptureFromLesson(ref)
                        }
                    )
                }

                AppTab.GUIDES -> {
                    GuidesScreen(
                        guides = allGuides,
                        selectedGuide = selectedGuide,
                        selectedLesson = selectedLesson,
                        lessons = lessonsInSelectedGuide,
                        isLeaderModeActive = isLeaderMode,
                        onToggleLeaderMode = { active -> viewModel.toggleLeaderMode(active) },
                        onSelectGuide = { g -> viewModel.selectGuide(g) },
                        onSelectLesson = { l -> viewModel.selectLesson(l) },
                        onJumpToScripture = { ref -> viewModel.jumpToScriptureFromLesson(ref) },
                        onDiscussInGroup = { _, qNum ->
                            viewModel.selectTab(AppTab.GROUPS)
                        },
                        onCreateCustomGuide = { title, level, desc, lessons ->
                            viewModel.createCustomGuide(title, level, desc, lessons)
                        },
                        onToggleCompleteLesson = { lessonId, isComplete ->
                            viewModel.toggleLessonCompleted(lessonId, isComplete)
                        }
                    )
                }

                AppTab.GROUPS -> {
                    GroupsScreen(
                        currentGroup = activeGroup,
                        members = activeGroupMembers,
                        assignment = activeGroupAssignment,
                        completions = activeGroupCompletions,
                        discussionPosts = discussionPosts,
                        isLeaderMode = isLeaderMode,
                        onToggleLeaderMode = { active -> viewModel.toggleLeaderMode(active) },
                        onPostDiscussionReply = { content, qNum ->
                            viewModel.postDiscussionReply("lesson_foundational_1", content, qNum)
                        },
                        onCreateGroup = { name, desc, guideId, sched ->
                            viewModel.createGroup(name, desc, guideId, sched)
                        },
                        onJoinGroup = { code ->
                            viewModel.joinGroupByCode(code) { _ -> }
                        },
                        onSelectGuideToView = { guideId ->
                            val found = allGuides.find { it.id == guideId }
                            if (found != null) {
                                viewModel.selectGuide(found)
                                viewModel.selectTab(AppTab.GUIDES)
                            }
                        }
                    )
                }

                AppTab.DEVOTION -> {
                    PersonalDevotionScreen(
                        readingProgress = readingProgress,
                        notes = allNotes,
                        highlights = allHighlights,
                        bookmarks = allBookmarks,
                        onJumpToVerse = { ref -> viewModel.jumpToScriptureFromLesson(ref) },
                        onDeleteNote = { id -> viewModel.deleteNote(id) },
                        onDeleteBookmark = { id -> viewModel.deleteBookmark(id) },
                        onDeleteHighlight = { vRef -> viewModel.removeHighlight(vRef) }
                    )
                }
            }
        }
    }
}
