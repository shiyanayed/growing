package com.example.data.repository

import com.example.data.local.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class AppRepository(private val dao: AppDao) {

    // User
    val currentUser: Flow<UserEntity?> = dao.getCurrentUser()

    suspend fun setUserRole(role: String) {
        val current = UserEntity(
            id = "user_me",
            name = "Devoted Reader",
            email = "reader@growingdeep.org",
            role = role,
            currentGroupId = "group_grace_fellowship"
        )
        dao.upsertUser(current)
    }

    // Guides
    val allGuides: Flow<List<GuideEntity>> = dao.getAllGuides()

    suspend fun getGuideById(id: String): GuideEntity? = dao.getGuideById(id)

    fun getLessonsForGuide(guideId: String): Flow<List<LessonEntity>> =
        dao.getLessonsForGuide(guideId)

    suspend fun getLessonById(id: String): LessonEntity? = dao.getLessonById(id)

    suspend fun createGuide(
        title: String,
        level: String,
        description: String,
        lessons: List<LessonEntity>
    ): String {
        val guideId = "guide_${UUID.randomUUID().toString().take(8)}"
        val guide = GuideEntity(
            id = guideId,
            title = title,
            level = level,
            description = description,
            authorId = "user_me",
            isPublic = false,
            totalLessons = lessons.size
        )
        dao.insertGuide(guide)
        val preparedLessons = lessons.mapIndexed { index, lesson ->
            lesson.copy(
                id = "lesson_${guideId}_${index + 1}",
                guideId = guideId,
                orderIndex = index + 1
            )
        }
        dao.insertLessons(preparedLessons)
        return guideId
    }

    // Groups
    val allGroups: Flow<List<GroupEntity>> = dao.getAllGroups()

    fun getGroupById(groupId: String): Flow<GroupEntity?> = dao.getGroupById(groupId)

    fun getMembersForGroup(groupId: String): Flow<List<GroupMemberEntity>> =
        dao.getMembersForGroup(groupId)

    fun getAssignmentForGroup(groupId: String): Flow<GroupGuideAssignmentEntity?> =
        dao.getAssignmentForGroup(groupId)

    suspend fun createGroup(name: String, description: String, assignedGuideId: String, schedule: String): String {
        val groupId = "group_${UUID.randomUUID().toString().take(8)}"
        val joinCode = "DEEP-${(1000..9999).random()}"
        val group = GroupEntity(
            id = groupId,
            name = name,
            leaderId = "user_me",
            joinCode = joinCode,
            description = description
        )
        dao.insertGroup(group)

        // Add creator as Leader
        dao.insertMember(
            GroupMemberEntity(
                groupId = groupId,
                userId = "user_me",
                userName = "Devoted Reader (You)",
                userRole = "Leader"
            )
        )

        // Assign guide
        dao.insertAssignment(
            GroupGuideAssignmentEntity(
                id = "assign_$groupId",
                groupId = groupId,
                guideId = assignedGuideId,
                schedule = schedule,
                currentLessonIndex = 1
            )
        )

        return groupId
    }

    suspend fun joinGroupByCode(code: String): GroupEntity? {
        val group = dao.getGroupByCode(code.trim().uppercase()) ?: return null
        dao.insertMember(
            GroupMemberEntity(
                groupId = group.id,
                userId = "user_me",
                userName = "Devoted Reader (You)",
                userRole = "Member"
            )
        )
        return group
    }

    // Notes
    val allNotes: Flow<List<NoteEntity>> = dao.getAllNotes()

    fun getNotesForVerse(verseRef: String): Flow<List<NoteEntity>> =
        dao.getNotesForVerse(verseRef)

    suspend fun saveNote(verseRef: String, content: String, visibility: String = "private") {
        val note = NoteEntity(
            id = "note_${UUID.randomUUID().toString().take(8)}",
            userId = "user_me",
            verseRef = verseRef,
            content = content,
            visibility = visibility,
            updatedAt = System.currentTimeMillis()
        )
        dao.insertNote(note)
    }

    suspend fun deleteNote(id: String) = dao.deleteNote(id)

    // Highlights
    val allHighlights: Flow<List<HighlightEntity>> = dao.getAllHighlights()

    fun getHighlightForVerse(verseRef: String): Flow<HighlightEntity?> =
        dao.getHighlightForVerse(verseRef)

    suspend fun setHighlight(verseRef: String, colorTag: String) {
        val highlight = HighlightEntity(
            id = "hl_${verseRef.replace(" ", "_").replace(":", "_")}",
            userId = "user_me",
            verseRef = verseRef,
            colorTag = colorTag,
            updatedAt = System.currentTimeMillis()
        )
        dao.insertHighlight(highlight)
    }

    suspend fun removeHighlight(verseRef: String) {
        dao.deleteHighlightByVerse(verseRef)
    }

    // Bookmarks
    val allBookmarks: Flow<List<BookmarkEntity>> = dao.getAllBookmarks()

    suspend fun toggleBookmark(verseRef: String, passageTitle: String, isReadLater: Boolean = false) {
        val id = "bm_${verseRef.replace(" ", "_").replace(":", "_")}"
        val bookmark = BookmarkEntity(
            id = id,
            verseRef = verseRef,
            passageTitle = passageTitle,
            isReadLater = isReadLater
        )
        dao.insertBookmark(bookmark)
    }

    suspend fun deleteBookmark(id: String) = dao.deleteBookmark(id)

    // Reading Progress
    val readingProgress: Flow<ReadingProgressEntity?> = dao.getReadingProgress()

    suspend fun updateReadingPosition(book: String, chapter: Int, verse: Int) {
        val current = ReadingProgressEntity(
            id = "progress_default",
            userId = "user_me",
            planId = "annual_plan",
            currentBook = book,
            currentChapter = chapter,
            currentVerse = verse,
            streakDays = 12,
            lastReadTimestamp = System.currentTimeMillis()
        )
        dao.updateReadingProgress(current)
    }

    // Discussion Posts
    fun getDiscussionPosts(lessonId: String, groupId: String): Flow<List<DiscussionPostEntity>> =
        dao.getDiscussionPosts(lessonId, groupId)

    suspend fun addDiscussionPost(
        lessonId: String,
        groupId: String,
        content: String,
        questionNumber: Int? = null,
        userName: String = "Devoted Reader (You)",
        userRole: String = "Leader"
    ) {
        val post = DiscussionPostEntity(
            id = "post_${UUID.randomUUID().toString().take(8)}",
            lessonId = lessonId,
            groupId = groupId,
            userId = "user_me",
            userName = userName,
            userRole = userRole,
            content = content,
            questionNumber = questionNumber,
            createdAt = System.currentTimeMillis()
        )
        dao.insertDiscussionPost(post)
    }

    // Lesson Completions
    fun getCompletionsForGroup(groupId: String): Flow<List<LessonCompletionEntity>> =
        dao.getCompletionsForGroup(groupId)

    fun getCompletionsForLesson(lessonId: String): Flow<List<LessonCompletionEntity>> =
        dao.getCompletionsForLesson(lessonId)

    suspend fun toggleLessonCompletion(
        lessonId: String,
        groupId: String,
        userId: String = "user_me",
        userName: String = "Devoted Reader",
        isCompleted: Boolean
    ) {
        val id = "$lessonId-$userId"
        if (isCompleted) {
            dao.insertCompletion(
                LessonCompletionEntity(
                    id = id,
                    lessonId = lessonId,
                    userId = userId,
                    userName = userName,
                    groupId = groupId
                )
            )
        } else {
            dao.deleteCompletion(id)
        }
    }
}
