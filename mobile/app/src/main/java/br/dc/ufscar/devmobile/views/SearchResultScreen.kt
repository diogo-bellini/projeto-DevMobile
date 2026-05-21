package br.dc.ufscar.devmobile.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.dc.ufscar.devmobile.R
import br.dc.ufscar.devmobile.composables.searchScreen.AppSearchBar
import br.dc.ufscar.devmobile.composables.searchScreen.SearchResultCard
import br.dc.ufscar.devmobile.ui.theme.DarkRed
import br.dc.ufscar.devmobile.viewmodels.SearchResultViewModel

@Composable
fun SearchResultScreen(
    hasPermission : Boolean,
    category : String? = null,
    price : Float? = null,
    distance : Float? = null,
    minReviews : Int? = null,
    maxReviews : Int? = null,
    minRating : Int? = null,
    onRestaurantClick : (Int) -> Unit = {},
    onFilterClick : (String?) -> Unit,
    viewModel : SearchResultViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(category, price, distance, minReviews, maxReviews, minRating, hasPermission) {
        if (hasPermission) {
            viewModel.getLocation(context) {
                if (category != null && price != null) {
                    viewModel.searchStoresByFilters(
                        price = price,
                        distance = distance ?: 100f,
                        minReviews = minReviews ?: 0,
                        maxReviews = maxReviews ?: 1000,
                        minRating = minRating ?: 0,
                        category = category
                    )
                } else if (category != null) {
                    viewModel.searchStoresByCategory(category)
                } else if (price != null) {
                    viewModel.searchStoresByFilters(
                        price = price,
                        distance = distance ?: 100f,
                        minReviews = minReviews ?: 0,
                        maxReviews = maxReviews ?: 1000,
                        minRating = minRating ?: 0,
                        category = category
                    )
                }
            }
        } else {
            if (category != null) {
                viewModel.searchStoresByCategory(category)
            } else if (price != null) {
                viewModel.searchStoresByFilters(
                    price = price,
                    distance = distance ?: 100f,
                    minReviews = minReviews ?: 0,
                    maxReviews = maxReviews ?: 1000,
                    minRating = minRating ?: 0,
                    category = category
                )
            }
        }
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
            onClick = { onFilterClick(category) },
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
            items(viewModel.otherResults) { store ->
                SearchResultCard(
                    store = store,
                    onClick = { onRestaurantClick(store.id) }
                )
            }
        }
    }
}
