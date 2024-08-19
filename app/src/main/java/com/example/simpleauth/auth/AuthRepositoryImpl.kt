package com.example.simpleauth.auth

import android.util.Log
import com.example.simpleauth.data.user.AuthPreferencesRepository
import com.example.simpleauth.model.HttpResponse
import com.example.simpleauth.model.User
import com.example.simpleauth.service.AuthService
import com.example.simpleauth.ui.screens.login.LoginForm
import com.example.simpleauth.ui.screens.register.RegisterForm
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
            Log.v("REG_ATTEMPT",result.toString())
            login(LoginForm(email = registerForm.email, password = registerForm.password))
            AuthResult.Authorized(result)
        }catch (e: HttpException) {
            if (e.code() == 422) {
                val responseBody = e.response()?.errorBody()?.string() ?: ""
                Log.e("REG_ERROR", "Raw response body: $responseBody") // Log the raw response
                val httpResponse = parseHttpResponse(responseBody)
                AuthResult.Unauthorized(httpResponse)
            } else {
                AuthResult.UnknownError(HttpResponse(null, e.message(), null))
            }
        }
    }

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

    override suspend fun login(loginForm: LoginForm): AuthResult<HttpResponse> {
        val result = authService.login(loginForm)
        Log.v("REG_ATTEMPT",result.toString())
        result.data?.token?.let { prefs.saveJwtToken(it) }
        result.data?.user?.name?.let { prefs.saveUserName(it) }
        result.data?.user?.email?.let { prefs.saveUserEmail(it) }
        return AuthResult.Authorized(result)
    }

    override suspend fun authenticate(token: String): AuthResult<User> {
        return try{
            val result = authService.authenticate(token)
            if(result.message != "Unauthenticated"){
                AuthResult.Authorized(result)
            }else{
                AuthResult.Unauthorized(result)
            }
        }catch(e: Exception){
            AuthResult.UnknownError(User(message = "Unauthenticated"))
        }
    }

}