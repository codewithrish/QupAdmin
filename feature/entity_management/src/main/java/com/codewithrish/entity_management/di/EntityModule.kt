package com.codewithrish.entity_management.di

import app.qup.network.common.QupInterceptorOkHttpClient
import com.codewithrish.entity_management.data.remote.api.EntityApi
import com.codewithrish.entity_management.data.repository.EntityRepositoryImpl
import com.codewithrish.entity_management.domain.repository.EntityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object EntityModule {
    @Singleton
    @Provides
    fun provideReceptionApi(
        @QupInterceptorOkHttpClient okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): EntityApi = retrofit
        .client(okHttpClient)
        .build()
        .create(EntityApi::class.java)
    @Singleton
    @Provides
    fun providesDoctorRepository(entityApi: EntityApi): EntityRepository = EntityRepositoryImpl(entityApi)
}