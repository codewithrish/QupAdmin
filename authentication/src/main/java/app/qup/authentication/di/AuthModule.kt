package app.qup.authentication.di

import app.qup.authentication.data.remote.api.AuthApi
import app.qup.authentication.data.repository.AuthRepositoryImpl
import app.qup.authentication.domain.repository.AuthRepository
import app.qup.network.common.UnAuthorisedInterceptorOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AuthModule {
    @Singleton
    @Provides
    fun provideAuthApi(
        @UnAuthorisedInterceptorOkHttpClient okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): AuthApi = retrofit
            .client(okHttpClient)
            .build()
            .create(AuthApi::class.java)
    @Singleton
    @Provides
    fun providesAuthRepository(authApi: AuthApi): AuthRepository = AuthRepositoryImpl(authApi)
}