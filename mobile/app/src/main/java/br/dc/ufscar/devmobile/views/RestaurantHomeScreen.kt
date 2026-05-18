package br.dc.ufscar.devmobile.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import br.dc.ufscar.devmobile.R
import br.dc.ufscar.devmobile.composables.InfoRowCard
import br.dc.ufscar.devmobile.ui.AppFontSize
import br.dc.ufscar.devmobile.ui.theme.GoldPrimary
import br.dc.ufscar.devmobile.composables.OpenStreetMapComponent
import br.dc.ufscar.devmobile.composables.RestaurantHeader
import br.dc.ufscar.devmobile.viewmodels.RestaurantViewModel

@Composable
fun RestaurantHomeScreen(
    modifier: Modifier = Modifier,
    storeId: Int = 1,
    onReserveClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    viewModel: RestaurantViewModel = viewModel(factory = RestaurantViewModel.Factory(storeId))
) {
    val store by viewModel.store.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        RestaurantHeader(
            name = store?.name ?: "",
            logoUrl = store?.logo ?: "",
            rating = store?.rating ?: 0.0,
            reviews = store?.reviews ?: 0,
            avgPrice = store?.avgPrice ?: 0.0
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoRowCard(
                title = stringResource(R.string.restaurant_schedule_title),
                subtitle = stringResource(R.string.restaurant_schedule_subtitle),
                onClick = { /* TODO */ }
            )
            InfoRowCard(
                title = stringResource(R.string.restaurant_table_map_title),
                subtitle = stringResource(R.string.restaurant_table_map_subtitle),
                onClick = { /* TODO */ }
            )
            InfoRowCard(
                title = stringResource(R.string.restaurant_menu_title),
                subtitle = stringResource(R.string.restaurant_menu_subtitle),
                onClick = onMenuClick
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.restaurant_location_title),
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = AppFontSize.Normal,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .border(1.dp, Color.Black)
                .clip(RectangleShape)
        ) {
            OpenStreetMapComponent(
                latitude = store?.latitude ?: -22.0087082,
                longitude = store?.longitude ?: -47.8909263,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onReserveClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.restaurant_reserve_button),
                fontSize = AppFontSize.Medium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RestaurantHomeScreenPreview() {
    RestaurantHomeScreen()
}
