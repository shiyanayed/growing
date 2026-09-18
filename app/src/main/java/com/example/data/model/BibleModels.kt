package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class Translation(val code: String, val displayName: String, val description: String) {
    BSB("BSB", "Berean Standard Bible", "Modern, accurate, and wonderfully readable (Public Domain)"),
    WEB("WEB", "World English Bible", "Clear modern English in public domain"),
    KJV("KJV", "King James Version", "Classic historic prose (Public Domain)")
}

data class BibleVerse(
    val book: String,
    val chapter: Int,
    val verse: Int,
    val text: String,
    val translation: Translation = Translation.BSB
) {
    val reference: String get() = "$book $chapter:$verse"
}

data class BibleBook(
    val name: String,
    val testament: String, // "Old Testament" or "New Testament"
    val chapterCount: Int
)

data class CrossReference(
    val sourceRef: String,
    val targetRef: String,
    val previewText: String
)

data class WordStudyEntry(
    val englishWord: String,
    val originalWord: String,
    val language: String, // "Greek" or "Hebrew"
    val transliteration: String,
    val strongsNumber: String,
    val pronunciation: String,
    val definition: String,
    val theologicalSignificance: String
)

enum class HighlightTag(val label: String, val colorHex: Long) {
    PROMISES("Promises", 0xFFFFE082),
    COMMANDS("Commands", 0xFFFFAB91),
    QUESTIONS("Questions to Research", 0xFF80DEEA),
    PRAISE("Praise & Worship", 0xFFC5E1A5),
    IDENTITY("Identity in Christ", 0xFFCE93D8),
    REFLECTIONS("Personal Reflection", 0xFFFFCC80);

    val composeColor: Color get() = Color(colorHex)
}
