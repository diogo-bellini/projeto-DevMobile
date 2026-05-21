package br.dc.ufscar.devmobile.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.dc.ufscar.devmobile.R
import br.dc.ufscar.devmobile.composables.home.BannerSection
import br.dc.ufscar.devmobile.composables.home.HomeTopBar
import br.dc.ufscar.devmobile.composables.home.StoreSection
import br.dc.ufscar.devmobile.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    onStoreClick: (Int) -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val stores by viewModel.stores.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        HomeTopBar()

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                error != null -> Text(
                    text = stringResource(R.string.home_error_loading, error!!),
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center).padding(24.dp)
                )

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item {
                        StoreSection(
                            title = stringResource(R.string.home_suggested_title),
                            stores = stores,
                            onStoreClick = onStoreClick
                        )
                    }

                    item {
                        BannerSection(resIds = listOf(R.drawable.restaurant_banner, R.drawable.restaurant_banner, R.drawable.restaurant_banner))
                    }

                    item {
                        StoreSection(
                            title = stringResource(R.string.home_latest_title),
                            stores = stores.reversed(),
                            onStoreClick = onStoreClick
                        )
                    }

                    item {
                        BannerSection(resIds = listOf(R.drawable.restaurant_banner, R.drawable.restaurant_banner))
                    }
                }
            }
        }
    }
}
