package ru.kpfu.itis.gimaletdinova.quizapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kpfu.itis.gimaletdinova.quizapp.BuildConfig
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.AuthAuthenticator
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.interceptor.AccessTokenInterceptor
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.interceptor.RefreshTokenInterceptor
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service.TriviaService
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service.AuthService
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.service.RefreshTokenService
import ru.kpfu.itis.gimaletdinova.quizapp.di.qualifier.AuthenticatedClient
import ru.kpfu.itis.gimaletdinova.quizapp.di.qualifier.PublicClient
import ru.kpfu.itis.gimaletdinova.quizapp.di.qualifier.TokenRefreshClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    @AuthenticatedClient
    fun provideAccessOkHttpClient(
        accessTokenInterceptor: AccessTokenInterceptor,
        authAuthenticator: AuthAuthenticator
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .authenticator(authAuthenticator)
            .addInterceptor(accessTokenInterceptor)

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return clientBuilder
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    @Singleton
    @TokenRefreshClient
    fun provideRefreshOkHttpClient(
        refreshTokenInterceptor: RefreshTokenInterceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(refreshTokenInterceptor)

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return clientBuilder
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @PublicClient
    fun provideUnauthenticatedOkHttpClient(): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return clientBuilder
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@TokenRefreshClient okHttpClient: OkHttpClient): RefreshTokenService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(RefreshTokenService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthenticationApi(@PublicClient okHttpClient: OkHttpClient): AuthService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideTriviaApi(@AuthenticatedClient okHttpClient: OkHttpClient): TriviaService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(TriviaService::class.java)

    }
}