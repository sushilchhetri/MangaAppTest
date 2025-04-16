package com.mangaversetest.presentation.ui.login


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mangaversetest.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val userName = remember { mutableStateOf("") }
    val userPassword = remember { mutableStateOf("") }
    val userNameError = remember { mutableStateOf<String?>(null) }
    val userPasswordError = remember { mutableStateOf<String?>(null) }

    val loginSuccess by viewModel.loginSuccess

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            onLoginSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(40.dp)
        ) {
            // Welcome Text
            Text(
                text = context.getString(R.string.welcomeLoginDesc),
                fontSize = 25.sp,
                color = Color.Cyan,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
            )

            // Username Field
            OutlinedTextField(
                value = userName.value,
                onValueChange = {
                    userName.value = it
                    if (it.isNotBlank()) userNameError.value = null
                },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = context.getString(R.string.userName))
                },
                label = { Text(context.getString(R.string.userName)) },
                isError = userNameError.value != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    errorTextColor = MaterialTheme.colorScheme.error,
                    errorIndicatorColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = Color.LightGray,
                    unfocusedLabelColor = Color.Gray,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    focusedLeadingIconColor = Color.White,
                    unfocusedLeadingIconColor = Color.Gray,
                )
            )
            if (userNameError.value != null) {
                Text(
                    text = userNameError.value ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Password Field
            OutlinedTextField(
                value = userPassword.value,
                onValueChange = {
                    userPassword.value = it
                    if (it.isNotBlank()) userPasswordError.value = null
                },
                leadingIcon = {
                    Icon(Icons.Default.Info, contentDescription = context.getString(R.string.password))
                },
                label = { Text(context.getString(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                isError = userPasswordError.value != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    errorTextColor = MaterialTheme.colorScheme.error,
                    errorIndicatorColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = Color.LightGray,
                    unfocusedLabelColor = Color.Gray,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    focusedLeadingIconColor = Color.White,
                    unfocusedLeadingIconColor = Color.Gray,
                )
            )
            if (userPasswordError.value != null) {
                Text(
                    text = userPasswordError.value ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Login Button
            OutlinedButton(
                onClick = {
                    var hasError = false
                    if (userName.value.isBlank()) {
                        userNameError.value = context.getString(R.string.userEmptyValidation)
                        hasError = true
                    }
                    if (userPassword.value.isBlank()) {
                        userPasswordError.value = context.getString(R.string.passwordEmptyValidation)
                        hasError = true
                    }
                    if (!hasError) {
                        viewModel.login()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp)
            ) {
                Text(
                    text = context.getString(R.string.login),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}


