package br.dc.ufscar.devmobile.composables.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.dc.ufscar.devmobile.network.Store

@Composable
fun StoreSection(
    title: String,
    stores: List<Store>,
    onStoreClick: (Int) -> Unit
) {
    Column {
        SectionHeader(title = title)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(stores) { store ->
                StoreHorizontalItem(store = store, onClick = { onStoreClick(store.id) })
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
