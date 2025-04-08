package com.example.photogalleryapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uri: String
)
