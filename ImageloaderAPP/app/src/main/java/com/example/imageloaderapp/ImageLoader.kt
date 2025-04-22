package com.example.imageloaderapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.loader.content.AsyncTaskLoader
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

// Data class to hold the result of loading image
data class ImageResult(val bitmap: Bitmap? = null, val errorMessage: String? = null)

class ImageLoader(context: Context, private val imageUrl: String) : AsyncTaskLoader<ImageResult>(context) {

    private var cachedResult: ImageResult? = null

    override fun onStartLoading() {
        if (cachedResult != null) {
            deliverResult(cachedResult)
        } else {
            forceLoad()
        }
    }

    override fun loadInBackground(): ImageResult {
        try {
            if (imageUrl.isEmpty()) {
                return ImageResult(errorMessage = "URL is empty")
            }

            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            connection.readTimeout = 15000
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return ImageResult(errorMessage = "Server returned error code: $responseCode")
            }

            val inputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            connection.disconnect()

            return if (bitmap != null) {
                ImageResult(bitmap = bitmap)
            } else {
                ImageResult(errorMessage = "Failed to decode image")
            }
        } catch (e: IOException) {
            return ImageResult(errorMessage = "Network error: ${e.message}")
        } catch (e: Exception) {
            return ImageResult(errorMessage = "Error: ${e.message}")
        }
    }

    override fun deliverResult(data: ImageResult?) {
        cachedResult = data
        super.deliverResult(data)
    }

    override fun onReset() {
        super.onReset()
        cachedResult = null
    }
}
