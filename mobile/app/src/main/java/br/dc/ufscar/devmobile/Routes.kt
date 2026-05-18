package br.dc.ufscar.devmobile

object Routes {
    var register = "register"
    var login = "login"
    var home = "home"
    var search = "search"
    var filters = "filters"
    var searchResult = "searchResult"
    var restaurantHome = "restaurantHome/{storeId}"
    fun restaurantHome(storeId: Int) = "restaurantHome/$storeId"
    var restaurantMenu = "restaurantMenu/{storeId}"
    fun restaurantMenu(storeId: Int) = "restaurantMenu/$storeId"
    var reserve = "reserve/{storeId}"
    fun reserve(storeId: Int) = "reserve/$storeId"
    var reserveConfirmation = "reserveConfirmation"

    var profile = "profile"
}