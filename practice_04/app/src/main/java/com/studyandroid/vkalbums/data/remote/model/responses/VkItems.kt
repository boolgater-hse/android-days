package com.studyandroid.vkalbums.data.remote.model.responses

data class VkItems<T>(
    val count: Long,
    val items: List<T>,
)
