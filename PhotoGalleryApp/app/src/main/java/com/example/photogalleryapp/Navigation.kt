package com.example.photogalleryapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.photogalleryapp.components.GalleryScreen
import com.example.photogalleryapp.components.PhotoDetailScreen
import com.example.photogalleryapp.viewmodel.GalleryViewModel

@Composable
fun Navigation(viewModel: GalleryViewModel) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "gallery") {
        composable("gallery") {
            GalleryScreen(viewModel, onPhotoClick = { index ->
                navController.navigate("detail/$index")
            })
        }
        composable("detail/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
            PhotoDetailScreen(index, viewModel, onBack = { navController.popBackStack() })
        }
    }
}