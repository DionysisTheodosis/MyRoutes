package com.dionysis.routes.domain.usecases

import com.dionysis.routes.domain.models.Route
import com.dionysis.routes.domain.repositories.RouteListener
import com.dionysis.routes.domain.repositories.RouteRepository
import javax.inject.Inject

class ListenToRouteUpdatesUseCase @Inject constructor(
    private val routeRepository: RouteRepository
) {

    private var routeListener: RouteListener? = null

    fun startListening(onRoutesUpdated: (List<Route>) -> Unit) {
        routeListener = routeRepository.createRouteListener()
        routeListener?.startListening(onRoutesUpdated)
    }

    fun stopListening() {
        routeListener?.stopListening()
        routeListener = null
    }
}