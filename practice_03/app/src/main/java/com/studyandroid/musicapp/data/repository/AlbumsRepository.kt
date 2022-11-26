package com.studyandroid.musicapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.studyandroid.musicapp.data.dao.AlbumsDao
import com.studyandroid.musicapp.data.model.Album

class AlbumsRepository(private val albumsDao: AlbumsDao) {

    suspend fun addAlbum(album: Album) {
        albumsDao.insert(album)
    }

    fun updateAlbum(album: Album) {
        albumsDao.update(album)
    }

    fun deleteAlbum(album: Album) {
        albumsDao.delete(album)
    }

    fun deleteAlbumsByArtistsId(id: Long) {
        albumsDao.deleteAllAlbumsByArtistsId(id)
    }

    fun getAllAlbumsWithArtistName(): LiveData<List<Album>> {
        val result = albumsDao.getAllAlbumsByAllArtists()
        val data: LiveData<List<Album>> = Transformations.map(result) { temp ->
            temp.flatMap { (key, values) ->
                values.map {
                    it.artistName = key.name
                    it
                }
            }
        }
        return data
    }

    fun getAlbumNameById(id: Long): String {
        return albumsDao.getAlbumNameById(id)
    }

    fun isAlbumExists(title: String): Long {
        return albumsDao.isAlbumExists(title)
    }
}
