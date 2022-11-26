package com.studyandroid.musicapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.studyandroid.musicapp.data.AppDatabase
import com.studyandroid.musicapp.data.model.User
import com.studyandroid.musicapp.data.repository.UsersRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsersRepository

    val getAllUsers: LiveData<List<User>>
    val userInsertionStatus = MutableLiveData<Boolean?>(null)
    val isUserExists = MutableLiveData<Boolean?>(null)
    val getUsersId = MutableLiveData<Long?>(null)

    init {
        val usersDao = AppDatabase.getInstance(application).usersDao()
        repository = UsersRepository(usersDao)

        getAllUsers = repository.getAllUsers()
    }

    fun addUser(user: User) {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("SQL_addUser_caught", "$exception")
            userInsertionStatus.value = false
        }
        viewModelScope.launch(handler) {
            repository.addUser(user)
            userInsertionStatus.postValue(true)
        }
    }

    fun isUserExists(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.isUserExists(user.email, user.password)
            Log.d("usersVM_isUserExists", "$result")
            isUserExists.postValue(result)
        }
    }

    fun getUsersId(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getUsersId.postValue(repository.getUsersId(email))
        }
    }
}
