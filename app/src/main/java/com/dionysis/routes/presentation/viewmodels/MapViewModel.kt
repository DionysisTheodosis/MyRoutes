package com.dionysis.routes.presentation.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dionysis.routes.domain.models.Position
import com.dionysis.routes.presentation.states.MapState
import com.dionysis.routes.service.LocationManager
import com.dionysis.routes.service.LocationTrackerService
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val locationManager: LocationManager
) : ViewModel() {


    private val _mapState = MutableStateFlow(MapState())
    val mapState: StateFlow<MapState> get() = _mapState
    private val _cameraPosition = MutableStateFlow(LatLng(0.0, 0.0))

    fun startLocationTracking() {
        Log.d("MapViewModel", "Starting location tracking")
        Intent(context, LocationTrackerService::class.java).also {
            it.action = LocationTrackerService.Action.START.name
            context.startService(it)
        }
        viewModelScope.launch {
            locationManager.trackLocation().collect { location ->
                location.let {
                    val newLatLng = LatLng(it.latitude, it.longitude)
                    updateLocation(newLatLng)
                }
            }
        }
    }

    fun stopLocationTracking() {
        Intent(context, LocationTrackerService::class.java).also {
            it.action = LocationTrackerService.Action.STOP.name
            context.startService(it)
        }
    }


    private fun updateLocation(latLng: LatLng) {
        if (_mapState.value.currentLocation != latLng) {
            _mapState.value = _mapState.value.copy(
                currentLocation = latLng,
                dynamicPositions = _mapState.value.dynamicPositions + latLng // Append new location
            )
        }
    }


    private fun List<Position>.toLatLngList(): List<LatLng> {
        return this.map { position -> LatLng(position.latitude, position.longitude) }
    }

    fun setRouteData(routeName: String, positions: List<Position>) {
        _mapState.value = _mapState.value.copy(
            routeName = routeName,
            polylinePositions = positions.toLatLngList()
        )
        _mapState.value = _mapState.value.copy(isLoading = false)
        _cameraPosition.value =
            _mapState.value.polylinePositions[(_mapState.value.polylinePositions.size) / 2]
    }

    fun createLatLngBounds(points: List<LatLng>): LatLngBounds {
        val builder = LatLngBounds.Builder()
        for (point in points) {
            builder.include(point)
        }
        return builder.build()
    }


    fun calculateAverageLatLng(positions: List<LatLng>): LatLng {
        return if (positions.isNotEmpty()) {
            positions[(positions.size) / 2]
        } else {
            LatLng(0.0, 0.0)
        }
    }

    fun setLocationPermision(hasLocationPermission: Boolean){
        _mapState.value = _mapState.value.copy(permissionState = hasLocationPermission)
    }
    override fun onCleared() {
        super.onCleared()
        stopLocationTracking()
        clearDynamicPositions()
    }

    fun clearDynamicPositions() {
        _mapState.value = _mapState.value.copy(dynamicPositions = emptyList())
    }

}