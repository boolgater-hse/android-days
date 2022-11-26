package com.studyandroid.musicapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.studyandroid.musicapp.data.model.Artist

@Dao
interface ArtistsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg artists: Artist)

    @Update
    fun update(artist: Artist)

    @Update
    fun update(artists: List<Artist>): Int

    @Delete
    fun delete(artist: Artist)

    @Query("SELECT * FROM artists")
    fun getAllArtists(): LiveData<List<Artist>>

    @Query("SELECT artists.name FROM artists WHERE artists.artist_id = :id")
    fun getArtistsNameById(id: Long): String

    @Query("SELECT artists.artist_id FROM artists WHERE artists.name = :name")
    fun isArtistExists(name: String): Long
}
