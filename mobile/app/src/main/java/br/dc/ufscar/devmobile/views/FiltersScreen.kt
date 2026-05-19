package br.dc.ufscar.devmobile.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import br.dc.ufscar.devmobile.R
import br.dc.ufscar.devmobile.ui.AppFontSize
import br.dc.ufscar.devmobile.ui.theme.DarkRed

@Composable
fun FiltersScreen(){
    Column() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = DarkRed),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = stringResource(R.string.filter_title),
                fontSize = AppFontSize.Large,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

    }
}