package ru.kpfu.itis.gimaletdinova.quizapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import ru.kpfu.itis.gimaletdinova.quizapp.BuildConfig
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.TriviaApi
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideTriviaApi(okHttpClient: OkHttpClient): TriviaApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.TRIVIA_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TriviaApi::class.java)
    }
}