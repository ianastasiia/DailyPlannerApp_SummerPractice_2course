package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.usecase

import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.model.Task
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.repository.TaskRepository
import javax.inject.Inject

class InsertTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
) {
    suspend operator fun invoke(task: Task): Long {
        return taskRepository.insertTask(task)
    }
}