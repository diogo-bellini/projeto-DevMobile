package br.dc.ufscar.devmobile.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.dc.ufscar.devmobile.R
import androidx.lifecycle.viewmodel.compose.viewModel
import br.dc.ufscar.devmobile.composables.searchScreen.AppCategorySearch
import br.dc.ufscar.devmobile.composables.searchScreen.AppSearchBar
import br.dc.ufscar.devmobile.entities.categorySearchItems
import br.dc.ufscar.devmobile.ui.AppFontSize
import br.dc.ufscar.devmobile.ui.theme.DarkRed
import br.dc.ufscar.devmobile.ui.theme.OpenSans
import br.dc.ufscar.devmobile.viewmodels.SearchResultViewModel

@Composable
fun SearchScreen(
    hasPermission : Boolean,
    viewModel : SearchResultViewModel = viewModel(),
    onRestaurantClick : (Int) -> Unit = {},
    onCategoryClick : (String) -> Unit = {},
    onFilterClick : () -> Unit = {}
){
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(hasPermission) {
        if (hasPermission){
            viewModel.getLocation(context)
        }
    }

    Column(
        Modifier
            .absolutePadding(20.dp, 20.dp, 20.dp)
            .verticalScroll(rememberScrollState()),
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
        Text(
            text = stringResource(R.string.category_label),
            fontFamily = OpenSans,
            fontSize = AppFontSize.Large,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        AppCategorySearch(
            items = categorySearchItems,
            onCategoryClick = { item ->
                onCategoryClick(item.backendName)
            }
        )
    }
}