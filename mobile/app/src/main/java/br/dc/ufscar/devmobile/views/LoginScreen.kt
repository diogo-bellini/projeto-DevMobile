package br.dc.ufscar.devmobile.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.dc.ufscar.devmobile.R
import androidx.compose.ui.text.input.KeyboardType
import br.dc.ufscar.devmobile.composables.InputBox
import br.dc.ufscar.devmobile.ui.theme.DarkRed
import br.dc.ufscar.devmobile.ui.theme.GoldPrimary
import br.dc.ufscar.devmobile.viewmodels.AuthState
import br.dc.ufscar.devmobile.viewmodels.AuthViewModel

private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var emailError by rememberSaveable { mutableStateOf<Int?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<Int?>(null) }

    val authState by viewModel.authState.collectAsState()
    val isLoading = authState is AuthState.Loading

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            viewModel.resetState()
            onLoginSuccess()
        }
    }

    fun validate(): Boolean {
        var valid = true
        if (email.isBlank()) {
            emailError = R.string.error_inform_email
            valid = false
        } else if (!emailRegex.matches(email)) {
            emailError = R.string.error_invalid_email
            valid = false
        } else {
            emailError = null
        }

        if (password.isBlank()) {
            passwordError = R.string.error_inform_password
            valid = false
        } else if (password.length < 6) {
            passwordError = R.string.error_password_too_short
            valid = false
        } else {
            passwordError = null
        }

        return valid
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkRed)
            .padding(horizontal = 40.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.upeek_logo),
                contentDescription = stringResource(R.string.cd_logo),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp)
            )

            InputBox(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                placeholder = stringResource(R.string.login_placeholder),
                keyboardType = KeyboardType.Email,
                isError = emailError != null
            )
            if (emailError != null) {
                Text(
                    text = stringResource(emailError!!),
                    color = Color(0xFFFFCDD2),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            InputBox(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                placeholder = stringResource(R.string.password_placeholder),
                isPassword = true,
                isError = passwordError != null
            )
            if (passwordError != null) {
                Text(
                    text = stringResource(passwordError!!),
                    color = Color(0xFFFFCDD2),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 2.dp)
                )
            }

            if (authState is AuthState.Error) {
                val error = authState as AuthState.Error
                val message = error.messageResId?.let { stringResource(it) } ?: error.message ?: ""
                if (message.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = message,
                        color = Color(0xFFFFCDD2),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (validate()) {
                        viewModel.login(email.trim(), password)
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .width(160.dp)
                    .height(48.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(
                        text = stringResource(R.string.enter_button),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.forgot_password),
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable { onForgotPasswordClick() }
                    .padding(vertical = 4.dp)
            )

            Text(
                text = stringResource(R.string.dont_have_account_register),
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable { onRegisterClick() }
                    .padding(vertical = 4.dp)
            )
        }
    }
}
