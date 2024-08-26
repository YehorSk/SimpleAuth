package com.example.simpleauth.ui.screens.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleauth.auth.data.repository.AuthRepository
import com.example.simpleauth.auth.data.model.AuthResult
import com.example.simpleauth.auth.data.model.AuthState
import com.example.simpleauth.auth.data.repository.AuthPreferencesRepository
import com.example.simpleauth.auth.data.model.HttpResponse
import com.example.simpleauth.utils.ConnectivityRepository
import com.example.simpleauth.utils.cleanError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    val authRepository: AuthRepository
) : ViewModel() {

    val state by mutableStateOf(AuthState())

    var regItemUiState by mutableStateOf(RegItemUiState())
        private set

    var regItemErrorUiState by mutableStateOf(RegErrorsItemUiState())
        private set

    var logItemUiState by mutableStateOf(LogItemUiState())
        private set

    var logItemErrorUiState by mutableStateOf(LogErrorItemUiState())
        private set

    private val resultChannel = Channel<AuthResult<HttpResponse>>()
    val authResults = resultChannel.receiveAsFlow()

    init {
        authenticate()
    }

    fun updateRegUiState(registerForm: RegisterForm){
        regItemUiState = RegItemUiState(itemDetails = registerForm, isEntryValid = validateRegInput(registerForm))
    }

    fun updateRegErrorUiState(registerForm: RegisterFormErrors){
        regItemErrorUiState = RegErrorsItemUiState(itemDetails = registerForm)
    }

    private fun validateRegInput(uiState: RegisterForm = regItemUiState.itemDetails): Boolean{
        return with(uiState){
            name.isNotBlank() && email.isNotBlank() && password.isNotBlank() && passwordConfirm.isNotBlank()
        }
    }

    fun updateLogUiState(loginForm: LoginForm){
        logItemUiState = LogItemUiState(itemDetails = loginForm, isEntryValid = validateLogInput(loginForm))
    }

    fun updateLogErrorUiState(loginForm: LoginFormErrors){
        logItemErrorUiState = LogErrorItemUiState(itemDetails = loginForm)
    }

    private fun validateLogInput(uiState: LoginForm = logItemUiState.itemDetails): Boolean{
        return with(uiState){
            email.isNotBlank() && password.isNotBlank()
        }
    }

    fun login(){
        viewModelScope.launch {
            state.isLoading = true
            val result = authRepository.login(loginForm = logItemUiState.itemDetails)
            resultChannel.send(result)
            state.isLoading = false
            when(result){
                is AuthResult.Authorized -> {
                    Log.v("Authorized", result.data.toString())
                    updateLogErrorUiState(
                        LoginFormErrors(
                            email = "",
                            password = ""
                        )
                    )
                }
                is AuthResult.Unauthorized -> {
                    Log.v("Unauthorized", result.data.toString())
                    updateLogErrorUiState(
                        LoginFormErrors(
                            email = cleanError(result.data?.errors?.email.toString()),
                            password = cleanError(result.data?.errors?.password.toString())
                        )
                    )
                }
                is AuthResult.UnknownError -> {
                    Log.v("UnknownError", result.data.toString())
                }
            }
        }
    }

    fun register(){
        viewModelScope.launch {
            state.isLoading = true
            val result = authRepository.register(registerForm = regItemUiState.itemDetails)
            resultChannel.send(result)
            when(result){
                is AuthResult.Authorized -> {
                    Log.v("Authorized", result.data.toString())
                    updateRegErrorUiState(
                        RegisterFormErrors(
                            name = "",
                            email = "",
                            password = "",
                            passwordConfirm = ""
                        )
                    )
                }
                is AuthResult.Unauthorized -> {
                    Log.v("Unauthorized", result.data.toString())
                    updateRegErrorUiState(
                        RegisterFormErrors(
                            name = cleanError(result.data?.errors?.name.toString()),
                            email = cleanError(result.data?.errors?.email.toString()),
                            password = cleanError(result.data?.errors?.password.toString()),
                            passwordConfirm = cleanError(result.data?.errors?.passwordConfirmation.toString())
                        )
                    )
                }
                is AuthResult.UnknownError -> {
                    Log.v("UnknownError", result.data.toString())
                }
            }
            state.isLoading = false
        }
    }

    private fun authenticate(){
        viewModelScope.launch {
            state.isLoading = true
            val result = authRepository.authenticate()
            resultChannel.send(result)
            state.isLoading = false
        }
    }

    fun logout(){
        viewModelScope.launch {
            state.isLoading = true
            val result = authRepository.logout()
            resultChannel.send(result)
            state.isLoading = false
        }
    }

}

data class RegItemUiState(
    val itemDetails: RegisterForm = RegisterForm(),
    val isEntryValid: Boolean = false
)

data class RegErrorsItemUiState(
    val itemDetails: RegisterFormErrors = RegisterFormErrors()
)


@Serializable
data class RegisterFormErrors(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    @SerialName("password_confirmation")
    val passwordConfirm: String = ""
)

@Serializable
data class RegisterForm(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    @SerialName("password_confirmation")
    val passwordConfirm: String = "",
    val message: String = ""
)


data class LogItemUiState(
    val itemDetails: LoginForm = LoginForm(),
    val isEntryValid: Boolean = false
)

data class LogErrorItemUiState(
    val itemDetails: LoginFormErrors = LoginFormErrors()
)

@Serializable
data class LoginFormErrors(
    val email: String = "",
    val password: String = ""
)

@Serializable
data class LoginForm(
    val email: String = "",
    val password: String = ""
)