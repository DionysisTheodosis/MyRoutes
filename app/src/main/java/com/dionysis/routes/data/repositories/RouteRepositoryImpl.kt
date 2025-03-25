package com.dionysis.routes.data.repositories

import android.util.Log
import com.dionysis.routes.data.dtos.RouteDto
import com.dionysis.routes.data.mappers.toRouteDto
import com.dionysis.routes.data.mappers.toRouteModel
import com.dionysis.routes.domain.models.Route
import com.dionysis.routes.domain.repositories.RouteListener
import com.dionysis.routes.domain.repositories.RouteRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RouteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RouteRepository {

    private val routeListeners = mutableListOf<ListenerRegistration>()

    override suspend fun saveRoute(route: Route) {
        try {
            firestore.collection("routes").add(route.toRouteDto())
                .addOnSuccessListener { documentReference ->
                    Log.d("FirestoreSuccess", "Document added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreError", "Save failed", e)
                }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Exception in saveRoute", e)
            throw e
        }
    }

    override suspend fun getAllRoutes(): List<Route> {
        val routeList = mutableListOf<Route>()
        val snapshot = firestore.collection("routes").get().await()

        for (document in snapshot.documents) {
            val routeDto = document.toObject(RouteDto::class.java)
            val route = routeDto?.toRouteModel()
            route?.let { routeList.add(it) }
        }
        return routeList
    }

     fun listenToRoutesUpdates(onRoutesUpdated: (List<Route>) -> Unit): ListenerRegistration {
        val listener = firestore.collection("routes").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("FirestoreError", "Listen failed", e)
                return@addSnapshotListener
            }
            snapshot?.let { docSnapshot ->
                val updatedRoutes = docSnapshot.documents.mapNotNull { document ->
                    val routeDto = document.toObject(RouteDto::class.java)
                    routeDto?.toRouteModel()
                }
                onRoutesUpdated(updatedRoutes)
            }
        }
        routeListeners.add(listener)
        return listener
    }

    override fun createRouteListener(): RouteListener {
        return object : RouteListener {
            override fun startListening(onRoutesUpdated: (List<Route>) -> Unit) {
                listenToRoutesUpdates(onRoutesUpdated)
            }

            override fun stopListening() {
                clearListeners()
            }
        }
    }

    override fun clearListeners() {
        routeListeners.forEach { it.remove() }
        routeListeners.clear()
    }
}