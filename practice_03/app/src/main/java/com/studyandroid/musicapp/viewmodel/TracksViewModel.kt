package com.studyandroid.musicapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.studyandroid.musicapp.data.AppDatabase
import com.studyandroid.musicapp.data.model.Track
import com.studyandroid.musicapp.data.repository.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TracksViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TracksRepository

    val getAllTracksWithArtistAndAlbumName: LiveData<List<Track>>

    init {
        val tracksDao = AppDatabase.getInstance(application).tracksDao()
        val albumsDao = AppDatabase.getInstance(application).albumsDao()
        repository = TracksRepository(tracksDao, albumsDao)

        getAllTracksWithArtistAndAlbumName = repository.getAllTracksWithArtistAndAlbumName()
    }

    fun addTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTrack(track)
        }
    }

    fun updateTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTrack(track)
        }
    }

    fun updateArtistIdWhereAlbumId(newArtistId: Long, albumId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateArtistIdWhereAlbumId(newArtistId, albumId)
        }
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTrack(track)
        }
    }

    fun deleteTracksByArtistsId(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTracksByArtistsId(id)
        }
    }

    fun deleteTracksByAlbumsId(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTracksByAlbumsId(id)
        }
    }
}
