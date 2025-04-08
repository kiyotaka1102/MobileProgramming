package com.example.photogalleryapp.data.DAO

import androidx.room.*
import com.example.photogalleryapp.data.model.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Query("SELECT * FROM Photo")
    fun getAll(): Flow<List<Photo>>

    @Insert
    suspend fun insert(photo: Photo)

    @Delete
    suspend fun delete(photo: Photo)

}
