package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.data.mapper

import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.data.local.TaskEntity
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.model.Task
import javax.inject.Inject

class TaskMapper @Inject constructor() {
    fun map(taskEntity: TaskEntity): Task =
        Task(
            id = taskEntity.id,
            name = taskEntity.name,
            description = taskEntity.description,
            dateStart = taskEntity.dateStart,
            dateFinish = taskEntity.dateFinish,
        )

    fun map(task: Task): TaskEntity =
        TaskEntity(
            id = task.id,
            name = task.name,
            description = task.description,
            dateStart = task.dateStart,
            dateFinish = task.dateFinish,
        )
}