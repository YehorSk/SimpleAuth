package com.example.simpleauth.auth

import android.util.Log
import com.example.simpleauth.auth.data.model.AuthResult
import com.example.simpleauth.auth.data.model.HttpResponse
import com.example.simpleauth.service.AuthService
import com.example.simpleauth.ui.screens.auth.LoginForm
import com.example.simpleauth.ui.screens.auth.RegisterForm
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val prefs: AuthPreferencesRepository
) : AuthRepository {

    override suspend fun register(registerForm: RegisterForm): AuthResult<HttpResponse> {
        return try{
            val result = authService.register(registerForm)
            login(LoginForm(email = registerForm.email, password = registerForm.password))
            AuthResult.Authorized(result)
        }catch (e: HttpException) {
            if (e.code() == 422) {
                val responseBody = e.response()?.errorBody()?.string() ?: ""
                val httpResponse = parseHttpResponse(responseBody)
                AuthResult.Unauthorized(httpResponse)
            } else {
                AuthResult.UnknownError(HttpResponse(null, e.message(), null))
            }
        }
    }

    private fun parseHttpResponse(responseBody: String): HttpResponse {
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

    override suspend fun login(loginForm: LoginForm): AuthResult<HttpResponse> {
        return try{
            val result = authService.login(loginForm)
            prefs.saveUser(result)
            AuthResult.Authorized(result)
        }catch (e: HttpException) {
            if (e.code() == 422) {
                val responseBody = e.response()?.errorBody()?.string() ?: ""
                val httpResponse = parseHttpResponse(responseBody)
                AuthResult.Unauthorized(httpResponse)
            } else {
                AuthResult.UnknownError(HttpResponse(null, e.message(), null))
            }
        }
    }

    override suspend fun authenticate(): AuthResult<HttpResponse> {
        return try{
            val token = prefs.jwtTokenFlow.first()
            if (token!!.isBlank()) {
                return AuthResult.Unauthorized(HttpResponse(message = "Unauthenticated"))
            }
            val result = authService.authenticate("Bearer $token")
            AuthResult.Authorized(result)
        }catch (e: HttpException) {
            if (e.code() == 401) {
                prefs.clearAllTokens()
                AuthResult.Unauthorized(HttpResponse(message = e.message()))
            } else {
                AuthResult.UnknownError(HttpResponse(message = e.code().toString()))
            }
        }
    }

    override suspend fun logout(): AuthResult<HttpResponse> {
        return try{
            val token = prefs.jwtTokenFlow.first()
            val result = authService.logout("Bearer $token")
            prefs.clearAllTokens()
            AuthResult.Unauthorized(result)
        }catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized(HttpResponse(message = e.message()))
            } else {
                AuthResult.UnknownError(HttpResponse(message = e.code().toString()))
            }
        }
    }

}