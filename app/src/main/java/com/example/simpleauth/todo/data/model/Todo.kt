package com.example.simpleauth.todo.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    val id: Int,
    @SerialName("user_id")
    val userId: Int,
    val name: String,
    val description: String,
    val priority: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
