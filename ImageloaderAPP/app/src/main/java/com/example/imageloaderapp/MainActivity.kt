package com.example.imageloaderapp

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<ImageResult> {

    private lateinit var urlEditText: EditText
    private lateinit var loadButton: Button
    private lateinit var imageView: ImageView
    private lateinit var statusTextView: TextView

    private val networkReceiver = NetworkChangeReceiver()
    private val IMAGE_LOADER_ID = 1

    // Runtime permission launcher for notifications (Android 13+)
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) startImageLoaderService()
        else statusTextView.text = "Notification permission denied"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        urlEditText = findViewById(R.id.url_edit_text)
        loadButton = findViewById(R.id.load_button)
        imageView = findViewById(R.id.image_view)
        statusTextView = findViewById(R.id.status_text_view)

        // Set default image URL
        urlEditText.setText("https://picsum.photos/800/600")

        loadButton.setOnClickListener {
            loadImage()
        }

        // Init the image loader
        LoaderManager.getInstance(this).initLoader(IMAGE_LOADER_ID, null, this)

        // Handle permission and start foreground service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                        == PackageManager.PERMISSION_GRANTED -> {
                    startImageLoaderService()
                }
                else -> {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            startImageLoaderService()
        }

        // Register network change receiver
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        updateNetworkStatus(isNetworkAvailable())
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
    }

    private fun startImageLoaderService() {
        val intent = Intent(this, ImageLoaderService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    private fun loadImage() {
        val url = urlEditText.text.toString().trim()
        if (url.isEmpty()) {
            statusTextView.text = "Please enter a valid URL"
            return
        }

        statusTextView.text = "Loading..."
        imageView.setImageBitmap(null)

        val args = Bundle().apply { putString("url", url) }
        LoaderManager.getInstance(this).restartLoader(IMAGE_LOADER_ID, args, this)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun updateNetworkStatus(isConnected: Boolean) {
        if (isConnected) {
            loadButton.isEnabled = true
            statusTextView.text = "Ready to load image"
        } else {
            loadButton.isEnabled = false
            statusTextView.text = "No internet connection"
        }
    }

    // Loader callbacks
    override fun onCreateLoader(id: Int, args: Bundle?): ImageLoader {
        val url = args?.getString("url") ?: ""
        return ImageLoader(this, url)
    }

    override fun onLoadFinished(loader: Loader<ImageResult>, result: ImageResult) {
        if (result.bitmap != null) {
            imageView.setImageBitmap(result.bitmap)
            statusTextView.text = "Image loaded successfully"
        } else {
            statusTextView.text = result.errorMessage ?: "Failed to load image"
        }
    }

    override fun onLoaderReset(loader: Loader<ImageResult>) {
        imageView.setImageBitmap(null)
    }

    // Network change broadcast receiver
    inner class NetworkChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                updateNetworkStatus(isNetworkAvailable())
            }
        }
    }
}
