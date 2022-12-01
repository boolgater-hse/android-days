package com.studyandroid.vkalbums.data.remote.model.responses

import com.google.gson.annotations.SerializedName

data class VkResponse<T>(
    @SerializedName("response")
    val data: T? = null,
    @SerializedName("error")
    val error: VkError? = null,
)
