package com.example.photogalleryapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.photogalleryapp.data.db.PhotoDatabase
import com.example.photogalleryapp.data.model.Photo
import com.example.photogalleryapp.data.repository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PhotoRepository

    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos = _photos.asStateFlow()

    private var pickerLauncher: (() -> Unit)? = null

    init {
        val dao = PhotoDatabase.getDatabase(application).photoDao()
        repository = PhotoRepository(dao)

        viewModelScope.launch {
            repository.getAllPhotos().collect {
                _photos.value = it

                // Only insert sample photos if the list is empty
                if (it.isEmpty()) {
                    repository.insertSamplePhotos()
                }
            }
        }
    }

    fun addPhoto(uri: String) {
        viewModelScope.launch {
            repository.insertPhoto(Photo(uri = uri))
        }
    }

    fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            repository.deletePhoto(photo)
        }
    }

    fun setPickerLauncher(launcher: () -> Unit) {
        pickerLauncher = launcher
    }

    fun launchPicker() {
        pickerLauncher?.invoke()
    }
}
