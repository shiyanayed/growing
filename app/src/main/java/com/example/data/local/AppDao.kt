package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // User
    @Query("SELECT * FROM users WHERE id = 'user_me' LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: UserEntity)

    // Guides
    @Query("SELECT * FROM guides ORDER BY level ASC, title ASC")
    fun getAllGuides(): Flow<List<GuideEntity>>

    @Query("SELECT * FROM guides WHERE id = :id LIMIT 1")
    suspend fun getGuideById(id: String): GuideEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGuides(guides: List<GuideEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGuide(guide: GuideEntity)

    // Lessons
    @Query("SELECT * FROM lessons WHERE guideId = :guideId ORDER BY orderIndex ASC")
    fun getLessonsForGuide(guideId: String): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE id = :lessonId LIMIT 1")
    suspend fun getLessonById(lessonId: String): LessonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: LessonEntity)

    // Groups
    @Query("SELECT * FROM groups")
    fun getAllGroups(): Flow<List<GroupEntity>>

    @Query("SELECT * FROM groups WHERE id = :id LIMIT 1")
    fun getGroupById(id: String): Flow<GroupEntity?>

    @Query("SELECT * FROM groups WHERE joinCode = :code LIMIT 1")
    suspend fun getGroupByCode(code: String): GroupEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity)

    // Group Members
    @Query("SELECT * FROM group_members WHERE groupId = :groupId ORDER BY joinedAt ASC")
    fun getMembersForGroup(groupId: String): Flow<List<GroupMemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: GroupMemberEntity)

    // Group Assignments
    @Query("SELECT * FROM group_guide_assignments WHERE groupId = :groupId LIMIT 1")
    fun getAssignmentForGroup(groupId: String): Flow<GroupGuideAssignmentEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: GroupGuideAssignmentEntity)

    // Notes
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE verseRef = :verseRef ORDER BY updatedAt DESC")
    fun getNotesForVerse(verseRef: String): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: String)

    // Highlights
    @Query("SELECT * FROM highlights ORDER BY updatedAt DESC")
    fun getAllHighlights(): Flow<List<HighlightEntity>>

    @Query("SELECT * FROM highlights WHERE verseRef = :verseRef LIMIT 1")
    fun getHighlightForVerse(verseRef: String): Flow<HighlightEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighlight(highlight: HighlightEntity)

    @Query("DELETE FROM highlights WHERE id = :id")
    suspend fun deleteHighlight(id: String)

    @Query("DELETE FROM highlights WHERE verseRef = :verseRef")
    suspend fun deleteHighlightByVerse(verseRef: String)

    // Bookmarks
    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmark(id: String)

    // Reading Progress
    @Query("SELECT * FROM reading_progress WHERE id = 'progress_default' LIMIT 1")
    fun getReadingProgress(): Flow<ReadingProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReadingProgress(progress: ReadingProgressEntity)

    // Discussion Posts
    @Query("SELECT * FROM discussion_posts WHERE lessonId = :lessonId AND groupId = :groupId ORDER BY createdAt ASC")
    fun getDiscussionPosts(lessonId: String, groupId: String): Flow<List<DiscussionPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscussionPost(post: DiscussionPostEntity)

    // Lesson Completions
    @Query("SELECT * FROM lesson_completions WHERE groupId = :groupId")
    fun getCompletionsForGroup(groupId: String): Flow<List<LessonCompletionEntity>>

    @Query("SELECT * FROM lesson_completions WHERE lessonId = :lessonId")
    fun getCompletionsForLesson(lessonId: String): Flow<List<LessonCompletionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletion(completion: LessonCompletionEntity)

    @Query("DELETE FROM lesson_completions WHERE id = :id")
    suspend fun deleteCompletion(id: String)
}
