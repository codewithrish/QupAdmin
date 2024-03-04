package app.qup.network.common

import android.util.Base64
import app.qup.network.data.remote.api.NetWorkApi
import app.qup.network.data.remote.dto.TokenResponseDto
import app.qup.network.data.remote.dto.toToken
import app.qup.util.common.BASE_URL
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.HashMap
import javax.inject.Inject
import app.qup.util.common.CLIENT_USERNAME
import app.qup.util.common.CLIENT_PASSWORD
import app.qup.util.common.JWT_ACCESS_TOKEN_PREF
import app.qup.util.common.JWT_REFRESH_TOKEN_PREF
import app.qup.util.common.QupSharedPrefManager

class AuthAuthenticator @Inject constructor(
    private val qupSharedPrefManager: QupSharedPrefManager
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = qupSharedPrefManager.getStringValue(JWT_REFRESH_TOKEN_PREF)

        return runBlocking {
            val newToken = getNewToken(refreshToken = refreshToken?:"")
            if (!newToken.isSuccessful || newToken.body() == null) {
                qupSharedPrefManager.deleteKey(JWT_ACCESS_TOKEN_PREF)
                qupSharedPrefManager.deleteKey(JWT_REFRESH_TOKEN_PREF)
            }
            newToken.body()?.toToken()?.let {
                qupSharedPrefManager.save(JWT_ACCESS_TOKEN_PREF, it.accessToken)
                qupSharedPrefManager.save(JWT_REFRESH_TOKEN_PREF, it.refreshToken)
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${it.accessToken}")
                    .build()
            }
        }
    }
    private suspend fun getNewToken(refreshToken: String): retrofit2.Response<TokenResponseDto> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val hashMap: HashMap<String, String> = HashMap()
        hashMap["grant_type"] = "refresh_token"
        hashMap["refresh_token"] = refreshToken
        val credentials = "$CLIENT_USERNAME:$CLIENT_PASSWORD"
        val encodedString = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(NetWorkApi::class.java)

        return service.refreshToken(encodedString, hashMap)
    }
}