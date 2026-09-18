package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.AiChatMessage
import com.example.data.ai.AiStudyRepository
import com.example.data.bible.BibleRepository
import com.example.data.local.*
import com.example.data.model.*
import com.example.data.repository.AppRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppTab(val title: String) {
    READER("Bible"),
    ASSISTANT("AI Study"),
    GUIDES("Guides"),
    GROUPS("Groups"),
    DEVOTION("My Notes")
}

data class BibleReaderUiState(
    val currentBook: String = "Romans",
    val currentChapter: Int = 8,
    val translation: Translation = Translation.BSB,
    val verses: List<BibleVerse> = emptyList(),
    val selectedVerse: BibleVerse? = null,
    val parallelVerses: Map<Translation, String> = emptyMap(),
    val crossReferences: List<CrossReference> = emptyList(),
    val notesForSelectedVerse: List<NoteEntity> = emptyList(),
    val highlightForSelectedVerse: HighlightEntity? = null,
    val isWordStudyModeActive: Boolean = false,
    val selectedWordStudy: WordStudyEntry? = null,
    val fontScale: Float = 1.0f // Accessible reading scale
)

data class AiAssistantUiState(
    val currentPassageRef: String = "Romans 8:28",
    val currentPassageText: String = "And we know that God causes all things to work together for good to those who love God, to those who are called according to His purpose.",
    val messages: List<AiChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val queryInput: String = ""
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = AppRepository(db.appDao())

    // Active Tab Navigation
    private val _activeTab = MutableStateFlow(AppTab.READER)
    val activeTab: StateFlow<AppTab> = _activeTab.asStateFlow()

    fun selectTab(tab: AppTab) {
        _activeTab.value = tab
    }

    // User & Leader Mode State
    val currentUser: StateFlow<UserEntity?> = repository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _isLeaderModeActive = MutableStateFlow(true)
    val isLeaderModeActive: StateFlow<Boolean> = _isLeaderModeActive.asStateFlow()

    fun toggleLeaderMode(active: Boolean) {
        _isLeaderModeActive.value = active
        viewModelScope.launch {
            repository.setUserRole(if (active) "leader" else "member")
        }
    }

    // Bible Reader State
    private val _readerState = MutableStateFlow(BibleReaderUiState())
    val readerState: StateFlow<BibleReaderUiState> = _readerState.asStateFlow()

    val allHighlights: StateFlow<List<HighlightEntity>> = repository.allHighlights
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotes: StateFlow<List<NoteEntity>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBookmarks: StateFlow<List<BookmarkEntity>> = repository.allBookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val readingProgress: StateFlow<ReadingProgressEntity?> = repository.readingProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Guides State
    val allGuides: StateFlow<List<GuideEntity>> = repository.allGuides
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedGuide = MutableStateFlow<GuideEntity?>(null)
    val selectedGuide: StateFlow<GuideEntity?> = _selectedGuide.asStateFlow()

    private val _selectedLesson = MutableStateFlow<LessonEntity?>(null)
    val selectedLesson: StateFlow<LessonEntity?> = _selectedLesson.asStateFlow()

    // Groups State
    val allGroups: StateFlow<List<GroupEntity>> = repository.allGroups
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeGroupId = MutableStateFlow("group_grace_fellowship")
    val activeGroupId: StateFlow<String> = _activeGroupId.asStateFlow()

    val activeGroup: StateFlow<GroupEntity?> = _activeGroupId
        .flatMapLatest { repository.getGroupById(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeGroupMembers: StateFlow<List<GroupMemberEntity>> = _activeGroupId
        .flatMapLatest { repository.getMembersForGroup(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeGroupAssignment: StateFlow<GroupGuideAssignmentEntity?> = _activeGroupId
        .flatMapLatest { repository.getAssignmentForGroup(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeGroupCompletions: StateFlow<List<LessonCompletionEntity>> = _activeGroupId
        .flatMapLatest { repository.getCompletionsForGroup(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // AI Study Assistant State
    private val _assistantState = MutableStateFlow(AiAssistantUiState())
    val assistantState: StateFlow<AiAssistantUiState> = _assistantState.asStateFlow()

    // UI Privacy banner toggle
    private val _showPrivacyBanner = MutableStateFlow(true)
    val showPrivacyBanner: StateFlow<Boolean> = _showPrivacyBanner.asStateFlow()

    fun dismissPrivacyBanner() {
        _showPrivacyBanner.value = false
    }

    init {
        loadChapter("Romans", 8, Translation.BSB)
        // Initial sample assistant response
        viewModelScope.launch {
            val initial = AiStudyRepository.askAssistant(
                "Romans 8:28",
                "And we know that God causes all things to work together for good to those who love God, to those who are called according to His purpose.",
                "What does it mean that all things work together for good?"
            )
            _assistantState.update { it.copy(messages = listOf(initial)) }
        }
    }

    // Reader Operations
    fun loadChapter(book: String, chapter: Int, translation: Translation = _readerState.value.translation) {
        val verses = BibleRepository.getVerses(book, chapter, translation)
        _readerState.update {
            it.copy(
                currentBook = book,
                currentChapter = chapter,
                translation = translation,
                verses = verses,
                selectedVerse = null,
                parallelVerses = emptyMap(),
                crossReferences = emptyList()
            )
        }
        viewModelScope.launch {
            repository.updateReadingPosition(book, chapter, 1)
        }
    }

    fun setTranslation(translation: Translation) {
        loadChapter(_readerState.value.currentBook, _readerState.value.currentChapter, translation)
    }

    fun setFontScale(scale: Float) {
        _readerState.update { it.copy(fontScale = scale.coerceIn(0.85f, 1.4f)) }
    }

    fun toggleWordStudyMode() {
        _readerState.update { it.copy(isWordStudyModeActive = !it.isWordStudyModeActive) }
    }

    fun selectVerse(verse: BibleVerse) {
        val parallel = BibleRepository.getParallelTranslations(verse.book, verse.chapter, verse.verse)
        val crossRefs = BibleRepository.getCrossReferencesForVerse(verse.reference)

        viewModelScope.launch {
            val notes = repository.getNotesForVerse(verse.reference).first()
            val highlight = repository.getHighlightForVerse(verse.reference).first()

            _readerState.update {
                it.copy(
                    selectedVerse = verse,
                    parallelVerses = parallel,
                    crossReferences = crossRefs,
                    notesForSelectedVerse = notes,
                    highlightForSelectedVerse = highlight
                )
            }

            // Sync AI context
            _assistantState.update {
                it.copy(
                    currentPassageRef = verse.reference,
                    currentPassageText = verse.text
                )
            }
        }
    }

    fun dismissVerseDetail() {
        _readerState.update { it.copy(selectedVerse = null) }
    }

    fun inspectWord(wordRaw: String) {
        val cleanWord = wordRaw.lowercase().replace(Regex("[^a-z]"), "")
        val entry = BibleRepository.wordStudyDictionary[cleanWord]
        _readerState.update { it.copy(selectedWordStudy = entry) }
    }

    fun dismissWordStudy() {
        _readerState.update { it.copy(selectedWordStudy = null) }
    }

    // Highlights & Notes
    fun addHighlight(verseRef: String, tag: HighlightTag) {
        viewModelScope.launch {
            repository.setHighlight(verseRef, tag.label)
            // Refresh currently selected verse highlight state
            val updated = repository.getHighlightForVerse(verseRef).first()
            _readerState.update { it.copy(highlightForSelectedVerse = updated) }
        }
    }

    fun removeHighlight(verseRef: String) {
        viewModelScope.launch {
            repository.removeHighlight(verseRef)
            _readerState.update { it.copy(highlightForSelectedVerse = null) }
        }
    }

    fun addNote(verseRef: String, content: String, visibility: String = "private") {
        if (content.isBlank()) return
        viewModelScope.launch {
            repository.saveNote(verseRef, content.trim(), visibility)
            val updated = repository.getNotesForVerse(verseRef).first()
            _readerState.update { it.copy(notesForSelectedVerse = updated) }
        }
    }

    fun deleteNote(id: String, verseRef: String? = null) {
        viewModelScope.launch {
            repository.deleteNote(id)
            if (verseRef != null) {
                val updated = repository.getNotesForVerse(verseRef).first()
                _readerState.update { it.copy(notesForSelectedVerse = updated) }
            }
        }
    }

    fun bookmarkCurrentPassage(verseRef: String, passageTitle: String, isReadLater: Boolean = false) {
        viewModelScope.launch {
            repository.toggleBookmark(verseRef, passageTitle, isReadLater)
        }
    }

    fun deleteBookmark(id: String) {
        viewModelScope.launch {
            repository.deleteBookmark(id)
        }
    }

    // AI Study Assistant
    fun updateAssistantQuery(text: String) {
        _assistantState.update { it.copy(queryInput = text) }
    }

    fun askAssistant(overridePrompt: String? = null) {
        val query = overridePrompt ?: _assistantState.value.queryInput
        if (query.isBlank()) return

        val userMessage = AiChatMessage(
            isFromUser = true,
            userText = query,
            passageContext = _assistantState.value.currentPassageRef
        )

        _assistantState.update {
            it.copy(
                messages = it.messages + userMessage,
                isLoading = true,
                queryInput = ""
            )
        }

        viewModelScope.launch {
            val response = AiStudyRepository.askAssistant(
                _assistantState.value.currentPassageRef,
                _assistantState.value.currentPassageText,
                query
            )
            _assistantState.update {
                it.copy(
                    messages = it.messages + response,
                    isLoading = false
                )
            }
        }
    }

    // Guides & Lessons
    fun selectGuide(guide: GuideEntity?) {
        _selectedGuide.value = guide
        _selectedLesson.value = null
    }

    fun selectLesson(lesson: LessonEntity?) {
        _selectedLesson.value = lesson
    }

    fun getLessonsForSelectedGuide(): Flow<List<LessonEntity>> {
        val guideId = _selectedGuide.value?.id ?: return emptyFlow()
        return repository.getLessonsForGuide(guideId)
    }

    fun jumpToScriptureFromLesson(verseRef: String) {
        // e.g. "Romans 8:28" or "1 Corinthians 15:1-4"
        val parts = verseRef.split(" ")
        if (parts.size >= 2) {
            val book = if (parts.size == 3) "${parts[0]} ${parts[1]}" else parts[0]
            val chapterVerse = parts.last()
            val chapter = chapterVerse.split(":").firstOrNull()?.toIntOrNull() ?: 1
            loadChapter(book, chapter)
            selectTab(AppTab.READER)
        }
    }

    fun createCustomGuide(title: String, level: String, description: String, lessons: List<LessonEntity>) {
        viewModelScope.launch {
            val guideId = repository.createGuide(title, level, description, lessons)
            val newGuide = repository.getGuideById(guideId)
            _selectedGuide.value = newGuide
        }
    }

    // Church / Groups
    fun selectGroup(groupId: String) {
        _activeGroupId.value = groupId
    }

    fun createGroup(name: String, description: String, assignedGuideId: String, schedule: String) {
        viewModelScope.launch {
            val newGroupId = repository.createGroup(name, description, assignedGuideId, schedule)
            _activeGroupId.value = newGroupId
        }
    }

    fun joinGroupByCode(code: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val group = repository.joinGroupByCode(code)
            if (group != null) {
                _activeGroupId.value = group.id
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun getDiscussionPostsForLesson(lessonId: String): Flow<List<DiscussionPostEntity>> {
        return repository.getDiscussionPosts(lessonId, _activeGroupId.value)
    }

    fun postDiscussionReply(lessonId: String, content: String, questionNumber: Int? = null) {
        if (content.isBlank()) return
        val user = currentUser.value
        viewModelScope.launch {
            repository.addDiscussionPost(
                lessonId = lessonId,
                groupId = _activeGroupId.value,
                content = content.trim(),
                questionNumber = questionNumber,
                userName = user?.name ?: "Devoted Reader (You)",
                userRole = if (_isLeaderModeActive.value) "Leader" else "Member"
            )
        }
    }

    fun toggleLessonCompleted(lessonId: String, isCompleted: Boolean) {
        val user = currentUser.value
        viewModelScope.launch {
            repository.toggleLessonCompletion(
                lessonId = lessonId,
                groupId = _activeGroupId.value,
                userId = user?.id ?: "user_me",
                userName = user?.name ?: "Devoted Reader",
                isCompleted = isCompleted
            )
        }
    }
}
