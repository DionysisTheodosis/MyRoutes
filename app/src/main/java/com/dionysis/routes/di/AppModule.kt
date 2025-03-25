package com.dionysis.routes.di

import android.app.Application
import android.content.Context
import com.dionysis.routes.data.repositories.GpxRepositoryImpl
import com.dionysis.routes.data.repositories.RouteRepositoryImpl
import com.dionysis.routes.domain.repositories.GpxRepository
import com.dionysis.routes.domain.repositories.RouteListener
import com.dionysis.routes.domain.repositories.RouteRepository
import com.dionysis.routes.domain.usecases.DownloadGpxFileUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideGpxRepository(okHttpClient: OkHttpClient): GpxRepository {
        return GpxRepositoryImpl(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideDownloadGpxFileUseCase(gpxRepository: GpxRepository): DownloadGpxFileUseCase {
        return DownloadGpxFileUseCase(gpxRepository)
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideRouteRepository(firestore: FirebaseFirestore): RouteRepository {
        return RouteRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideRouteListener(routeRepository: RouteRepository): RouteListener {
        return routeRepository.createRouteListener()
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

}