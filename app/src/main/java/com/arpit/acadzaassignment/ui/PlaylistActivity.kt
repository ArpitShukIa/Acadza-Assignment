package com.arpit.acadzaassignment.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.arpit.acadzaassignment.Application
import com.arpit.acadzaassignment.R
import com.arpit.acadzaassignment.adapters.VideoAdapter
import com.arpit.acadzaassignment.databinding.ActivityPlaylistBinding
import com.arpit.acadzaassignment.models.Video
import com.arpit.acadzaassignment.util.getApiKey
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.coroutines.*
import timber.log.Timber

class PlaylistActivity : YouTubeBaseActivity() {

    private val TAG = "TAG PlaylistActivity"

    private lateinit var binding: ActivityPlaylistBinding

    private val job = Job()

    private var videos: List<Video> = ArrayList()
    private var nextVideos = videos
    private var index = 0
    private var playlistId: String = ""
    private var playlistName: String = ""
    private var playlistThumbnail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_playlist)

        playlistId = intent.extras?.getString("playlistId")!!
        playlistName = intent.extras?.getString("playlistName")!!
        playlistThumbnail = intent.extras?.getString("playlistThumbnail")!!

        binding.videos.adapter = VideoAdapter()
        loadVideos()

        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.video_player)
        youTubePlayerView.initialize(getApiKey(), object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                p1?.let {
                    try {
                        p1.setShowFullscreenButton(false)
                        p1.loadPlaylist(playlistId)
                        p1.setPlayerStateChangeListener(object :
                            YouTubePlayer.PlayerStateChangeListener {
                            override fun onLoading() {}

                            override fun onLoaded(p0: String?) {}

                            override fun onAdStarted() {}

                            override fun onVideoStarted() {
                                val video = videos[index]
                                binding.title.text = video.title
                                binding.channel.text = video.channel
                                if (index < videos.size - 1) {
                                    binding.nextText.text = getString(R.string.next_in_playlist)
                                    nextVideos = videos.subList(index + 1, videos.size)
                                } else {
                                    binding.nextText.text = getString(R.string.text_last_video)
                                    nextVideos = ArrayList()
                                }
                                (binding.videos.adapter as VideoAdapter).submitList(nextVideos)
                                binding.videos.smoothScrollToPosition(0)
                                binding.divider.visibility = View.VISIBLE
                            }

                            override fun onVideoEnded() {}

                            override fun onError(p0: YouTubePlayer.ErrorReason?) {
                                Timber.tag(TAG).e("onError: $p0")
                            }
                        })
                        p1.setPlaylistEventListener(object : YouTubePlayer.PlaylistEventListener {
                            override fun onPrevious() {
                                index--
                            }

                            override fun onNext() {
                                index++
                            }

                            override fun onPlaylistEnded() {
                                Timber.tag(TAG).d("onPlaylistEnded: ")
                            }

                        })
                    } catch (e: Exception) {
                        Timber.tag(TAG).e(e)
                    }
                }
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Timber.tag(TAG).d("onInitializationFailure: ")
            }

        })
    }

    private fun loadVideos() {
        val app = application as Application
        if (playlistId.isEmpty()) {
            playlistId = app.playlist!!.id
            videos = app.playlist!!.videos!!
            binding.title.text = videos[0].title
            binding.channel.text = videos[0].channel
            binding.nextText.text = getString(R.string.next_in_playlist)
            binding.divider.visibility = View.VISIBLE
            (binding.videos.adapter as VideoAdapter).submitList(videos.drop(1))
            return
        }

        CoroutineScope(Dispatchers.Main + job).launch {
            withContext(Dispatchers.IO) {
                videos = app.repository.getVideos(playlistId, playlistName, playlistThumbnail)
                cacheVideos()
            }
        }
    }

    private fun cacheVideos() {
        val playlistDao = (application as Application).playlistDao
        videos.forEach { playlistDao.insertVideo(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}