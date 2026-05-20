package br.dc.ufscar.devmobile.entities

import br.dc.ufscar.devmobile.R

data class CategorySearchItem(
    val title : Int,
    val icon : Int,
    val backendName: String
)

val categorySearchItems = listOf(
    CategorySearchItem(title = R.string.arabe_category, R.drawable.arabe_category, "Árabe"),
    CategorySearchItem(title = R.string.doces_category, R.drawable.doces_category, "Doces"),
    CategorySearchItem(title = R.string.pizza_category, R.drawable.pizza_category, "Pizza"),
    CategorySearchItem(title = R.string.hotdog_category, R.drawable.hotdog_category, "Hot Dog"),
    CategorySearchItem(title = R.string.brasileira_category, R.drawable.brasileira_category, "Brasileira"),
    CategorySearchItem(title = R.string.chinesa_category, R.drawable.chinesa_category, "Chinesa"),
    CategorySearchItem(title = R.string.italiana_category, R.drawable.italiana_category, "Italiana"),
    CategorySearchItem(title = R.string.japonesa_category, R.drawable.japonesa_category, "Japonesa"),
    CategorySearchItem(title = R.string.lanches_category, R.drawable.lanches_category, "Lanches"),
    CategorySearchItem(title = R.string.mexicana_category, R.drawable.mexicana_category, "Mexicana"),
    CategorySearchItem(title = R.string.peixes_category, R.drawable.peixes_category, "Peixes"),
    CategorySearchItem(title = R.string.salada_category, R.drawable.salada_category, "Salada"),
    CategorySearchItem(title = R.string.salgados_category, R.drawable.salgados_category, "Salgados"),
    CategorySearchItem(title = R.string.tailandesa_category, R.drawable.tailandesa_category, "Tailandesa"),
    CategorySearchItem(title = R.string.vegana_category, R.drawable.vegana_category, "Vegana"),
    CategorySearchItem(title = R.string.vegetariana_category, R.drawable.vegetariana_category, "Vegetariana"),
)