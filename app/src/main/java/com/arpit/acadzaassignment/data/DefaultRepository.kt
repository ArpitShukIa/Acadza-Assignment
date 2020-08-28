package com.arpit.acadzaassignment.data

import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.arpit.acadzaassignment.Application
import com.arpit.acadzaassignment.models.Playlist
import com.arpit.acadzaassignment.models.Video
import com.arpit.acadzaassignment.util.getApiKey
import org.json.JSONObject
import timber.log.Timber

class DefaultRepository(private val application: Application) : Repository {

    private lateinit var requestQueue: RequestQueue

    override suspend fun getPlaylists(query: String): List<Playlist> {
        requestQueue = Volley.newRequestQueue(application)
        val url = queryUrlFormat.format(query)
        return jsonParseQuery(url)
    }

    override suspend fun getVideos(playlistId: String): List<Video> {
        requestQueue = Volley.newRequestQueue(application)
        return jsonParseVideos(playlistId)
    }

    override suspend fun getSavedPlaylists(): List<Playlist> {
        val videos = application.playlistDao.getAllVideos()
        playlistsHashMap = HashMap()
        for (video in videos) {
            if (playlistsHashMap.containsKey(video.playlistId))
                playlistsHashMap[video.playlistId]?.add(video)
            else
                playlistsHashMap[video.playlistId] = mutableListOf(video)
        }
        val playlistIds = playlistsHashMap.keys.joinToString()
        val url = playlistsUrlFormat.format(playlistIds)
        requestQueue = Volley.newRequestQueue(application)
        return jsonParsePlaylists(url)
    }

    private fun jsonParsePlaylists(url: String): List<Playlist> {
        val playlists = mutableListOf<Playlist>()
        val future = RequestFuture.newFuture<JSONObject>()
        val request = JsonObjectRequest(url, null, future, future)
        requestQueue.add(request)
        try {
            val response = future.get()
            val items = response.getJSONArray("items")
            for (i in 0 until items.length()) {
                val data = items.getJSONObject(i)
                val snippet = data.getJSONObject("snippet")
                val thumbnail = snippet.getJSONObject("thumbnails").getJSONObject("high")

                val id = data["id"].toString()
                val title = snippet["title"].toString()
                val channel = snippet["channelTitle"].toString()
                val image = thumbnail["url"].toString()
                val playlist = Playlist(id, image, title, channel, playlistsHashMap[id])
                playlists.add(playlist)
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
        }
        return playlists
    }

    private fun jsonParseVideos(playlistId: String): List<Video> {
        val url = videosUrlFormat.format(playlistId)
        val videos = mutableListOf<Video>()
        val future = RequestFuture.newFuture<JSONObject>()
        val request = JsonObjectRequest(url, null, future, future)
        requestQueue.add(request)
        try {
            val response = future.get()
            val items = response.getJSONArray("items")
            for (i in 0 until items.length()) {
                val data = items.getJSONObject(i)
                val snippet = data.getJSONObject("snippet")
                val thumbnail = snippet.getJSONObject("thumbnails").getJSONObject("high")
                val title = snippet["title"].toString()
                val channel = snippet["channelTitle"].toString()
                val image = thumbnail["url"].toString()
                val id = snippet.getJSONObject("resourceId")["videoId"].toString()
                val video = Video(id, playlistId, title, channel, image)
                videos.add(video)
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
        }
        return videos
    }

    private fun jsonParseQuery(url: String): List<Playlist> {
        val playlists = mutableListOf<Playlist>()
        val future = RequestFuture.newFuture<JSONObject>()
        val request = JsonObjectRequest(url, null, future, future)
        requestQueue.add(request)
        try {
            val response = future.get()
            val items = response.getJSONArray("items")
            for (i in 0 until items.length()) {
                val data = items.getJSONObject(i)
                val snippet = data.getJSONObject("snippet")
                val thumbnail = snippet.getJSONObject("thumbnails").getJSONObject("high")

                val id = data.getJSONObject("id")["playlistId"].toString()
                val title = snippet["title"].toString()
                val channel = snippet["channelTitle"].toString()
                val image = thumbnail["url"].toString()
                val playlist = Playlist(id, image, title, channel)
                playlists.add(playlist)
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
        }
        return playlists
    }

    companion object {
        private const val TAG = "TAG DefaultRepository"
        private val API_KEY = getApiKey()
        private val queryUrlFormat =
            "https://www.googleapis.com/youtube/v3/search?part=snippet&q=%s&type=playlist&maxResults=50&key=$API_KEY"
        private val videosUrlFormat =
            "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=%s&maxResults=50&key=$API_KEY"
        private val playlistsUrlFormat =
            "https://www.googleapis.com/youtube/v3/playlists?part=snippet&id=%s&maxResults=50&key=$API_KEY"
        private lateinit var playlistsHashMap: HashMap<String, MutableList<Video>>
    }

}