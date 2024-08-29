package com.example.simpleauth.ui.screens.auth

import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.simpleauth.auth.data.model.AuthResult

import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthScreenViewModel = hiltViewModel(),
    onSuccess: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState by authViewModel.uiState.collectAsState()
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
            LogBody(
                itemUiState = authViewModel.logItemUiState,
                onItemValueChange = authViewModel::updateLogUiState,
                onLogClick = {
                    coroutineScope.launch {
                        authViewModel.login()
                    }
                },
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
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
    fun LogBody(
        itemUiState: LogItemUiState,
        onItemValueChange: (LoginForm) -> Unit,
        onLogClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
        ) {
            LogForm(
                loginForm = itemUiState.itemDetails,
                onValueChange = onItemValueChange,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onLogClick,
                enabled = itemUiState.isEntryValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "LogIn")
            }
        }
    }

    @Composable
    fun LogForm(
        modifier: Modifier = Modifier,
        loginForm: LoginForm,
        enabled: Boolean = true,
        onValueChange: (LoginForm) -> Unit = {}
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = loginForm.email,
                onValueChange = { onValueChange(loginForm.copy(email = it)) },
                label = { Text(text = "User Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
            OutlinedTextField(
                value = loginForm.password,
                onValueChange = { onValueChange(loginForm.copy(password = it)) },
                label = { Text(text = "User Password") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
        }
    }