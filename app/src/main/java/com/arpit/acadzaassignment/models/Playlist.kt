package com.arpit.acadzaassignment.models

data class Playlist(
    val id: String,
    val thumbnail: String,
    val title: String,
    val channel: String,
    val videos: List<Video>? = null
)