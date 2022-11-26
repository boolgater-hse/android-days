package com.studyandroid.musicapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.studyandroid.musicapp.data.model.Library
import com.studyandroid.musicapp.data.model.Track
import com.studyandroid.musicapp.data.model.User

@Dao
interface LibraryDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(vararg library: Library)

    @Update
    fun update(library: Library)

    @Update
    fun update(libraries: List<Library>): Int

    @Delete
    fun delete(library: Library)

    @Query(
        """
            DELETE FROM library
            WHERE library.track_id = :trackId AND
            library.lib_owner_id = (SELECT users.user_id FROM users WHERE users.email = :email)
        """
    )
    fun deleteTrackFromLibrary(email: String, trackId: Long)

    @Query("SELECT * FROM library")
    fun getAllLibraries(): LiveData<List<Library>>

    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
            SELECT * FROM library l
            INNER JOIN users u ON u.email = :email AND u.user_id = l.lib_owner_id
            INNER JOIN tracks t ON l.track_id = t.track_id
        """
    )
    fun getLibraryOfUser(email: String): LiveData<Map<User, List<Track>>>
}
