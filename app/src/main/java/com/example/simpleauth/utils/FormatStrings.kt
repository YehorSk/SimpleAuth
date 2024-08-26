package com.example.simpleauth.utils

import android.util.Log
import com.example.simpleauth.auth.data.model.HttpResponse
import kotlinx.serialization.json.Json

fun cleanError(error: String?) = error?.replace("[", "")?.replace("]", "").orEmpty()

fun parseHttpResponse(responseBody: String): HttpResponse {
    return try {
        if (responseBody.isBlank()) {
            Log.e("PARSE_ERROR", "Response body is blank or empty")
            HttpResponse(status = 422, message = "Empty response", data = null, errors = null)
        } else {
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<HttpResponse>(responseBody)
        }
    } catch (e: Exception) {
        Log.e("PARSE_ERROR", "Error parsing HttpResponse", e)
        HttpResponse(status = 422, message = "Invalid response format", data = null, errors = null)
    }
}