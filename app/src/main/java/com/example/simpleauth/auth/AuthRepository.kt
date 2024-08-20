package com.example.simpleauth.auth

import com.example.simpleauth.model.HttpResponse
import com.example.simpleauth.model.User
import com.example.simpleauth.ui.screens.auth.LoginForm
import com.example.simpleauth.ui.screens.auth.RegisterForm

interface AuthRepository {

    suspend fun register(registerForm: RegisterForm) : AuthResult<HttpResponse>

    suspend fun login(loginForm: LoginForm) : AuthResult<HttpResponse>

    suspend fun authenticate(token : String) : AuthResult<HttpResponse>

    suspend fun logout(token : String) : AuthResult<HttpResponse>

}