package br.dc.ufscar.devmobile.composables.searchScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.dc.ufscar.devmobile.entities.CategorySearchItem
import br.dc.ufscar.devmobile.ui.AppFontSize
import br.dc.ufscar.devmobile.ui.theme.Gray
import br.dc.ufscar.devmobile.ui.theme.OpenSans

@Composable
fun CategorySearchComponent(
    item : CategorySearchItem,
    onCategoryClick: () -> Unit = {}
){
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = Gray
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable { onCategoryClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(item.title),
                fontFamily = OpenSans,
                fontSize = AppFontSize.Normal,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(item.icon),
                contentDescription = stringResource(item.title),
                modifier = Modifier.size(55.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}