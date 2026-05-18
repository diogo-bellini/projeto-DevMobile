package br.dc.ufscar.devmobile.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.dc.ufscar.devmobile.network.Store
import br.dc.ufscar.devmobile.ui.AppFontSize
import br.dc.ufscar.devmobile.ui.theme.GoldPrimary
import br.dc.ufscar.devmobile.viewmodels.HomeViewModel
import coil.compose.AsyncImage

@Composable
fun HomeScreen(
    onStoreClick: (Int) -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val stores by viewModel.stores.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            error != null -> Text(
                text = "Erro ao carregar lojas:\n$error",
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center).padding(24.dp)
            )

            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(stores) { store ->
                    StoreCard(store = store, onClick = { onStoreClick(store.id) })
                }
            }
        }
    }
}

@Composable
private fun StoreCard(store: Store, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = store.logo.replace("localhost", "10.0.2.2"),
                contentDescription = store.name,
                modifier = Modifier.size(64.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = store.namepiece, fontWeight = FontWeight.Bold, fontSize = AppFontSize.Medium)
                Text(text = store.category, fontSize = AppFontSize.Small, color = Color.Gray)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "★ ${store.rating}", fontSize = AppFontSize.Small, color = GoldPrimary, fontWeight = FontWeight.SemiBold)
                    Text(text = "(${store.reviews})", fontSize = AppFontSize.Small, color = Color.Gray)
                    Text(text = "• ${store.time}", fontSize = AppFontSize.Small, color = Color.Gray)
                }
                Text(text = store.address, fontSize = AppFontSize.Tiny, color = Color.Gray, maxLines = 1)
            }
        }
    }
}
