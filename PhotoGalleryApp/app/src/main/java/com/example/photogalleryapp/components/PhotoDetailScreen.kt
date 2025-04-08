package com.example.photogalleryapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.graphicsLayer

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.photogalleryapp.viewmodel.GalleryViewModel

@Composable
fun PhotoDetailScreen(index: Int, viewModel: GalleryViewModel, onBack: () -> Unit) {
    val photos by viewModel.photos.collectAsState()
    var currentIndex by remember { mutableStateOf(index) }

    if (currentIndex !in photos.indices) return

    val photo = photos[currentIndex]
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Box(
            Modifier
                .weight(1f)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale *= zoom
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                }
                .pointerInput(currentIndex) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        if (dragAmount > 50 && currentIndex > 0) currentIndex--
                        else if (dragAmount < -50 && currentIndex < photos.lastIndex) currentIndex++
                    }
                }
        ) {
            AsyncImage(
                model = photo.uri,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
                    .graphicsLayer(scaleX = scale, scaleY = scale, translationX = offsetX, translationY = offsetY)
            )
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { if (currentIndex > 0) currentIndex-- }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
            }
            Button(onClick = { if (currentIndex < photos.lastIndex) currentIndex++ }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
            }
        }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text("Back to Gallery")
        }
    }
}
