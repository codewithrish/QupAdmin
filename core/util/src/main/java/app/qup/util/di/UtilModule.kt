package app.qup.util.di

import android.content.Context
import android.content.SharedPreferences
import app.qup.util.common.QUP_SHARED_PREFERENCE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UtilModule {
    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(QUP_SHARED_PREFERENCE, Context.MODE_PRIVATE)
    }
}