package com.dionysis.routes.data.mappers

import com.dionysis.routes.data.dtos.RouteDto
import com.dionysis.routes.domain.models.Route

fun RouteDto.toRouteModel(): Route {
    return Route(
        id = this.id,
        name = this.name,
        positions = this.positions
    )
}


fun Route.toRouteDto(): RouteDto {
    return RouteDto(
        id = this.id,
        name = this.name,
        positions = this.positions
    )
}