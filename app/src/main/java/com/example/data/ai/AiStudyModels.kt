package com.example.data.ai

import androidx.compose.ui.graphics.Color
import java.util.UUID

enum class NuanceTag(val label: String, val badgeColor: Long, val containerColor: Long, val borderColor: Long) {
    SCRIPTURE_SAYS(
        label = "Scripture says",
        badgeColor = 0xFF1B5E20,
        containerColor = 0xFFF1F8E9,
        borderColor = 0xFFAED581
    ),
    THEOLOGICAL_INTERPRETATION(
        label = "Theological interpretation",
        badgeColor = 0xFF0D47A1,
        containerColor = 0xFFE3F2FD,
        borderColor = 0xFF90CAF9
    ),
    DEBATED_AMONG_TRADITIONS(
        label = "Debated among traditions",
        badgeColor = 0xFFB26A00,
        containerColor = 0xFFFFF8E1,
        borderColor = 0xFFFFD54F
    );

    val badgeComposeColor: Color get() = Color(badgeColor)
    val containerComposeColor: Color get() = Color(containerColor)
    val borderComposeColor: Color get() = Color(borderColor)
}

data class AiResponseSection(
    val tag: NuanceTag,
    val title: String,
    val content: String,
    val citedVerses: List<String>
)

data class AiChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val isFromUser: Boolean,
    val userText: String = "",
    val passageContext: String = "",
    val sections: List<AiResponseSection> = emptyList(),
    val rawText: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
