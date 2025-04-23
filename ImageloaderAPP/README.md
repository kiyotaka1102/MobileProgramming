# Image Loader App

A simple Android application that demonstrates loading images from the web using various Android components including AsyncTaskLoader, Services, Broadcasts, and Notifications.

## Features

- Load images from any URL
- Background image loading with AsyncTaskLoader
- Foreground service with periodic notifications
- Network connectivity monitoring
- Runtime permission handling

## Project Setup

### Gradle Configuration

The project uses Kotlin DSL for Gradle. The main `build.gradle.kts` configuration:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.imageloaderapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.imageloaderapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
```

### Required Permissions

The app requires the following permissions in `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_DATA_SYNC" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
```

## Running the App

1. Clone the repository or download the source code
2. Open the project in Android Studio
3. Ensure you have SDK 35 installed (or modify the compileSdk in build.gradle.kts)
4. Build and run the app on an emulator or physical device with API level 24 or higher
5. Enter a valid image URL (default is provided - https://picsum.photos/800/600)
6. Press the "Load" button to fetch and display the image

## Component Implementation Details

### AsyncTaskLoader

The `ImageLoader` class extends `AsyncTaskLoader<ImageResult>` to perform network operations in the background:

- **Initialization**: Created with a context and image URL
- **Background Processing**: `loadInBackground()` method downloads the image from the network
- **Result Delivery**: `deliverResult()` caches and delivers the result to the UI
- **Caching**: Stores the last loaded result to avoid repeated downloads
- **Error Handling**: Returns descriptive error messages if image loading fails

### Service

The `ImageLoaderService` is a foreground service that:

- Starts when the app is launched (after permission checks)
- Creates a notification channel for Android Oreo and above
- Displays a persistent notification to indicate it's running
- Implements periodic notifications every 5 minutes
- Handles foreground service type based on Android version

### Broadcast Receiver

The `NetworkChangeReceiver` monitors network connectivity:

- Registered in `MainActivity` to listen for `ConnectivityManager.CONNECTIVITY_ACTION`
- Updates the UI based on network availability
- Enables/disables the load button based on connection status

### Notifications

Notifications are implemented in the following ways:

- **Channel Creation**: Creates a notification channel for Android Oreo+
- **Foreground Service**: Displays a persistent notification while the service runs
- **Periodic Updates**: Updates the notification every 5 minutes
- **Intent Handling**: Includes a PendingIntent to open the app when notification is tapped

### Runtime Permissions

For Android 13 and above, the app requests the `POST_NOTIFICATIONS` permission:

- Uses `ActivityResultContracts.RequestPermission()` for clean permission handling
- Starts the foreground service only if permission is granted
- Provides user feedback if permission is denied

## Architecture Overview

The app follows a simple architecture:

1. **MainActivity**: UI controller that manages user interactions
2. **ImageLoader**: Background loader that fetches images from the web
3. **ImageLoaderService**: Long-running service for background operations
4. **NetworkChangeReceiver**: Monitors network connectivity changes

## Troubleshooting

- If images fail to load, check your internet connection
- On Android 13+ (API 33+), ensure notification permissions are granted
- For devices with battery optimization, you may need to exempt the app
- Check the status text view for detailed error messages

## Layout Requirements

Create a layout file `activity_main.xml` with the following components:
- EditText with id `url_edit_text` for entering image URLs
- Button with id `load_button` for triggering image loading
- ImageView with id `image_view` for displaying the loaded image
- TextView with id `status_text_view` for showing loading status and errors

Don't forget to create a drawable resource `ic_notification` for the service notification icon.

## License

[MIT License](LICENSE)
