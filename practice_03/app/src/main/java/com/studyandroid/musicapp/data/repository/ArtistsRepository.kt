package com.studyandroid.musicapp.data.repository

import androidx.lifecycle.LiveData
import com.studyandroid.musicapp.data.dao.ArtistsDao
import com.studyandroid.musicapp.data.model.Artist

class ArtistsRepository(private val artistsDao: ArtistsDao) {

    suspend fun addArtist(artist: Artist) {
        artistsDao.insert(artist)
    }

    fun updateArtist(artist: Artist) {
        artistsDao.update(artist)
    }

    fun deleteArtist(artist: Artist) {
        artistsDao.delete(artist)
    }

    fun getAllArtists(): LiveData<List<Artist>> {
        return artistsDao.getAllArtists()
    }

    fun getArtistsNameById(id: Long): String {
        return artistsDao.getArtistsNameById(id)
    }

    fun isArtistExists(name: String): Long {
        return artistsDao.isArtistExists(name)
    }
}
