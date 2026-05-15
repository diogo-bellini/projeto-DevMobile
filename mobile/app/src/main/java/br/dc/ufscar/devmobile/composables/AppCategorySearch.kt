package br.dc.ufscar.devmobile.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.dc.ufscar.devmobile.entities.CategorySearchItem

@Composable
fun AppCategorySearch(
    items : List<CategorySearchItem>
){
    items.forEach { item ->
        Text(text = stringResource(item.title))
    }
}