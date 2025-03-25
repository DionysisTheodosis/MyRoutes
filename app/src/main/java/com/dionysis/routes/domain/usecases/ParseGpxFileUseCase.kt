package com.dionysis.routes.domain.usecases

import com.dionysis.routes.domain.models.Position
import com.dionysis.routes.domain.repositories.GpxRepository
import java.io.InputStream
import javax.inject.Inject

class ParseGpxFileUseCase @Inject constructor(private val gpxRepository: GpxRepository) {
    fun execute(inputStream: InputStream): Result<List<Position>> {
        return try {
            val positions = gpxRepository.parseGpxFile(inputStream)
            Result.success(positions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}