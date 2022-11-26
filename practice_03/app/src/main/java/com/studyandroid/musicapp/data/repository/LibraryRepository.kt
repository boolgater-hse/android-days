package com.studyandroid.musicapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.studyandroid.musicapp.data.dao.ArtistsDao
import com.studyandroid.musicapp.data.dao.LibraryDao
import com.studyandroid.musicapp.data.model.Library
import com.studyandroid.musicapp.data.model.Track

class LibraryRepository(
    private val libraryDao: LibraryDao,
    private val artistsDao: ArtistsDao,
) {

    suspend fun addLibrary(library: Library) {
        libraryDao.insert(library)
    }

    fun deleteFromTrackLibrary(email: String, trackId: Long) {
        libraryDao.deleteTrackFromLibrary(email, trackId)
    }

    fun getLibraryOfUser(email: String): LiveData<List<Track>> {
        val result = libraryDao.getLibraryOfUser(email)
        val data = Transformations.map(result) {
            it.flatMap { (_, values) ->
                values.map { track ->
                    track.artistName = artistsDao.getArtistsNameById(track.artistId)
                    track
                }
            }
        }
        return data
    }
}
