package com.mangaversetest.network
import okhttp3.Interceptor
import java.io.IOException

class ForbiddenInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val response = chain.proceed(request)

        return response

    }
}