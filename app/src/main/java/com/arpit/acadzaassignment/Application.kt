package com.arpit.acadzaassignment

import android.app.Application
import com.arpit.acadzaassignment.data.DefaultRepository
import com.arpit.acadzaassignment.data.MyRoomDatabase
import com.arpit.acadzaassignment.data.PlaylistDao
import com.arpit.acadzaassignment.data.Repository
import com.arpit.acadzaassignment.models.Playlist
import timber.log.Timber

class Application : Application() {

    val repository: Repository by lazy { DefaultRepository(this) }
    val playlistDao: PlaylistDao by lazy { MyRoomDatabase.getDatabase(this).playlistDao() }

    var playlist: Playlist? = null

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}