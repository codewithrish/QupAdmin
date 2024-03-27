package app.qup.commcredits.di

import app.qup.commcredits.data.remote.api.CommApi
import app.qup.commcredits.data.repository.CommRepositoryImpl
import app.qup.commcredits.domain.repository.CommRepository
import app.qup.network.common.QupInterceptorOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CommModule {
    @Singleton
    @Provides
    fun provideCommApi(
        @QupInterceptorOkHttpClient okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): CommApi = retrofit
        .client(okHttpClient)
        .build()
        .create(CommApi::class.java)
    @Singleton
    @Provides
    fun providesCommRepository(commApi: CommApi): CommRepository = CommRepositoryImpl(commApi)
}