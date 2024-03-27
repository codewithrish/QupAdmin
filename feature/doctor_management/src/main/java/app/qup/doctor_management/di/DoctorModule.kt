package app.qup.doctor_management.di

import app.qup.doctor_management.data.remote.api.DoctorApi
import app.qup.doctor_management.data.repository.DoctorRepositoryImpl
import app.qup.doctor_management.domain.repository.DoctorRepository
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
object DoctorModule {
    @Singleton
    @Provides
    fun provideDoctorApi(
        @QupInterceptorOkHttpClient okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): DoctorApi = retrofit
        .client(okHttpClient)
        .build()
        .create(DoctorApi::class.java)
    @Singleton
    @Provides
    fun providesDoctorRepository(doctorApi: DoctorApi): DoctorRepository = DoctorRepositoryImpl(doctorApi)
}