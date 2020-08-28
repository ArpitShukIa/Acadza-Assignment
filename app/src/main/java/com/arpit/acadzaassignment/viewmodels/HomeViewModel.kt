package com.arpit.acadzaassignment.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arpit.acadzaassignment.Application
import com.arpit.acadzaassignment.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : ViewModel() {

    private val TAG = "TAG HomeViewModel"

    private val repository = application.repository

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    private val _status = MutableLiveData("Search something...")
    val status: LiveData<String> = _status

    fun loadPlaylists(query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _status.postValue("Loading playlists...")
                val list = repository.getPlaylists(query)
                _playlists.postValue(list)
                _status.postValue("")
            }
        }
    }

    fun loadSavedPlaylists() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _status.postValue("Loading playlists...")
                val list = repository.getSavedPlaylists()
                _playlists.postValue(list)
                _status.postValue(
                    if (list.isEmpty())
                        "You haven't viewed anything yet" else ""
                )
            }
        }
    }
}