package com.example.act3.model

import java.util.UUID

data class Event(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String = "General",
    val statusTag: String? = null,
    val attendeesTag: String? = null
)
