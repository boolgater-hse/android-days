package com.studyandroid.vkalbums.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.studyandroid.vkalbums.data.remote.model.Album
import com.studyandroid.vkalbums.data.remote.model.Photo
import com.studyandroid.vkalbums.data.remote.model.User
import com.studyandroid.vkalbums.data.remote.model.responses.VkItems
import com.studyandroid.vkalbums.data.remote.model.responses.VkResponse
import com.studyandroid.vkalbums.data.remote.model.responses.VkResponseList
import com.studyandroid.vkalbums.data.remote.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository()

    val getUserOriginalIdResponse: MutableLiveData<Response<VkResponseList<User>>> =
        MutableLiveData()
    val getUsersAlbumsListResponse: MutableLiveData<Response<VkResponse<VkItems<Album>>>> =
        MutableLiveData()
    val getAlbumPhotosListResponse: MutableLiveData<Response<VkResponse<VkItems<Photo>>>> =
        MutableLiveData()

    fun getUserOriginalId(userId: String) {
        viewModelScope.launch {
            getUserOriginalIdResponse.postValue(repository.getUserOriginalId(userId))
        }
    }

    fun getUsersAlbumsList(ownerId: Long) {
        viewModelScope.launch {
            getUsersAlbumsListResponse.postValue(repository.getUsersAlbumsList(ownerId))
        }
    }

    fun getAlbumPhotosList(ownerId: Long, albumId: String, offset: Int, count: Int?) {
        viewModelScope.launch {
            getAlbumPhotosListResponse.postValue(
                repository.getAlbumPhotosList(
                    ownerId,
                    albumId,
                    offset,
                    count
                )
            )
        }
    }
}
