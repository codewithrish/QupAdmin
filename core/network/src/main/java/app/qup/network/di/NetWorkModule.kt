package app.qup.network.di

import app.qup.network.BuildConfig
import app.qup.network.common.AuthAuthenticator
import app.qup.network.common.AuthInterceptor
import app.qup.network.common.QupInterceptorOkHttpClient
import app.qup.network.common.UnAuthorisedInterceptorOkHttpClient
import app.qup.util.common.BASE_URL
import app.qup.util.common.QupSharedPrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val TIMEOUT_IN_SECONDS: Long = 60

@InstallIn(SingletonComponent::class)
@Module
object NetWorkModule {
    @UnAuthorisedInterceptorOkHttpClient
    @Singleton
    @Provides
    fun provideAuthOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }
    @QupInterceptorOkHttpClient
    @Singleton
    @Provides
    fun provideQupOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }
    // Interceptors
    @Singleton
    @Provides
    fun providesLogger() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
    @Singleton
    @Provides
    fun provideAuthInterceptor(qupSharedPrefManager: QupSharedPrefManager): AuthInterceptor = AuthInterceptor(qupSharedPrefManager)
    @Singleton
    @Provides
    fun provideAuthAuthenticator(qupSharedPrefManager: QupSharedPrefManager): AuthAuthenticator = AuthAuthenticator(qupSharedPrefManager)
    // Retrofit Builder
    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
}