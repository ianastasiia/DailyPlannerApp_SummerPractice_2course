package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.usecase

import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.model.Task
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
) {
    suspend operator fun invoke(taskId: Long): Task? {
        return taskRepository.getTaskById(taskId = taskId)
    }
}