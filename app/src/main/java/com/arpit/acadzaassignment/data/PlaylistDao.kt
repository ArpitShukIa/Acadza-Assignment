package com.arpit.acadzaassignment.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arpit.acadzaassignment.models.Video

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVideo(video: Video)

    @Query("SELECT * FROM videos_table")
    fun getAllVideos(): List<Video>

}