package br.dc.ufscar.devmobile.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import br.dc.ufscar.devmobile.R

@Composable
fun ReserveScreen(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // 1. Imagem de Fundo com Blur
        Image(
            painter = painterResource(id = R.drawable.restaurant_banner),
            contentDescription = "Banner de comida japonesa",
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .blur(radius = 2.dp),
            contentScale = ContentScale.Crop
        )

        // 2. Coluna que organiza o Logo e o Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 110.dp), // Posiciona o centro do logo na borda da imagem
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // LOGO CIRCULAR (Com zIndex para ficar na frente da borda do card)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .zIndex(1f) // Faz o logo ficar por cima da linha do card
                    .clip(CircleShape)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ){
                Text("LOGO", color = Color.White, fontSize = 12.sp)
            }

            // 3. O CARD COM CONTORNO
            // Usamos o offset negativo para "puxar" o card para cima do logo
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .offset(y = (-40).dp), // Sobe o card para o meio do logo
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color.Black), // O contorno que você pediu
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 45.dp), // top padding maior para não cobrir o texto
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Sushi Fan - Fanáticos por sushi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "1,2 km",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // LINHA CINZA (Limitada ao padding do Card)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.LightGray)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "⭐ 4,7 (823 avaliações)", fontSize = 14.sp)
                    Text(
                        text = "Média de R$ 34,70/pessoa",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}