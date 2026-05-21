package br.dc.ufscar.devmobile.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import br.dc.ufscar.devmobile.composables.AppTopBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.dc.ufscar.devmobile.R
import br.dc.ufscar.devmobile.ui.theme.DarkRed
import br.dc.ufscar.devmobile.ui.theme.GoldPrimary
import br.dc.ufscar.devmobile.ui.theme.LightGray
import br.dc.ufscar.devmobile.repositories.ReservationRepository
import br.dc.ufscar.devmobile.repositories.StoreRepository
import br.dc.ufscar.devmobile.viewmodels.ReservationUiState
import br.dc.ufscar.devmobile.viewmodels.ReservationViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreen(
    storeRepository: StoreRepository,
    reservationRepository: ReservationRepository,
    storeId: Int = 1,
    onBackClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    viewModel: ReservationViewModel = viewModel(
        factory = ReservationViewModel.Factory(storeId, storeRepository, reservationRepository)
    )
) {
    val store by viewModel.store.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val peopleOptions = remember(context) {
        (1..10).map { count ->
            context.resources.getQuantityString(R.plurals.reservation_people_count, count, count)
        }
    }
    val tableOptions = remember(context) {
        (1..20).map { num ->
            context.getString(R.string.reservation_table_number, num)
        }
    }

    val timeSlots = remember(store) {
        val open = store?.openHour ?: 11
        val close = store?.closeHour ?: 23
        buildList {
            for (hour in open until close) {
                add("%02d:00".format(hour))
                add("%02d:30".format(hour))
            }
            add("%02d:00".format(close))
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(datePickerState.selectedDateMillis) {
        viewModel.selectedDateMillis = datePickerState.selectedDateMillis
    }

    LaunchedEffect(uiState) {
        if (uiState is ReservationUiState.Success) onConfirmClick()
    }

    val dataPicked = datePickerState.selectedDateMillis?.let { millis ->
        SimpleDateFormat("dd MMM, EEE", Locale("pt", "BR"))
            .format(Date(millis))
            .replaceFirstChar { it.uppercase() }
    } ?: stringResource(R.string.reservation_select_date)

    val isLoading = uiState is ReservationUiState.Loading

    Scaffold(
        topBar = { AppTopBar(title = stringResource(R.string.reservation_title), onBackClick = onBackClick) },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = store?.name ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            )
            Text(
                text = store?.address ?: "",
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
            )

            FormRow(label = stringResource(R.string.reservation_label_people)) {
                SelectableDropdown(
                    selected = if (viewModel.people > 0) peopleOptions[viewModel.people - 1] else stringResource(R.string.select_placeholder),
                    options = peopleOptions,
                    onSelect = { index -> viewModel.people = index + 1 }
                )
            }

            FormRow(label = stringResource(R.string.reservation_label_date)) {
                DropdownSelector(text = dataPicked) { showDatePicker = true }
            }

            FormRow(label = stringResource(R.string.reservation_label_time)) {
                SelectableDropdown(
                    selected = viewModel.selectedTime.ifEmpty { stringResource(R.string.select_placeholder) },
                    options = timeSlots,
                    onSelect = { index -> viewModel.selectedTime = timeSlots[index] }
                )
            }

            FormRow(label = stringResource(R.string.reservation_label_table)) {
                SelectableDropdown(
                    selected = if (viewModel.selectedTable > 0) tableOptions[viewModel.selectedTable - 1] else stringResource(R.string.select_placeholder),
                    options = tableOptions,
                    onSelect = { index -> viewModel.selectedTable = index + 1 }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState is ReservationUiState.Error) {
                Text(
                    text = (uiState as ReservationUiState.Error).message,
                    color = DarkRed,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.createReservation() },
                enabled = viewModel.isFormValid && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = Color.White,
                    disabledContainerColor = GoldPrimary.copy(alpha = 0.5f),
                    disabledContentColor = Color.White
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = stringResource(R.string.reservation_confirm_button), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(stringResource(R.string.ok), color = DarkRed)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(stringResource(R.string.cancel), color = DarkRed)
                    }
                },
                colors = DatePickerDefaults.colors(containerColor = Color.White)
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        selectedDayContainerColor = DarkRed,
                        todayDateBorderColor = DarkRed,
                        todayContentColor = DarkRed
                    )
                )
            }
        }
    }
}

@Composable
fun SelectableDropdown(
    selected: String,
    options: List<String>,
    onSelect: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        DropdownSelector(text = selected) { expanded = true }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(option, fontSize = 14.sp) },
                    onClick = {
                        onSelect(index)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun FormRow(label: String, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.width(170.dp)
        )
        content()
    }
}

@Composable
fun DropdownSelector(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .background(LightGray, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text, fontSize = 14.sp, color = Color.Black)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = stringResource(R.string.cd_select),
            tint = Color.Black,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReservationScreenPreview() {
    ReservationScreen()
}
