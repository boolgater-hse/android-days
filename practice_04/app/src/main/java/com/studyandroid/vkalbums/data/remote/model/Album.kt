package com.studyandroid.vkalbums.data.remote.model

import com.google.gson.annotations.SerializedName

data class Album(
    val id: Long,
    @SerializedName("owner_id")
    val ownerId: Long,
    val size: Long,
    val title: String,
    @SerializedName("thumb_id")
    val thumbId: Long,
    @SerializedName("sizes")
    val thumbSizes: List<Size>,
    var thumbUrl: String = "",
)
