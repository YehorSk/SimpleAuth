package com.example.simpleauth.auth

import com.example.simpleauth.model.HttpResponse
import com.example.simpleauth.model.User
import com.example.simpleauth.ui.screens.login.LoginForm
import com.example.simpleauth.ui.screens.register.RegisterForm

interface AuthRepository {

    suspend fun register(registerForm: RegisterForm) : AuthResult<HttpResponse>

    suspend fun login(loginForm: LoginForm) : AuthResult<HttpResponse>

    suspend fun authenticate(token : String) : AuthResult<User>

}