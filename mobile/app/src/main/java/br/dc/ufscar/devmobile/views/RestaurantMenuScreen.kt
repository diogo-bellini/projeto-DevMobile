package br.dc.ufscar.devmobile.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import br.dc.ufscar.devmobile.viewmodels.MenuViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import br.dc.ufscar.devmobile.ui.theme.DarkGray
import br.dc.ufscar.devmobile.ui.theme.DarkRed
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.dc.ufscar.devmobile.R
import br.dc.ufscar.devmobile.composables.AppTopBar
import br.dc.ufscar.devmobile.ui.AppFontSize

data class MenuItem(val name: String, val description: String)
data class MenuSection(val title: String, val items: List<MenuItem>)

@Composable
fun RestaurantMenuScreen(
    storeId: Int = 1,
    onBackClick: () -> Unit = {},
    viewModel: MenuViewModel = viewModel(factory = MenuViewModel.Factory(storeId))
) {
    val sections by viewModel.sections.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(title = stringResource(R.string.restaurant_menu_title), onBackClick = onBackClick)
        },
        containerColor = DarkRed
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalArrangement = Arrangement.End
        ) {
            if (isLoading) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else LazyColumn(modifier = Modifier.weight(1f).padding(17.dp)) {
                item {
                    Text(
                        text = stringResource(R.string.menu_executive_section),
                        fontWeight = FontWeight.Bold,
                        fontSize = AppFontSize.SemiLarge,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkGray)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }

                sections.forEach { section ->
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = section.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = AppFontSize.SemiLarge,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    items(section.items) { item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            Text(text = item.name, fontWeight = FontWeight.SemiBold, fontSize = AppFontSize.Normal, color = Color.White)
                            Text(text = item.description, fontSize = AppFontSize.Tiny, color = Color.White)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            Image(
                painter = painterResource(id = R.drawable.menu_image),
                contentDescription = "Menu",
                modifier = Modifier.fillMaxHeight(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RestaurantMenuScreenPreview() {
    RestaurantMenuScreen()
}
