package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.data.repository

import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.data.local.TaskDao
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.data.mapper.TaskMapper
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.model.Task
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.repository.TaskRepository
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val taskMapper: TaskMapper,
) : TaskRepository {
    override suspend fun getTasksByDate(dateStart: Long, dateFinish: Long): List<Task> {
        val response = taskDao.getTasksByDate(
            dateStart = dateStart,
            dateFinish = dateFinish,
        )
        return response.map { taskMapper.map(it) }
    }

    override suspend fun getTaskById(taskId: Long): Task? {
        return taskDao.getTaskById(taskId = taskId)?.let { taskMapper.map(it) }
    }

    override suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task = taskMapper.map(task = task))
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task = taskMapper.map(task = task))
    }

    override suspend fun deleteTaskById(taskId: Long) {
        taskDao.deleteTaskById(taskId = taskId)
    }
}