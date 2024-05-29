package ru.kpfu.itis.gimaletdinova.quizapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.JwtManager
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.JwtManagerImpl
import ru.kpfu.itis.gimaletdinova.quizapp.data.repository.LevelsRepositoryImpl
import ru.kpfu.itis.gimaletdinova.quizapp.data.repository.RoomRepositoryImpl
import ru.kpfu.itis.gimaletdinova.quizapp.data.repository.ScoresRepositoryImpl
import ru.kpfu.itis.gimaletdinova.quizapp.data.repository.TriviaRepositoryImpl
import ru.kpfu.itis.gimaletdinova.quizapp.data.repository.UserRepositoryImpl
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.LevelsRepository
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.RoomRepository
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.ScoresRepository
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.TriviaRepository
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.UserRepository
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

    @Binds
    @Singleton
    fun bindJwtTokenManagerToJwtTokenManagerImpl(jwtTokenManagerImpl: JwtManagerImpl) : JwtManager

    @Binds
    @Singleton
    fun bindUserRepositoryToUserRepositoryImpl(userRepositoryImpl: UserRepositoryImpl) : UserRepository

    @Binds
    @Singleton
    fun bindRoomRepositoryToRoomRepositoryImpl(roomRepository: RoomRepositoryImpl) : RoomRepository

    @Binds
    @Singleton
    fun bindScoresRepositoryToScoresRepositoryImpl(scoresRepository: ScoresRepositoryImpl) : ScoresRepository
}