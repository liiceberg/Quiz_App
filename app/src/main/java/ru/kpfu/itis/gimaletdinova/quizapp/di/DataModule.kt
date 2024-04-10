package ru.kpfu.itis.gimaletdinova.quizapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kpfu.itis.gimaletdinova.quizapp.data.repository.TriviaRepositoryImpl
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.TriviaRepository
@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindTriviaRepositoryToTriviaRepositoryImpl(repositoryImpl: TriviaRepositoryImpl): TriviaRepository
}