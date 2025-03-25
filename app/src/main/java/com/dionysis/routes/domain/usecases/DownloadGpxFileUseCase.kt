package com.dionysis.routes.domain.usecases

import com.dionysis.routes.domain.repositories.GpxRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject

class DownloadGpxFileUseCase @Inject constructor(
    private val gpxRepository: GpxRepository
) {
    suspend fun execute(url: String): Result<InputStream> {
        return try {
            val inputStream = withContext(Dispatchers.IO) {
                gpxRepository.downloadGpxFile(url)
            }
            Result.success(inputStream)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}