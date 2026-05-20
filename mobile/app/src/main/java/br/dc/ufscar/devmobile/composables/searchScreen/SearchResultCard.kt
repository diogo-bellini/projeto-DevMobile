package br.dc.ufscar.devmobile.composables.searchScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.dc.ufscar.devmobile.R
import br.dc.ufscar.devmobile.network.SearchResultDto
import br.dc.ufscar.devmobile.ui.AppFontSize
import br.dc.ufscar.devmobile.ui.theme.Gray
import coil.compose.rememberAsyncImagePainter

@Composable
fun SearchResultCard(
    store : SearchResultDto,
    onClick : () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Gray)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(store.logo.replace("localhost", "10.0.2.2")),
                contentDescription = null,
                modifier = Modifier.size(64.dp).clip(CircleShape)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = store.namepiece,
                    fontWeight = FontWeight.Bold,
                    fontSize = AppFontSize.Medium
                )
                store.distance?.let {
                    Text(
                        text = stringResource(R.string.search_result_distance, it),
                        fontSize = AppFontSize.Small,
                    )
                }
                Text(
                    text = stringResource(R.string.search_result_avg_price, store.avgPrice),
                    fontSize = AppFontSize.Small,
                )
            }
        }
    }
}