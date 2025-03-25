package com.dionysis.routes.presentation.actions

interface RouteActions {
    data object LoadRoutes : RouteActions
    data class SaveRoute(val routeName: String, val url : String) : RouteActions
}