package de.maggiwuerze.nextcloudgallery.util

import android.util.Log
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class BasicAuthInterceptor(var user: String?, var password: String?) :
    Interceptor {
    private val credentials: String
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)
    }

    init {
        credentials = Credentials.basic(user, password)
    }
}