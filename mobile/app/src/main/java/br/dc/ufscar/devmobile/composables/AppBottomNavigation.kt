package br.dc.ufscar.devmobile.composables

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.dc.ufscar.devmobile.entities.BottomNavItem

@Composable
fun AppBottomNavigation(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            val title = stringResource(id = item.title)

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = title) },
                label = { Text(title) }
            )
        }
    }
}