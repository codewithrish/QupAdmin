package app.qup.network.common

import app.qup.util.common.JWT_ACCESS_TOKEN_PREF
import app.qup.util.common.QupSharedPrefManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val qupSharedPrefManager: QupSharedPrefManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = qupSharedPrefManager.getStringValue(JWT_ACCESS_TOKEN_PREF)
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer $accessToken")
        return chain.proceed(request.build())
    }
}