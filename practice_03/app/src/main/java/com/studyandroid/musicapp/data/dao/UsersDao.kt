package com.studyandroid.musicapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.studyandroid.musicapp.data.model.User

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(vararg user: User)

    @Update
    fun update(user: User)

    @Update
    fun update(users: List<User>): Int

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT EXISTS(SELECT * FROM users WHERE email = :email AND password = :password)")
    fun isUserExists(email: String, password: String): Boolean

    @Query("SELECT users.user_id FROM users WHERE users.email = :email")
    fun getUsersId(email: String): Long
}
