package br.dc.ufscar.devmobile.composables.searchScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.dc.ufscar.devmobile.entities.CategorySearchItem

@Composable
fun AppCategorySearch(
    items : List<CategorySearchItem>,
    onCategoryClick : (CategorySearchItem) -> Unit = {}
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.heightIn(max = 2000.dp).padding(bottom = 10.dp),
        userScrollEnabled = false
    ) {
        items(items) { item ->
            CategorySearchComponent(
                item = item,
                onCategoryClick = { onCategoryClick(item) }
            )
        }
    }
}