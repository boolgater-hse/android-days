package com.studyandroid.musicapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.studyandroid.musicapp.data.model.Artist
import com.studyandroid.musicapp.data.model.Track

@Dao
interface TracksDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg tracks: Track)

    @Update
    fun update(track: Track)

    @Update
    fun update(tracks: List<Track>): Int

    @Query("UPDATE tracks SET artist_id = :newArtistId WHERE album_id = :albumId")
    fun updateArtistIdWhereAlbumId(newArtistId: Long, albumId: Long)

    @Delete
    fun delete(track: Track)

    @Query("DELETE FROM tracks WHERE tracks.artist_id = :id")
    fun deleteTracksByArtistsId(id: Long)

    @Query("DELETE FROM tracks WHERE tracks.album_id = :id")
    fun deleteTracksByAlbumsId(id: Long)

    @Query("SELECT * FROM tracks")
    fun getAllTracks(): List<Track>

    @Query(
        """
            SELECT * FROM artists a
            INNER JOIN tracks t
            ON t.artist_id = a.artist_id
        """
    )
    fun getAllTracksByAllArtists(): LiveData<Map<Artist, List<Track>>>
}
