package com.dionysis.routes.presentation.states

import com.dionysis.routes.domain.models.Route

data class RouteState(
    val isLoading: Boolean = false,
    val routes: List<Route> = emptyList(),
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val routeName: String = "",
    val url: String = "",
    val routeNameError: String = "",
    val urlError: String = "",
    val isDialogVisible: Boolean = false
)
