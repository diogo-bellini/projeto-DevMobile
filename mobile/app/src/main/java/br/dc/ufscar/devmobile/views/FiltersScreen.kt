package br.dc.ufscar.devmobile.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.dc.ufscar.devmobile.R
import br.dc.ufscar.devmobile.ui.AppFontSize
import br.dc.ufscar.devmobile.ui.theme.DarkGray
import br.dc.ufscar.devmobile.ui.theme.DarkRed
import br.dc.ufscar.devmobile.ui.theme.GoldPrimary
import br.dc.ufscar.devmobile.viewmodels.FiltersViewModel

@Composable
fun FiltersScreen(
    viewModel : FiltersViewModel = viewModel()
) {
    var price by remember { mutableFloatStateOf(0f) }
    var distance by remember { mutableFloatStateOf(0f) }
    var reviews by remember { mutableStateOf(0f..1000f) }
    var minRating by remember { mutableIntStateOf(0) }

    Column() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(color = DarkRed),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = stringResource(R.string.filter_title),
                fontSize = AppFontSize.XLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Nota mínima:",
                fontSize = AppFontSize.Large,
                fontWeight = FontWeight.Bold
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (i in 1..5) {
                    Icon(
                        imageVector = if (i <= minRating){
                            Icons.Filled.Star
                        } else {
                            Icons.Outlined.StarOutline
                        },
                        contentDescription = null,
                        tint = if (i <= minRating) {
                            GoldPrimary
                        } else {
                            DarkGray
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { minRating = i }
                    )
                }
            }
            Text(
                text = "Preço médio: R$ ${String.format("%.2f", price)}",
                fontSize = AppFontSize.Large,
                fontWeight = FontWeight.Bold
            )
            Slider(
                value = price,
                onValueChange = { price = it },
                colors = SliderDefaults.colors(
                    thumbColor = DarkGray,
                    activeTrackColor = DarkGray,
                    inactiveTrackColor = DarkGray.copy(alpha = 0.24f)
                ),
                valueRange = 0f..500f,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Distância máxima: ${String.format("%.0f", distance)} km",
                fontSize = AppFontSize.Large,
                fontWeight = FontWeight.Bold
            )
            Slider(
                value = distance,
                onValueChange = { distance = it },
                colors = SliderDefaults.colors(
                    thumbColor = DarkGray,
                    activeTrackColor = DarkGray,
                    inactiveTrackColor = DarkGray.copy(alpha = 0.24f)
                ),
                steps = 9,
                valueRange = 0f..100f,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Quantidade de avaliações: ${reviews.start.toInt()} - ${reviews.endInclusive.toInt()}",
                fontSize = AppFontSize.Large,
                fontWeight = FontWeight.Bold
            )
            RangeSlider(
                value = reviews,
                onValueChange = { reviews = it },
                colors = SliderDefaults.colors(
                    thumbColor = DarkGray,
                    activeTrackColor = DarkGray,
                    inactiveTrackColor = DarkGray.copy(alpha = 0.24f)
                ),
                valueRange = 0f..1000f,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { /* TODO: Implement filter logic in viewModel */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text(
                    text = "Aplicar Filtros",
                    fontSize = AppFontSize.Large,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}