package com.example.simpleauth.auth

import com.example.simpleauth.auth.data.model.AuthResult
import com.example.simpleauth.auth.data.model.HttpResponse
import com.example.simpleauth.ui.screens.auth.LoginForm
import com.example.simpleauth.ui.screens.auth.RegisterForm

interface AuthRepository {

    suspend fun register(registerForm: RegisterForm) : AuthResult<HttpResponse>

    suspend fun login(loginForm: LoginForm) : AuthResult<HttpResponse>

    suspend fun authenticate() : AuthResult<HttpResponse>

    suspend fun logout() : AuthResult<HttpResponse>

}