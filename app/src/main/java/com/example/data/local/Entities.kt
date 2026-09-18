package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "user_me",
    val name: String = "Devoted Reader",
    val email: String = "reader@growingdeep.org",
    val role: String = "leader", // "member" or "leader"
    val currentGroupId: String? = "group_foundations"
)

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey val id: String,
    val name: String,
    val leaderId: String,
    val joinCode: String,
    val description: String
)

@Entity(tableName = "group_members")
data class GroupMemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val groupId: String,
    val userId: String,
    val userName: String,
    val userRole: String,
    val joinedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "guides")
data class GuideEntity(
    @PrimaryKey val id: String,
    val title: String,
    val level: String, // "Foundational", "Intermediate", "Advanced"
    val description: String,
    val authorId: String = "system",
    val isPublic: Boolean = true,
    val totalLessons: Int = 15
)

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey val id: String,
    val guideId: String,
    val orderIndex: Int,
    val title: String,
    val objective: String,
    val keyVerses: List<String>,
    val teachingPoints: List<String>,
    val discussionQuestions: List<String>,
    val leaderNotes: String
)

@Entity(tableName = "group_guide_assignments")
data class GroupGuideAssignmentEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val guideId: String,
    val schedule: String,
    val currentLessonIndex: Int = 1
)

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val verseRef: String,
    val content: String,
    val visibility: String = "private", // "private" or "group"
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "highlights")
data class HighlightEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val verseRef: String,
    val colorTag: String,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: String,
    val userId: String = "user_me",
    val verseRef: String,
    val passageTitle: String,
    val isReadLater: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reading_progress")
data class ReadingProgressEntity(
    @PrimaryKey val id: String = "progress_default",
    val userId: String = "user_me",
    val planId: String = "annual_plan",
    val currentBook: String = "Romans",
    val currentChapter: Int = 8,
    val currentVerse: Int = 28,
    val streakDays: Int = 7,
    val lastReadTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "discussion_posts")
data class DiscussionPostEntity(
    @PrimaryKey val id: String,
    val lessonId: String,
    val groupId: String,
    val userId: String,
    val userName: String,
    val userRole: String,
    val content: String,
    val questionNumber: Int? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "lesson_completions")
data class LessonCompletionEntity(
    @PrimaryKey val id: String, // "$lessonId-$userId"
    val lessonId: String,
    val userId: String,
    val userName: String,
    val groupId: String,
    val completedAt: Long = System.currentTimeMillis()
)

class Converters {
    private val moshi = Moshi.Builder().build()
    private val listStringType = Types.newParameterizedType(List::class.java, String::class.java)
    private val adapter = moshi.adapter<List<String>>(listStringType)

    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        return adapter.toJson(list ?: emptyList())
    }

    @TypeConverter
    fun toStringList(json: String?): List<String> {
        return if (json.isNullOrEmpty()) emptyList() else adapter.fromJson(json) ?: emptyList()
    }
}
