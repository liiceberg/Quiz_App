package ru.kpfu.itis.gimaletdinova.quizapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kpfu.itis.gimaletdinova.quizapp.data.repository.LevelsRepositoryImpl
import ru.kpfu.itis.gimaletdinova.quizapp.data.repository.TriviaRepositoryImpl
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.LevelsRepository
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.TriviaRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    @Singleton
    fun bindTriviaRepositoryToTriviaRepositoryImpl(repositoryImpl: TriviaRepositoryImpl): TriviaRepository

    @Binds
    @Singleton
    fun bindLevelsRepositoryToLevelsRepositoryImpl(repositoryImpl: LevelsRepositoryImpl): LevelsRepository
}