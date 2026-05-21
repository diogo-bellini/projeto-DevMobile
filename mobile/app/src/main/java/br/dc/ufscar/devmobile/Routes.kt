package br.dc.ufscar.devmobile

object Routes {
    var register = "register"
    var login = "login"
    var home = "home"
    var search = "search"
    var filters = "filters?category={category}"

    fun filters(category: String? = null): String {
        return if (category != null) {
            "filters?category=$category"
        } else {
            "filters"
        }
    }
    var searchResult = "searchResult?category={category}&price={price}&distance={distance}&minReviews={minReviews}&maxReviews={maxReviews}&minRating={minRating}"
    fun searchResult(
        category: String? = null,
        price: Float? = null,
        distance: Float? = null,
        minReviews: Int? = null,
        maxReviews: Int? = null,
        minRating: Int? = null
    ): String {
        val params = mutableListOf<String>()
        category?.let { params.add("category=$it") }
        price?.let { params.add("price=$it") }
        distance?.let { params.add("distance=$it") }
        minReviews?.let { params.add("minReviews=$it") }
        maxReviews?.let { params.add("maxReviews=$it") }
        minRating?.let { params.add("minRating=$it") }

        return if (params.isEmpty()) {
            "searchResult"
        } else {
            "searchResult?${params.joinToString("&")}"
        }
    }
    var restaurantHome = "restaurantHome/{storeId}"
    fun restaurantHome(storeId: Int) = "restaurantHome/$storeId"
    var restaurantMenu = "restaurantMenu/{storeId}"
    fun restaurantMenu(storeId: Int) = "restaurantMenu/$storeId"
    var reserve = "reserve/{storeId}"
    fun reserve(storeId: Int) = "reserve/$storeId"
    var reserveConfirmation = "reserveConfirmation"
    var profile = "profile"
}