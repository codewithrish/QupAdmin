package app.qup.summary.di

import app.qup.network.common.QupInterceptorOkHttpClient
import app.qup.summary.data.remote.api.SummaryApi
import app.qup.summary.data.repository.SummaryRepositoryImpl
import app.qup.summary.domain.repository.SummaryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SummaryModule {
    @Singleton
    @Provides
    fun provideSummaryApi(
        @QupInterceptorOkHttpClient okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): SummaryApi = retrofit
        .client(okHttpClient)
        .build()
        .create(SummaryApi::class.java)
    @Singleton
    @Provides
    fun providesSummaryRepository(summaryApi: SummaryApi): SummaryRepository = SummaryRepositoryImpl(summaryApi)
}