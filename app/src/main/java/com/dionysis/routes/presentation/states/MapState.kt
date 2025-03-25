package com.dionysis.routes.presentation.states

import com.dionysis.routes.domain.models.Position
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.LatLng


data class MapState(
    val isLoading: Boolean = true,
    val positions: List<Position> = emptyList(),
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val currentLocation: LatLng? = null,
    val routeName: String? = null,
    val dynamicPositions: List<LatLng> = emptyList(),
    val permissionState: Boolean = false,
    val polylinePositions: List<LatLng> = emptyList(),
    val routePosition: LatLng? = null,
    val cameraPosition:CameraUpdate? = null
)
