package com.example.act3.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryItem(
    val name: String,
    val iconKey: String = "Category",
    val bgColorHex: Long = 0xFF252A36L,
    val textColorHex: Long = 0xFFFFFFFFL
) {
    fun getIcon(): ImageVector {
        return when (iconKey) {
            "Code" -> Icons.Default.Code
            "School" -> Icons.Default.School
            "Trophy" -> Icons.Default.EmojiEvents
            "Work" -> Icons.Default.Work
            "Music" -> Icons.Default.MusicNote
            "Fitness" -> Icons.Default.FitnessCenter
            "Palette" -> Icons.Default.Palette
            "Flight" -> Icons.Default.Flight
            "Fastfood" -> Icons.Default.Fastfood
            "Star" -> Icons.Default.Star
            else -> Icons.Default.Category
        }
    }

    val bgColor: Color get() = Color(bgColorHex)
    val textColor: Color get() = Color(textColorHex)
}
