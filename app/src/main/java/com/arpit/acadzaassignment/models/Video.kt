package com.arpit.acadzaassignment.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos_table")
data class Video(
    @PrimaryKey
    val id: String,
    val playlistId: String,
    val playlistName: String,
    val playlistThumbnail: String,
    val title: String,
    val channel: String,
    val thumbnail: String
) {
    override fun toString(): String {
        return "\nVideo($title)"
    }
}