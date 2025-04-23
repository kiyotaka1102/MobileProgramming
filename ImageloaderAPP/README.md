# Image Loader App

A simple Android application that demonstrates loading images from the web using various Android components including AsyncTaskLoader, Services, Broadcasts, and Notifications.

## Features

- Load images from any URL
- Background image loading with AsyncTaskLoader
- Foreground service with periodic notifications
- Network connectivity monitoring
- Runtime permission handling

## Setup Requirements

### Dependencies

Add these dependencies to your `build.gradle` file:

```gradle
dependencies {
    implementation 'androidx.core:core-ktx:1.10.1'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.loader:loader:1.1.0'
    implementation 'com.google.android.material:material:1.9.0'
}
```

### Permissions

Add the following permissions to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_DATA_SYNC" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

### Service Declaration

Declare the foreground service in your `AndroidManifest.xml`:

```xml
<application>
    <!-- Other application components -->
    
    <service
        android:name=".ImageLoaderService"
        android:enabled="true"
        android:exported="false"
        android:foregroundServiceType="dataSync" />
</application>
```

## Running the App

1. Clone the repository or download the source code
2. Open the project in Android Studio
3. Build and run the app on an emulator or physical device
4. Enter a valid image URL (default is provided)
5. Press the "Load" button to fetch and display the image

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
- On Android 13+, ensure notification permissions are granted
- For devices with battery optimization, you may need to exempt the app
- Check the status text view for detailed error messages

## License

[MIT License](LICENSE)