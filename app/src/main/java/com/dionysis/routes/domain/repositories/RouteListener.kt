package com.dionysis.routes.domain.repositories

import com.dionysis.routes.domain.models.Route

interface RouteListener {
    fun startListening(onRoutesUpdated: (List<Route>) -> Unit)
    fun stopListening()
}