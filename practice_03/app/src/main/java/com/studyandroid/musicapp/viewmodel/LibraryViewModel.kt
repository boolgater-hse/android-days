package com.studyandroid.musicapp.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.studyandroid.musicapp.R
import com.studyandroid.musicapp.data.AppDatabase
import com.studyandroid.musicapp.data.model.Library
import com.studyandroid.musicapp.data.model.Track
import com.studyandroid.musicapp.data.repository.LibraryRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LibraryRepository

    val usersLibrary: LiveData<List<Track>>
    val libraryInsertionStatus = MutableLiveData<Boolean?>(null)
    val currentUser: String

    init {
        val libraryDao = AppDatabase.getInstance(application).libraryDao()
        val artistsDao = AppDatabase.getInstance(application).artistsDao()
        repository = LibraryRepository(libraryDao, artistsDao)

        val sharedPref = application.getSharedPreferences(
            application.getString(R.string.pref_current_user), Context.MODE_PRIVATE
        )
        currentUser =
            sharedPref.getString(application.getString(R.string.pref_current_user), "").toString()
        usersLibrary = repository.getLibraryOfUser(currentUser)
    }

    fun addLibrary(library: Library) {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("SQL_addLibrary_caught", "$exception")
            libraryInsertionStatus.postValue(false)
        }
        viewModelScope.launch(handler) {
            repository.addLibrary(library)
            libraryInsertionStatus.postValue(true)
        }
    }

    fun getLibraryOfUser(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLibraryOfUser(email)
        }
    }

    fun deleteFromTrackLibrary(email: String, trackId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromTrackLibrary(email, trackId)
        }
    }
}
