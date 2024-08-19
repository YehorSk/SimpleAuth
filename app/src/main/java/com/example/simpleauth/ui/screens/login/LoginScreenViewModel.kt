package com.example.simpleauth.ui.screens.login


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleauth.auth.AuthRepository
import com.example.simpleauth.data.user.AuthPreferencesRepository
import com.example.simpleauth.ui.screens.register.RegItemUiState
import com.example.simpleauth.ui.screens.register.RegisterForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var itemUiState by mutableStateOf(LogItemUiState())
        private set


    fun updateUiState(loginForm: LoginForm){
        itemUiState = LogItemUiState(itemDetails = loginForm, isEntryValid = validateInput(loginForm))
    }

    private fun validateInput(uiState: LoginForm = itemUiState.itemDetails): Boolean{
        return with(uiState){
            email.isNotBlank() && password.isNotBlank()
        }
    }

    fun login(){
        viewModelScope.launch {
            authRepository.login(loginForm = itemUiState.itemDetails)
        }
    }

}


data class LogItemUiState(
    val itemDetails: LoginForm = LoginForm(),
    val isEntryValid: Boolean = false
)
@Serializable
data class LoginForm(
    val email: String = "",
    val password: String = ""
)