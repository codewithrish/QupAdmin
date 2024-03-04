package app.qup.srk_management.di

import app.qup.network.common.QupInterceptorOkHttpClient
import app.qup.network.common.UnAuthorisedInterceptorOkHttpClient
import app.qup.srk_management.data.remote.api.SrkApi
import app.qup.srk_management.data.repository.SrkRepositoryImpl
import app.qup.srk_management.domain.repository.SrkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SrkModule {
    @Singleton
    @Provides
    fun provideSrkApi(
        @QupInterceptorOkHttpClient okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): SrkApi = retrofit
        .client(okHttpClient)
        .build()
        .create(SrkApi::class.java)
    @Singleton
    @Provides
    fun providesSrkRepository(srkApi: SrkApi): SrkRepository = SrkRepositoryImpl(srkApi)
}