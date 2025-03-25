package com.dionysis.routes.domain.usecases

import com.dionysis.routes.domain.models.Route
import com.dionysis.routes.domain.repositories.RouteRepository
import javax.inject.Inject


class SaveRouteUseCase @Inject constructor(private val routeRepository: RouteRepository) {
    suspend fun execute(route: Route): Result<Unit> {
        return try {
            routeRepository.saveRoute(route)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}