package com.studyandroid.musicapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.studyandroid.musicapp.data.AppDatabase
import com.studyandroid.musicapp.data.model.Artist
import com.studyandroid.musicapp.data.repository.ArtistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ArtistsRepository

    val getAllArtists: LiveData<List<Artist>>
    val isArtistExists: MutableLiveData<Long?> = MutableLiveData(null)
    val getArtistsNameById: MutableLiveData<String?> = MutableLiveData(null)

    init {
        val artistsDao = AppDatabase.getInstance(application).artistsDao()
        repository = ArtistsRepository(artistsDao)

        getAllArtists = repository.getAllArtists()
    }

    fun addArtist(artist: Artist) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addArtist(artist)
        }
    }

    fun updateArtist(artist: Artist) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateArtist(artist)
        }
    }

    fun deleteArtist(artist: Artist) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteArtist(artist)
        }
    }

    fun getArtistsNameById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            getArtistsNameById.postValue(repository.getArtistsNameById(id))
        }
    }

    fun isArtistExists(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isArtistExists.postValue(repository.isArtistExists(name))
        }
    }
}
