package com.example.act3.ui

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.act3.model.CategoryItem
import com.example.act3.model.Event
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class EventViewModel(application: Application) : AndroidViewModel(application) {
    val events = mutableStateListOf<Event>()
    var selectedCategory by mutableStateOf("Todos")

    val categories = mutableStateListOf(
        CategoryItem("General", "Category", 0xFF252A36L, 0xFFFFFFFFL),
        CategoryItem("Tecnología", "Code", 0xFF1E3A5FL, 0xFF70B2FFL),
        CategoryItem("Educación", "School", 0xFF123B2BL, 0xFF4EE29BL),
        CategoryItem("Competencia", "Trophy", 0xFF3B1E5FL, 0xFFD08EFFL)
    )

    private val prefs = application.getSharedPreferences("act3_event_prefs", Context.MODE_PRIVATE)

    init {
        loadCategoriesFromPrefs()
        loadEventsFromPrefs()
    }

    fun addCategory(categoryItem: CategoryItem) {
        if (categories.none { it.name.equals(categoryItem.name, ignoreCase = true) }) {
            categories.add(categoryItem)
            saveCategoriesToPrefs()
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
            saveCategoriesToPrefs()
            saveEventsToPrefs()
        }
    }

    fun getCategoryByName(name: String): CategoryItem {
        return categories.find { it.name.equals(name, ignoreCase = true) }
            ?: CategoryItem(name, "Category", 0xFF252A36L, 0xFFFFFFFFL)
    }

    fun addEvent(event: Event) {
        events.add(0, event)
        saveEventsToPrefs()
    }

    fun updateEvent(updatedEvent: Event) {
        val index = events.indexOfFirst { it.id == updatedEvent.id }
        if (index != -1) {
            events[index] = updatedEvent
            saveEventsToPrefs()
        }
    }

    fun removeEvent(event: Event) {
        events.remove(event)
        saveEventsToPrefs()
    }

    // --- Métodos de Persistencia en SharedPreferences (JSON) ---

    private fun saveEventsToPrefs() {
        try {
            val jsonArray = JSONArray()
            events.forEach { event ->
                val jsonObj = JSONObject().apply {
                    put("id", event.id)
                    put("title", event.title)
                    put("description", event.description)
                    put("date", event.date)
                    put("location", event.location)
                    put("category", event.category)
                    put("statusTag", event.statusTag ?: "")
                    put("attendeesTag", event.attendeesTag ?: "")
                }
                jsonArray.put(jsonObj)
            }
            prefs.edit().putString("saved_events", jsonArray.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadEventsFromPrefs() {
        val jsonString = prefs.getString("saved_events", null)
        events.clear()
        if (!jsonString.isNullOrEmpty()) {
            try {
                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    events.add(
                        Event(
                            id = obj.optString("id", UUID.randomUUID().toString()),
                            title = obj.optString("title", ""),
                            description = obj.optString("description", ""),
                            date = obj.optString("date", ""),
                            location = obj.optString("location", ""),
                            category = obj.optString("category", "General"),
                            statusTag = obj.optString("statusTag").ifEmpty { null },
                            attendeesTag = obj.optString("attendeesTag").ifEmpty { null }
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun saveCategoriesToPrefs() {
        try {
            val jsonArray = JSONArray()
            categories.forEach { cat ->
                val jsonObj = JSONObject().apply {
                    put("name", cat.name)
                    put("iconKey", cat.iconKey)
                    put("bgColorHex", cat.bgColorHex)
                    put("textColorHex", cat.textColorHex)
                }
                jsonArray.put(jsonObj)
            }
            prefs.edit().putString("saved_categories", jsonArray.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadCategoriesFromPrefs() {
        val jsonString = prefs.getString("saved_categories", null)
        if (!jsonString.isNullOrEmpty()) {
            try {
                val jsonArray = JSONArray(jsonString)
                val loadedList = mutableListOf<CategoryItem>()
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    loadedList.add(
                        CategoryItem(
                            name = obj.getString("name"),
                            iconKey = obj.optString("iconKey", "Category"),
                            bgColorHex = obj.optLong("bgColorHex", 0xFF252A36L),
                            textColorHex = obj.optLong("textColorHex", 0xFFFFFFFFL)
                        )
                    )
                }
                if (loadedList.isNotEmpty()) {
                    categories.clear()
                    categories.addAll(loadedList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
