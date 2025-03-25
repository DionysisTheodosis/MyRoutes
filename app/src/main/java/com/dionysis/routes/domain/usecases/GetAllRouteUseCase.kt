package com.dionysis.routes.domain.usecases

import com.dionysis.routes.domain.models.Route
import com.dionysis.routes.domain.repositories.RouteRepository
import javax.inject.Inject

class GetAllRouteUseCase@Inject constructor(private val routeRepository: RouteRepository) {
    suspend fun execute(): Result<List<Route>> {
        return try {
            val routes = routeRepository.getAllRoutes()
            Result.success(routes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}