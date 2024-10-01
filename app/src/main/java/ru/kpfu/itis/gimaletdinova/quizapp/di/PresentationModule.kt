package ru.kpfu.itis.gimaletdinova.quizapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import ru.kpfu.itis.gimaletdinova.quizapp.util.WorkRepeater

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

    @Provides
    fun provideWorkRepeater(dispatcher: CoroutineDispatcher) : WorkRepeater = WorkRepeater(
        dispatcher = dispatcher,
        job = SupervisorJob()
    )

}