package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.data.local.PlannerDatabase
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.data.mapper.TaskMapper
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.data.repository.TaskRepositoryImpl
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.repository.TaskRepository
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.usecase.GetTaskByIdUseCase
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.usecase.GetTasksByDateUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): PlannerDatabase {
        return PlannerDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(db: PlannerDatabase, taskMapper: TaskMapper): TaskRepository {
        return TaskRepositoryImpl(taskDao = db.taskDao(), taskMapper = taskMapper)
    }

    @Provides
    @Singleton
    fun provideTaskMapper(): TaskMapper = TaskMapper()

    @Provides
    @Singleton
    fun provideGetTasksByDate(repository: TaskRepository): GetTasksByDateUseCase {
        return GetTasksByDateUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetTaskByIdUseCase(repository: TaskRepository): GetTaskByIdUseCase {
        return GetTaskByIdUseCase(repository)
    }

}