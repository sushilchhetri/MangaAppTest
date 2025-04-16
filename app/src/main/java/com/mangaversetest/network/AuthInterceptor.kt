package com.mangaversetest.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {



    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
            .header(
                NetworkObject.WEB_PARAM_HEADER_X_ACCEPT,
                NetworkObject.WEB_PARAM_HEADER_X_ACCEPT_VALUE
            )
            .header(
                NetworkObject.WEB_PARAM_HEADER_X_CONTENT_TYPE,
                NetworkObject.WEB_PARAM_HEADER_X_ACCEPT_VALUE
            )
            .header(
                NetworkObject.WEB_PARAM_HEADER_API_VERSION,
                NetworkObject.WEB_PARAM_HEADER_API_VERSION_CODE
            )
            .header(
                NetworkObject.WEB_RAPID_API_KEY,
                NetworkObject.API_KEY_VALUE
            )

        return chain.proceed(
            requestBuilder
                .build()
        )
    }
}