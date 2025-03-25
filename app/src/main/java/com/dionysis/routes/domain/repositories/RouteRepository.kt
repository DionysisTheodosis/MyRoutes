package com.dionysis.routes.domain.repositories

import com.dionysis.routes.domain.models.Route

interface RouteRepository {
    suspend fun saveRoute(route: Route)
    suspend fun getAllRoutes(): List<Route>
    fun createRouteListener(): RouteListener
    fun clearListeners()
}