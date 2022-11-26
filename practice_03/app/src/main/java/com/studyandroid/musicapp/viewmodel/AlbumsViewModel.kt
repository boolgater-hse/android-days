package com.studyandroid.musicapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.studyandroid.musicapp.data.AppDatabase
import com.studyandroid.musicapp.data.model.Album
import com.studyandroid.musicapp.data.repository.AlbumsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlbumsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AlbumsRepository

    val getAllAlbumsWithArtistName: LiveData<List<Album>>
    val isAlbumExists: MutableLiveData<Long?> = MutableLiveData(null)
    val getAlbumNameById: MutableLiveData<String?> = MutableLiveData(null)

    init {
        val albumsDao = AppDatabase.getInstance(application).albumsDao()
        repository = AlbumsRepository(albumsDao)

        getAllAlbumsWithArtistName = repository.getAllAlbumsWithArtistName()
    }

    fun addAlbum(album: Album) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAlbum(album)
        }
    }

    fun updateAlbum(album: Album) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAlbum(album)
        }
    }

    fun deleteAlbum(album: Album) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlbum(album)
        }
    }

    fun deleteAlbumsByArtistId(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlbumsByArtistsId(id)
        }
    }

    fun getAlbumNameById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            getAlbumNameById.postValue(repository.getAlbumNameById(id))
        }
    }

    fun isAlbumExists(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isAlbumExists.postValue(repository.isAlbumExists(title))
        }
    }
}
