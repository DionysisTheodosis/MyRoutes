package com.dionysis.routes.domain.models



data class Route(
    val id: String = ""  ,  // Default value
    val name: String = "",  // Default value
    val positions: List<Position> = emptyList() // Default to empty list
)
