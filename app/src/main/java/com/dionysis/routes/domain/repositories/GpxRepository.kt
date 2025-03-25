package com.dionysis.routes.domain.repositories

import com.dionysis.routes.domain.models.Position
import java.io.InputStream

interface GpxRepository {
    suspend fun downloadGpxFile(url: String): InputStream
     fun parseGpxFile(inputStream: InputStream): List<Position>
}