package com.example.simpleauth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HttpResponse(
    val status: Int? = null,
    val message: String?,
    val data: Data? = null,
    val errors: Errors? = null
)

@Serializable
data class Data(
    val user: User? = null,
    val token: String? = null,
    val message: String? = ""
)

@Serializable
data class User(
    val name: String? = "",
    val email: String? = "",
    @SerialName("email_verified_at")
    val emailVerifiedAt: String? = "",
    @SerialName("updated_at")
    val updatedAt: String? = "",
    @SerialName("created_at")
    val createdAt: String? = "",
    val id: Int? = 0,
    val message: String? = ""
)

@Serializable
data class Errors(
    val email: List<String>? = listOf(),
    val password: List<String>? = listOf(),
    val name: List<String>? = listOf(),
    val passwordConfirmation: List<String>? = listOf()
)

