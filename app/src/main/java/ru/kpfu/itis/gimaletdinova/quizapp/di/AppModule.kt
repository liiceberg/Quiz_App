package ru.kpfu.itis.gimaletdinova.quizapp.di

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.kpfu.itis.gimaletdinova.quizapp.util.Keys.DATA_STORE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.createDataStore(DATA_STORE_NAME)


    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}