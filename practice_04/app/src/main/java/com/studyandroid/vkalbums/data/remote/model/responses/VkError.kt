package com.studyandroid.vkalbums.data.remote.model.responses

import com.google.gson.annotations.SerializedName

data class VkError(
    @SerializedName("error_code")
    val errorCode: Long,
    @SerializedName("error_msg")
    val errorMsg: String,
)
