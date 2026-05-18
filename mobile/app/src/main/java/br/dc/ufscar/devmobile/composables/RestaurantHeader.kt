package br.dc.ufscar.devmobile.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import br.dc.ufscar.devmobile.R
import br.dc.ufscar.devmobile.ui.AppFontSize
import coil.compose.AsyncImage

@Composable
fun RestaurantHeader(
    modifier: Modifier = Modifier,
    name: String = "",
    logoUrl: String = "",
    rating: Double = 0.0,
    reviews: Int = 0,
    avgPrice: Double = 0.0
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.restaurant_banner),
            contentDescription = stringResource(R.string.cd_banner),
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .blur(radius = 2.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .zIndex(1f)
                    .clip(CircleShape)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                if (logoUrl.isNotBlank()) {
                    AsyncImage(
                        model = logoUrl.replace("localhost", "10.0.2.2"),
                        contentDescription = name,
                        modifier = Modifier.size(80.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(stringResource(R.string.cd_logo_placeholder), color = Color.White, fontSize = AppFontSize.Small)
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .offset(y = (-40).dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color.Black),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 45.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold,
                        fontSize = AppFontSize.Medium
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.LightGray)
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(text = stringResource(R.string.restaurant_header_rating, rating, reviews), fontSize = AppFontSize.Tiny)
                    Text(
                        text = stringResource(R.string.restaurant_header_avg_price, "%.2f".format(avgPrice)),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = AppFontSize.Tiny
                    )
                }
            }
        }
    }
}
