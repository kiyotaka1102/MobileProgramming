package com.example.photogalleryapp.data.repository

import com.example.photogalleryapp.data.DAO.PhotoDao
import com.example.photogalleryapp.data.model.Photo

class PhotoRepository(private val dao: PhotoDao) {
    fun getAllPhotos() = dao.getAll()

    suspend fun insertPhoto(photo: Photo) = dao.insert(photo)

    suspend fun deletePhoto(photo: Photo) = dao.delete(photo)

    suspend fun insertSamplePhotos() {
        val samplePhotos = listOf(
            Photo(uri = "android.resource://com.example.photogalleryapp/drawable/sample1"),
            Photo(uri = "android.resource://com.example.photogalleryapp/drawable/sample2"),
            Photo(uri = "android.resource://com.example.photogalleryapp/drawable/sample3")
        )
        samplePhotos.forEach { dao.insert(it) }
    }
}
