package br.dc.ufscar.devmobile.views

import androidx.compose.runtime.Composable
import br.dc.ufscar.devmobile.composables.AppCategorySearch
import br.dc.ufscar.devmobile.entities.categorySearchItems

@Composable
fun SearchScreen(){
    AppCategorySearch(categorySearchItems)
}