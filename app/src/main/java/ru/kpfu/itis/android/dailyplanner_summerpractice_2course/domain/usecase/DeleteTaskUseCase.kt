package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.usecase

import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
) {
    suspend operator fun invoke(taskId: Long) {
        return taskRepository.deleteTaskById(taskId)
    }
}