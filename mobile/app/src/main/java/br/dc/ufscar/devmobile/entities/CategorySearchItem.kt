package br.dc.ufscar.devmobile.entities

import br.dc.ufscar.devmobile.R

data class CategorySearchItem(
    val title : Int,
    val icon : Int
)

val categorySearchItems = listOf(
    CategorySearchItem(title = R.string.arabe_category, R.drawable.arabe_category),
    CategorySearchItem(title = R.string.doces_category, R.drawable.doces_category),
    CategorySearchItem(title = R.string.pizza_category, R.drawable.pizza_category),
    CategorySearchItem(title = R.string.hotdog_category, R.drawable.hotdog_category),
    CategorySearchItem(title = R.string.brasileira_category, R.drawable.brasileira_category),
    CategorySearchItem(title = R.string.chinesa_category, R.drawable.chinesa_category),
    CategorySearchItem(title = R.string.italiana_category, R.drawable.italiana_category),
    CategorySearchItem(title = R.string.japonesa_category, R.drawable.japonesa_category),
    CategorySearchItem(title = R.string.lanches_category, R.drawable.lanches_category),
    CategorySearchItem(title = R.string.mexicana_category, R.drawable.mexicana_category),
    CategorySearchItem(title = R.string.peixes_category, R.drawable.peixes_category),
    CategorySearchItem(title = R.string.salada_category, R.drawable.salada_category),
    CategorySearchItem(title = R.string.salgados_category, R.drawable.salgados_category),
    CategorySearchItem(title = R.string.tailandesa_category, R.drawable.tailandesa_category),
    CategorySearchItem(title = R.string.vegana_category, R.drawable.vegana_category),
    CategorySearchItem(title = R.string.vegetariana_category, R.drawable.vegetariana_category),
)