package br.dc.ufscar.devmobile.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.AnnotatedString
import br.dc.ufscar.devmobile.R
import br.dc.ufscar.devmobile.composables.InputBox
import br.dc.ufscar.devmobile.ui.theme.DarkRed
import br.dc.ufscar.devmobile.ui.theme.GoldPrimary
import br.dc.ufscar.devmobile.viewmodels.AuthState
import br.dc.ufscar.devmobile.viewmodels.AuthViewModel
import java.util.Calendar
import java.util.TimeZone

private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

private class CpfVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val formatted = buildString {
            digits.forEachIndexed { i, c ->
                when (i) {
                    3 -> append('.')
                    6 -> append('.')
                    9 -> append('-')
                }
                append(c)
            }
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val o = offset.coerceIn(0, digits.length)
                return when {
                    o <= 3 -> o
                    o <= 6 -> o + 1
                    o <= 9 -> o + 2
                    else -> o + 3
                }
            }
            override fun transformedToOriginal(offset: Int): Int {
                val t = offset.coerceIn(0, formatted.length)
                return when {
                    t <= 3 -> t
                    t <= 7 -> t - 1
                    t <= 11 -> t - 2
                    else -> t - 3
                }.coerceIn(0, digits.length)
            }
        }
        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerField(
    selectedDate: String,
    placeholder: String,
    onDateSelected: (String) -> Unit,
    isError: Boolean = false
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val shape = RoundedCornerShape(4.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(shape)
            .background(if (isError) Color(0xFFFFF0F0) else Color.White)
            .then(if (isError) Modifier.border(1.5.dp, Color(0xFFE57373), shape) else Modifier)
            .clickable { showDialog = true }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (selectedDate.isEmpty()) placeholder else selectedDate,
                color = if (selectedDate.isEmpty()) Color.Gray else Color.Black,
                fontSize = 16.sp
            )
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                        cal.timeInMillis = millis
                        val day = cal.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
                        val month = (cal.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
                        val year = cal.get(Calendar.YEAR).toString()
                        onDateSelected("$day/$month/$year")
                    }
                    showDialog = false
                }) {
                    Text("OK", color = GoldPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar", color = GoldPrimary)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }

    var fullNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var cpfError by remember { mutableStateOf<String?>(null) }
    var birthDateError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var termsError by remember { mutableStateOf<String?>(null) }

    val authState by viewModel.authState.collectAsState()
    val isLoading = authState is AuthState.Loading

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            viewModel.resetState()
            onRegisterSuccess()
        }
    }

    fun validate(): Boolean {
        var valid = true

        if (fullName.isBlank()) {
            fullNameError = "Informe seu nome completo."
            valid = false
        } else {
            fullNameError = null
        }

        if (email.isBlank()) {
            emailError = "Informe seu email."
            valid = false
        } else if (!emailRegex.matches(email)) {
            emailError = "Email inválido."
            valid = false
        } else {
            emailError = null
        }

        if (cpf.length != 11) {
            cpfError = if (cpf.isEmpty()) "Informe seu CPF." else "CPF deve ter 11 dígitos."
            valid = false
        } else {
            cpfError = null
        }

        if (birthDate.isBlank()) {
            birthDateError = "Selecione sua data de nascimento."
            valid = false
        } else {
            birthDateError = null
        }

        if (password.isBlank()) {
            passwordError = "Informe uma senha."
            valid = false
        } else if (password.length < 6) {
            passwordError = "A senha deve ter no mínimo 6 caracteres."
            valid = false
        } else {
            passwordError = null
        }

        if (confirmPassword.isBlank()) {
            confirmPasswordError = "Confirme sua senha."
            valid = false
        } else if (confirmPassword != password) {
            confirmPasswordError = "As senhas não coincidem."
            valid = false
        } else {
            confirmPasswordError = null
        }

        if (!termsAccepted) {
            termsError = "Você precisa aceitar os termos."
            valid = false
        } else {
            termsError = null
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
            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.upeek_logo),
                contentDescription = "Logo UPeek",
                modifier = Modifier
                    .width(200.dp)
                    .padding(bottom = 40.dp)
            )

            InputBox(
                value = fullName,
                onValueChange = { fullName = it; fullNameError = null },
                placeholder = stringResource(R.string.full_name_placeholder),
                isError = fullNameError != null
            )
            if (fullNameError != null) ErrorText(fullNameError!!)

            Spacer(modifier = Modifier.height(16.dp))

            InputBox(
                value = email,
                onValueChange = { email = it; emailError = null },
                placeholder = stringResource(R.string.email_placeholder),
                keyboardType = KeyboardType.Email,
                isError = emailError != null
            )
            if (emailError != null) ErrorText(emailError!!)

            Spacer(modifier = Modifier.height(16.dp))

            InputBox(
                value = cpf,
                onValueChange = { new ->
                    val digits = new.filter { it.isDigit() }.take(11)
                    cpf = digits
                    cpfError = null
                },
                placeholder = stringResource(R.string.cpf_placeholder),
                keyboardType = KeyboardType.Number,
                visualTransformation = CpfVisualTransformation(),
                isError = cpfError != null
            )
            if (cpfError != null) ErrorText(cpfError!!)

            Spacer(modifier = Modifier.height(16.dp))

            DatePickerField(
                selectedDate = birthDate,
                placeholder = stringResource(R.string.birth_date_placeholder),
                onDateSelected = { birthDate = it; birthDateError = null },
                isError = birthDateError != null
            )
            if (birthDateError != null) ErrorText(birthDateError!!)

            Spacer(modifier = Modifier.height(16.dp))

            InputBox(
                value = password,
                onValueChange = { password = it; passwordError = null },
                placeholder = stringResource(R.string.password_placeholder),
                isPassword = true,
                isError = passwordError != null
            )
            if (passwordError != null) ErrorText(passwordError!!)

            Spacer(modifier = Modifier.height(16.dp))

            InputBox(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; confirmPasswordError = null },
                placeholder = stringResource(R.string.confirm_password_placeholder),
                isPassword = true,
                isError = confirmPasswordError != null
            )
            if (confirmPasswordError != null) ErrorText(confirmPasswordError!!)

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = { termsAccepted = it; termsError = null },
                    colors = CheckboxDefaults.colors(
                        checkedColor = GoldPrimary,
                        uncheckedColor = if (termsError != null) Color(0xFFE57373) else Color.White,
                        checkmarkColor = Color.White
                    )
                )
                Text(
                    text = stringResource(R.string.terms_agreement),
                    color = Color.White,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            if (termsError != null) ErrorText(termsError!!)

            if (authState is AuthState.Error) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = (authState as AuthState.Error).message,
                    color = Color(0xFFFFCDD2),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (validate()) {
                        val parts = birthDate.split("/")
                        val iso = "${parts[2]}-${parts[1]}-${parts[0]}"
                        viewModel.register(
                            nomeCompleto = fullName.trim(),
                            email = email.trim(),
                            cpf = cpf,
                            dataNascimento = iso,
                            senha = password
                        )
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(
                        text = stringResource(R.string.finalize_register_button),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.already_have_account_login),
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable { onLoginClick() }
                    .padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun ErrorText(message: String) {
    Text(
        text = message,
        color = Color(0xFFFFCDD2),
        fontSize = 12.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 2.dp)
    )
}