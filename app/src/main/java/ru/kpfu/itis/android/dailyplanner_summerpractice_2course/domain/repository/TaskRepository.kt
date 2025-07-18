package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.repository

import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.model.Task

interface TaskRepository {
    suspend fun getTasksByDate(dateStart: Long, dateFinish: Long): List<Task>

    suspend fun getTaskById(taskId: Long): Task?

    suspend fun insertTask(task: Task) : Long

    suspend fun updateTask(task: Task)

    suspend fun deleteTaskById(taskId: Long)
}