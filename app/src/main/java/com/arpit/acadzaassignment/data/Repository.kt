package com.arpit.acadzaassignment.data

import com.arpit.acadzaassignment.models.Playlist
import com.arpit.acadzaassignment.models.Video

interface Repository {

    suspend fun getPlaylists(query: String): List<Playlist>

    suspend fun getVideos(
        playlistId: String,
        playlistName: String,
        playlistThumbnail: String
    ): List<Video>

    suspend fun getSavedPlaylists(): List<Playlist>

}