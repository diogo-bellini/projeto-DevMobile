package br.dc.ufscar.devmobile.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.dc.ufscar.devmobile.R
import br.dc.ufscar.devmobile.composables.searchScreen.AppSearchBar
import br.dc.ufscar.devmobile.composables.searchScreen.SearchResultCard
import br.dc.ufscar.devmobile.network.SearchResultDto
import br.dc.ufscar.devmobile.ui.AppFontSize
import br.dc.ufscar.devmobile.ui.theme.DarkRed
import br.dc.ufscar.devmobile.viewmodels.SearchResultViewModel
import coil.compose.AsyncImage

@Composable
fun SearchResultScreen(
    category : String,
    onRestaurantClick : (Int) -> Unit = {},
    onFilterClick : () -> Unit = {},
    viewModel : SearchResultViewModel = viewModel()
) {
    LaunchedEffect(category) {
        viewModel.searchStoresByCategory(category)
    }

    Column(
        Modifier.absolutePadding(20.dp, 20.dp, 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppSearchBar(
            viewModel,
            onRestaurantClick
        )

        Button(
            onClick = onFilterClick,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkRed,
                contentColor = Color.White
            )
        ) {
            Text(text = stringResource(R.string.filter_title))
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(viewModel.resultCategory) { store ->
                SearchResultCard(
                    viewModel = viewModel,
                    store = store,
                    onClick = { onRestaurantClick(store.id) }
                )
            }
        }
    }
}