package com.example.act3.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.act3.model.CategoryItem
import com.example.act3.model.Event

class EventViewModel : ViewModel() {
    val events = mutableStateListOf<Event>()
    var selectedCategory by mutableStateOf("Todos")

    val categories = mutableStateListOf(
        CategoryItem("General", "Category", 0xFF252A36L, 0xFFFFFFFFL),
        CategoryItem("Tecnología", "Code", 0xFF1E3A5FL, 0xFF70B2FFL),
        CategoryItem("Educación", "School", 0xFF123B2BL, 0xFF4EE29BL),
        CategoryItem("Competencia", "Trophy", 0xFF3B1E5FL, 0xFFD08EFFL)
    )

    fun addCategory(categoryItem: CategoryItem) {
        if (categories.none { it.name.equals(categoryItem.name, ignoreCase = true) }) {
            categories.add(categoryItem)
        }
    }

    fun removeCategory(categoryName: String) {
        if (!categoryName.equals("General", ignoreCase = true)) {
            categories.removeAll { it.name.equals(categoryName, ignoreCase = true) }
            events.forEachIndexed { index, event ->
                if (event.category.equals(categoryName, ignoreCase = true)) {
                    events[index] = event.copy(category = "General")
                }
            }
            if (selectedCategory.equals(categoryName, ignoreCase = true)) {
                selectedCategory = "Todos"
            }
        }
    }

    fun getCategoryByName(name: String): CategoryItem {
        return categories.find { it.name.equals(name, ignoreCase = true) }
            ?: CategoryItem(name, "Category", 0xFF252A36L, 0xFFFFFFFFL)
    }

    fun addEvent(event: Event) {
        events.add(0, event)
    }

    fun updateEvent(updatedEvent: Event) {
        val index = events.indexOfFirst { it.id == updatedEvent.id }
        if (index != -1) {
            events[index] = updatedEvent
        }
    }

    fun removeEvent(event: Event) {
        events.remove(event)
    }
}
