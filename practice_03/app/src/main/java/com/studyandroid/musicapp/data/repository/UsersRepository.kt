package com.studyandroid.musicapp.data.repository

import androidx.lifecycle.LiveData
import com.studyandroid.musicapp.data.dao.UsersDao
import com.studyandroid.musicapp.data.model.User

class UsersRepository(private val usersDao: UsersDao) {

    suspend fun addUser(user: User) {
        usersDao.insert(user)
    }

    fun getAllUsers(): LiveData<List<User>> {
        return usersDao.getAllUsers()
    }

    fun isUserExists(email: String, password: String): Boolean {
        return usersDao.isUserExists(email, password)
    }

    fun getUsersId(email: String): Long {
        return usersDao.getUsersId(email)
    }
}

