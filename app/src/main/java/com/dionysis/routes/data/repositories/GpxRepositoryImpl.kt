package com.dionysis.routes.data.repositories

import com.dionysis.routes.domain.models.Position
import com.dionysis.routes.domain.repositories.GpxRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import javax.inject.Inject

class GpxRepositoryImpl @Inject constructor(
    private val client: OkHttpClient
) : GpxRepository {

    override suspend fun downloadGpxFile(url: String): InputStream {
        val request = Request.Builder()
            .url(url)
            .build()

        val response: Response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw Exception("Failed to download GPX file: ${response.message}")
        }

        return response.body.byteStream()
    }

    override  fun parseGpxFile(inputStream: InputStream): List<Position> {
        val positions : MutableList<Position> = mutableListOf()

        try {
            val factory = XmlPullParserFactory.newInstance()
            val parser: XmlPullParser = factory.newPullParser()
            parser.setInput(inputStream, null)

            var eventType = parser.eventType
            var lat: Double? = null
            var lon: Double? = null

            var counter = 0
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (parser.name == "trkpt") {
                            lat = parser.getAttributeValue(null, "lat")?.toDouble()
                            lon = parser.getAttributeValue(null, "lon")?.toDouble()
                            counter++
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "trkpt" && lat != null && lon != null) {
                            positions+=Position(latitude = lat, longitude = lon)
                            lat = null
                            lon = null
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
           throw Exception("Failed to parse GPX file: ${e.message}")
        } finally {
            inputStream.close()
        }
        return positions
    }
}