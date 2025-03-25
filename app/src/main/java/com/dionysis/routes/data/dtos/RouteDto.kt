package com.dionysis.routes.data.dtos

import com.dionysis.routes.domain.models.Position
import com.google.firebase.firestore.DocumentId

data class RouteDto(
    @DocumentId
    val id: String = ""  ,
    val name: String = "",
    val positions: List<Position> = emptyList()
)
