package com.example.act3.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.act3.model.Event

class EventViewModel : ViewModel() {
    val events = mutableStateListOf<Event>()
    var selectedCategory by mutableStateOf("Todos")

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
