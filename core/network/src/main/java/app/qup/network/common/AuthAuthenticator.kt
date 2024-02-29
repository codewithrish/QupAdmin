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

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val token = runBlocking {
            tokenManager.getRefreshToken().first()
        }
        return runBlocking {
            val newToken = getNewToken(token?:"")
            if (!newToken.isSuccessful || newToken.body() == null) {
                tokenManager.deleteAccessToken()
                tokenManager.deleteRefreshToken()
            }
            newToken.body()?.toToken()?.let {
                tokenManager.saveAccessToken(it.accessToken)
                tokenManager.saveRefreshToken(it.refreshToken)
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