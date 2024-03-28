package app.qup.indiapps.di

import app.qup.indiapps.data.remote.api.IndiApi
import app.qup.indiapps.data.repository.IndiRepositoryImpl
import app.qup.indiapps.domain.repository.IndiRepository
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
object IndiModule {
    @Singleton
    @Provides
    fun provideIndiApi(
        @QupInterceptorOkHttpClient okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): IndiApi = retrofit
        .client(okHttpClient)
        .build()
        .create(IndiApi::class.java)
    @Singleton
    @Provides
    fun providesIndiRepository(indiApi: IndiApi): IndiRepository = IndiRepositoryImpl(indiApi)
}