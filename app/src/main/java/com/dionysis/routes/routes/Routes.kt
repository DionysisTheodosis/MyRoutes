package com.dionysis.routes.routes

import kotlinx.serialization.Serializable

sealed interface Routes {
    val route: String

    @Serializable
    data object MainScreen : Routes {
        override val route: String = "main_screen"
    }

    @Serializable
    data object MapScreen : Routes {
        override val route: String = "map_screen"
    }
    
}