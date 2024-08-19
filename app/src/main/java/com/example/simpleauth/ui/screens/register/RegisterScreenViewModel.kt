package com.example.simpleauth.ui.screens.register

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleauth.auth.AuthRepository
import com.example.simpleauth.auth.AuthResult
import com.example.simpleauth.data.user.AuthPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    val authRepository: AuthRepository,
    private val prefs: AuthPreferencesRepository
) : ViewModel() {

    var itemUiState by mutableStateOf(RegItemUiState())
        private set

    var itemErrorUiState by mutableStateOf(RegErrorsItemUiState())
        private set

    var token by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            prefs.jwtTokenFlow.collectLatest { newToken ->
                token = newToken
                if (!token.isNullOrEmpty()) {
                    Log.v("LOGIN-TOKEN", "Token updated: $newToken")
                }
            }
        }
    }

    fun updateUiState(registerForm: RegisterForm){
        itemUiState = RegItemUiState(itemDetails = registerForm, isEntryValid = validateInput(registerForm))
    }

    fun updateErrorUiState(registerForm: RegisterFormErrors){
        itemErrorUiState = RegErrorsItemUiState(itemDetails = registerForm)
    }

    private fun validateInput(uiState: RegisterForm = itemUiState.itemDetails): Boolean{
        return with(uiState){
            name.isNotBlank() && email.isNotBlank() && password.isNotBlank() && passwordConfirm.isNotBlank()
        }
    }

    fun register(){
        viewModelScope.launch {
            when(val result = authRepository.register(registerForm = itemUiState.itemDetails)){
                is AuthResult.Authorized -> {
                    Log.v("Authorized", result.data.toString())
                    updateErrorUiState(
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
                    updateErrorUiState(
                        RegisterFormErrors(
                            name = result.data?.errors?.name.toString().replace("[", "").replace("]", ""),
                            email = result.data?.errors?.email.toString().replace("[", "").replace("]", ""),
                            password = result.data?.errors?.password.toString().replace("[", "").replace("]", ""),
                            passwordConfirm = result.data?.errors?.passwordConfirmation.toString().replace("[", "").replace("]", "")
                        )
                    )
                }
                is AuthResult.UnknownError -> {
                    Log.v("UnknownError", result.data.toString())
                }
            }
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