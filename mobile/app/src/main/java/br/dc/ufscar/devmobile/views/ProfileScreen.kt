package br.dc.ufscar.devmobile.views

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.dc.ufscar.devmobile.composables.AppTopBar
import br.dc.ufscar.devmobile.entities.User
import br.dc.ufscar.devmobile.ui.theme.DarkRed
import br.dc.ufscar.devmobile.ui.theme.GoldPrimary
import br.dc.ufscar.devmobile.viewmodels.ProfileState
import br.dc.ufscar.devmobile.viewmodels.ProfileViewModel
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

private fun formatCpf(cpf: String): String {
    val digits = cpf.filter { it.isDigit() }
    return if (digits.length == 11)
        "${digits.substring(0, 3)}.${digits.substring(3, 6)}.${digits.substring(6, 9)}-${digits.substring(9)}"
    else cpf
}

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit,
    onLogout: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(title = "Dados Pessoais", onBackClick = onBackClick)
        }
    ) { innerPadding ->
        when (val s = state) {
            is ProfileState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = DarkRed)
                }
            }

            is ProfileState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhum usuário encontrado.",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }

            is ProfileState.Success -> {
                ProfileContent(
                    user = s.user,
                    modifier = Modifier.padding(innerPadding),
                    onLogout = {
                        viewModel.logout(onLogout)
                    }
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    user: User,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UserInfoCard(user = user)

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = DarkRed),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = "Sair da conta",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun UserInfoCard(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkRed, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Text(
                text = "Informações do usuário",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        InfoRow(label = "Nome completo", value = user.fullName)
        HorizontalDivider(color = Color(0xFFEEEEEE))
        InfoRow(label = "CPF", value = formatCpf(user.cpf))
        HorizontalDivider(color = Color(0xFFEEEEEE))
        InfoRow(label = "Email", value = user.email)
        HorizontalDivider(color = Color(0xFFEEEEEE))
        InfoRow(
            label = "Data de nascimento",
            value = user.birthdate.format(dateFormatter),
            isLast = true
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String, isLast: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(
            text = label,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            color = Color.Gray,
            fontSize = 13.sp
        )
    }
}