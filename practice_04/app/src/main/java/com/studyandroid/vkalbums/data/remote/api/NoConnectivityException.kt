package com.studyandroid.vkalbums.data.remote.api

import java.io.IOException

class NoConnectivityException : IOException() {

    override fun getLocalizedMessage(): String {
        return "No internet connection"
    }
}
