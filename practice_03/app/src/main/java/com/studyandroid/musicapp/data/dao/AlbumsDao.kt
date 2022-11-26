package com.studyandroid.musicapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.studyandroid.musicapp.data.model.Album
import com.studyandroid.musicapp.data.model.Artist

@Dao
interface AlbumsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg album: Album)

    @Update
    fun update(album: Album)

    @Update
    fun update(albums: List<Album>): Int

    @Delete
    fun delete(album: Album)

    @Query("DELETE FROM albums WHERE albums.artist_id = :id")
    fun deleteAllAlbumsByArtistsId(id: Long)

    @Query("SELECT * FROM albums")
    fun getAllAlbums(): LiveData<List<Album>>

    @Query(
        """
            SELECT * FROM artists
            INNER JOIN albums ON artists.artist_id = albums.artist_id
        """
    )
    fun getAllAlbumsByAllArtists(): LiveData<Map<Artist, List<Album>>>

    @Query("SELECT albums.title FROM albums WHERE albums.album_id = :id")
    fun getAlbumNameById(id: Long): String

    @Query("SELECT albums.album_id FROM albums WHERE albums.title = :title")
    fun isAlbumExists(title: String): Long
}
