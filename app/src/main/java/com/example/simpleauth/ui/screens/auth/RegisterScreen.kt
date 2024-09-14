package com.example.simpleauth.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simpleauth.auth.data.model.AuthResult
import kotlinx.coroutines.launch


@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthScreenViewModel = hiltViewModel(),
    onLogClick: () -> Unit,
    onSuccess: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RegBody(
                itemUiState = authViewModel.regItemUiState,
                onItemValueChange = authViewModel::updateRegUiState,
                onRegClick = {
                    coroutineScope.launch {
                        authViewModel.register()
                    }
                },
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                onLogClick = onLogClick,
                itemErrorUiState = authViewModel.regItemErrorUiState
            )
        }
        LaunchedEffect(authViewModel.authResults) {
            authViewModel.authResults.collect { result ->
                if(result is AuthResult.Authorized){
                    onSuccess()
                }
            }
        }
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun RegBody(
    itemUiState: RegItemUiState,
    itemErrorUiState: RegErrorsItemUiState,
    onItemValueChange: (RegisterForm) -> Unit,
    onRegClick: () -> Unit,
    onLogClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ) {
        RegForm(
            registerForm = itemUiState.itemDetails,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth(),
            itemErrorUiState = itemErrorUiState
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onRegClick,
            enabled = itemUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Register")
        }
        Button(
            onClick = onLogClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "LogIn")
        }
    }
}


@Composable
fun RegForm(registerForm: RegisterForm, itemErrorUiState: RegErrorsItemUiState, enabled: Boolean = true, onValueChange: (RegisterForm) -> Unit = {}, modifier: Modifier = Modifier){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val errorName = (itemErrorUiState.itemDetails.name != "[]" && itemErrorUiState.itemDetails.name != "")
        val errorEmail = (itemErrorUiState.itemDetails.email != "[]" && itemErrorUiState.itemDetails.email != "")
        val errorPwd = (itemErrorUiState.itemDetails.password != "[]" && itemErrorUiState.itemDetails.password != "")
        val errorPwdCnf = (itemErrorUiState.itemDetails.passwordConfirm != "[]" && itemErrorUiState.itemDetails.passwordConfirm != "")
        OutlinedTextField(
            value = registerForm.name,
            onValueChange = { onValueChange(registerForm.copy(name = it)) },
            label = { Text(text = "User Name")},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            isError = errorName,
            supportingText = {
                if (errorName) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = itemErrorUiState.itemDetails.name,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (errorName)
                    Icon(Icons.Filled.Warning,"error", tint = MaterialTheme.colorScheme.error)
            },
        )
        OutlinedTextField(
            value = registerForm.email,
            onValueChange = { onValueChange(registerForm.copy(email = it)) },
            label = { Text(text = "User Email")},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            isError = errorEmail,
            supportingText = {
                if (errorEmail) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = itemErrorUiState.itemDetails.email,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (errorEmail)
                    Icon(Icons.Filled.Warning,"error", tint = MaterialTheme.colorScheme.error)
            },
        )
        OutlinedTextField(
            value = registerForm.password,
            onValueChange = { onValueChange(registerForm.copy(password = it)) },
            label = { Text(text = "User Password")},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            isError = errorPwd,
            supportingText = {
                if (errorPwd) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = itemErrorUiState.itemDetails.password,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (errorPwd)
                    Icon(Icons.Filled.Warning,"error", tint = MaterialTheme.colorScheme.error)
            },
        )
        OutlinedTextField(
            value = registerForm.passwordConfirm,
            onValueChange = { onValueChange(registerForm.copy(passwordConfirm = it)) },
            label = { Text(text = "Password Confirm")},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            isError = errorPwdCnf,
            supportingText = {
                if (errorPwdCnf) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = itemErrorUiState.itemDetails.passwordConfirm,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (errorPwdCnf)
                    Icon(Icons.Filled.Warning,"error", tint = MaterialTheme.colorScheme.error)
            },
        )
    }
}


