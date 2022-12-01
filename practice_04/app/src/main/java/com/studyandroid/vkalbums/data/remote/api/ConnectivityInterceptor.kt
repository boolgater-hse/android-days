package com.studyandroid.vkalbums.data.remote.api

import com.studyandroid.vkalbums.data.remote.WifiService
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!WifiService.instance.isOnline()) {
            throw NoConnectivityException()
        } else {
            return chain.proceed(chain.request())
        }
    }
}
