package com.dionysis.routes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dionysis.routes.domain.models.Route
import com.dionysis.routes.domain.usecases.DownloadGpxFileUseCase
import com.dionysis.routes.domain.usecases.GetAllRouteUseCase
import com.dionysis.routes.domain.usecases.ListenToRouteUpdatesUseCase
import com.dionysis.routes.domain.usecases.ParseGpxFileUseCase
import com.dionysis.routes.domain.usecases.SaveRouteUseCase
import com.dionysis.routes.presentation.actions.RouteActions
import com.dionysis.routes.presentation.states.RouteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val downloadGpxUseCase: DownloadGpxFileUseCase,
    private val parseGpxFileUseCase: ParseGpxFileUseCase,
    private val saveRouteUseCase: SaveRouteUseCase,
    private val getAllRoutesUseCase: GetAllRouteUseCase,
    private val listenToRouteUpdatesUseCase: ListenToRouteUpdatesUseCase,
) : ViewModel() {


    private val _routeState = MutableStateFlow(RouteState())
    val routeState: StateFlow<RouteState> get() = _routeState

    init {
        initLoadRoutes()
        startListeningToRouteUpdates()
    }


    fun initLoadRoutes() {
        viewModelScope.launch(Dispatchers.IO) {
            _routeState.value = _routeState.value.copy(isLoading = true)
            fetchAllRoutes()
        }
    }

    private suspend fun fetchAllRoutes() {
        val result = getAllRoutesUseCase.execute()
        result.onSuccess { routes ->
            _routeState.value = _routeState.value.copy(
                isLoading = false,
                routes = routes,
                successMessage = "Routes loaded successfully"
            )
        }.onFailure {
            _routeState.value = _routeState.value.copy(
                isLoading = false,
                errorMessage = "Failed to load routes"
            )
        }
    }


    fun onAction(action: RouteActions) {
        when (action) {
            is RouteActions.LoadRoutes -> initLoadRoutes()
            is RouteActions.SaveRoute -> saveRouteAction(action.routeName, action.url)
        }
    }


    private fun saveRouteAction(routeName: String, url: String) {

        if (routeName.isBlank()) {
            _routeState.value =
                _routeState.value.copy(routeNameError = "Route name cannot be empty")
            return
        }

        if (!url.startsWith("http")) {
            _routeState.value = _routeState.value.copy(urlError = "Invalid URL format")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _routeState.value = _routeState.value.copy(isLoading = true)

            val downloadResult = downloadGpxUseCase.execute(url)
            if (downloadResult.isFailure) {
                _routeState.value = _routeState.value.copy(
                    isLoading = false,
                    errorMessage = downloadResult.exceptionOrNull()?.message ?: "Download failed"
                )
                return@launch
            }

            val inputStream = downloadResult.getOrThrow()
            val positions = parseGpxFileUseCase.execute(inputStream)
            if (positions.isFailure) {
                _routeState.value = _routeState.value.copy(
                    isLoading = false,
                    errorMessage = positions.exceptionOrNull()?.message ?: "Parsing failed"
                )
                return@launch
            }

            val route = Route(name = routeName, positions = positions.getOrThrow())
            val saveResult = saveRouteUseCase.execute(route)
            if (saveResult.isFailure) {
                _routeState.value = _routeState.value.copy(
                    isLoading = false,
                    errorMessage = saveResult.exceptionOrNull()?.message ?: "Saving route failed"
                )
                return@launch
            }
            _routeState.value = _routeState.value.copy(
                isLoading = false,
                successMessage = "Route saved successfully",
                errorMessage = null
            )
        }
    }

    fun onDialogOpen() {
        _routeState.value = _routeState.value.copy(isDialogVisible = true)
    }
    fun onDialogClose() {
        _routeState.value = _routeState.value.copy(isDialogVisible = false)
    }



    fun onRouteNameChange(newName: String) {
        _routeState.value = _routeState.value.copy(
            routeName = newName,
            routeNameError = if (newName.isNotBlank()) "" else "Route name cannot be empty"
        )
    }


    fun onUrlChange(newUrl: String) {
        _routeState.value = _routeState.value.copy(
            url = newUrl,
            urlError = if (newUrl.startsWith("http")) "" else "Invalid URL format"
        )
    }


    private fun startListeningToRouteUpdates() {
        listenToRouteUpdatesUseCase.startListening { updatedRoutes ->
            _routeState.value = _routeState.value.copy(routes = updatedRoutes)
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenToRouteUpdatesUseCase.stopListening()
    }

    fun clearErrorMessage() {
        _routeState.value = _routeState.value.copy(errorMessage = null)
    }

    fun clearSuccessMessage() {
        _routeState.value = _routeState.value.copy(successMessage = null)
    }
}