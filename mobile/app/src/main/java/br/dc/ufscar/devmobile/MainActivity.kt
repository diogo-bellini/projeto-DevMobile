package br.dc.ufscar.devmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import br.dc.ufscar.devmobile.configs.UPeekDatabase
import br.dc.ufscar.devmobile.ui.theme.UPeekTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UPeekTheme{
                MainAppNavigation()
            }
        }
    }
}