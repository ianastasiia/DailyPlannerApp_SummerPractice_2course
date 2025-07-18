package ru.kpfu.itis.android.dailyplanner_summerpractice_2course

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.model.Task
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.repository.TaskRepository
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.usecase.GetTasksByDateUseCase

class GetTasksByDateUseCaseTest {
    private val taskRepository = mockk<TaskRepository>()
    private val getTasksByDateUseCase = GetTasksByDateUseCase(taskRepository)

    @Test
    fun `should return tasks order by date`() = runTest {
        val start = 1000000L
        val end = 2000000L
        val expectedTasks = listOf(
            Task(1, "Task 1", "Description 1", 1500000, 1600000),
            Task(2, "Task 2", "Description 2", 1700000, 1800000)
        )

        coEvery { taskRepository.getTasksByDate(start, end) } returns expectedTasks

        // Act
        val result = getTasksByDateUseCase(start, end)

        // Assert
        assertEquals(expectedTasks, result)
    }

}