package com.studyandroid.musicapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.studyandroid.musicapp.data.dao.AlbumsDao
import com.studyandroid.musicapp.data.dao.TracksDao
import com.studyandroid.musicapp.data.model.Track

class TracksRepository(
    private val tracksDao: TracksDao,
    private val albumsDao: AlbumsDao,
) {

    suspend fun addTrack(track: Track) {
        tracksDao.insert(track)
    }

    fun updateTrack(track: Track) {
        tracksDao.update(track)
    }

    fun updateArtistIdWhereAlbumId(newArtistId: Long, albumId: Long) {
        tracksDao.updateArtistIdWhereAlbumId(newArtistId, albumId)
    }

    fun deleteTrack(track: Track) {
        tracksDao.delete(track)
    }

    fun deleteTracksByArtistsId(id: Long) {
        tracksDao.deleteTracksByArtistsId(id)
    }

    fun deleteTracksByAlbumsId(id: Long) {
        tracksDao.deleteTracksByAlbumsId(id)
    }

    fun getAllTracksWithArtistAndAlbumName(): LiveData<List<Track>> {
        val result = tracksDao.getAllTracksByAllArtists()
        val data: LiveData<List<Track>> = Transformations.map(result) { temp ->
            temp.flatMap { (key, values) ->
                values.map {
                    it.albumName = albumsDao.getAlbumNameById(it.albumId)
                    it.artistName = key.name
                    it
                }
            }
        }
        return data
    }
}
