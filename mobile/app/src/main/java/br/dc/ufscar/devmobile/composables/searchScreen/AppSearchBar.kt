package br.dc.ufscar.devmobile.composables.searchScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.dc.ufscar.devmobile.viewmodels.SearchResultViewModel
import coil.compose.rememberAsyncImagePainter
import br.dc.ufscar.devmobile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
    viewModel: SearchResultViewModel,
    onRestaurantClick: (Int) -> Unit = {}
){
    var text by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = text,
                onQueryChange = {
                    text = it
                    viewModel.searchStoresBySubstring(it)
                },
                onSearch = { expanded = false },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text(stringResource(R.string.searchbar_label)) }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (expanded) {
                    Modifier.heightIn(max = 400.dp)
                } else {
                    Modifier
                }
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel.resultsSearchBar) { item ->
                ListItem(
                    headlineContent = { Text(item.namepiece) },
                    leadingContent = {
                        Image(
                            painter = rememberAsyncImagePainter(item.logoUrl.replace("localhost", "10.0.2.2")),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRestaurantClick(item.id) }
                )
            }
        }
    }
}