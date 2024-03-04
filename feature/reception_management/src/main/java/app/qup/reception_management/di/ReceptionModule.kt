package app.qup.reception_management.di

import app.qup.network.common.QupInterceptorOkHttpClient
import app.qup.reception_management.data.remote.api.ReceptionApi
import app.qup.reception_management.data.repository.ReceptionRepositoryImpl
import app.qup.reception_management.domain.repository.ReceptionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object ReceptionModule {
    @Singleton
    @Provides
    fun provideReceptionApi(
        @QupInterceptorOkHttpClient okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): ReceptionApi = retrofit
        .client(okHttpClient)
        .build()
        .create(ReceptionApi::class.java)
    @Singleton
    @Provides
    fun providesReceptionRepository(receptionApi: ReceptionApi): ReceptionRepository = ReceptionRepositoryImpl(receptionApi)
}