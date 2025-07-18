package ru.kpfu.itis.android.dailyplanner_summerpractice_2course

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.model.Task
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.usecase.GetTasksByDateUseCase
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.presentation.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.ZoneId

class CalendarViewModelTest {
    private val getTasksByDateUseCase = mockk<GetTasksByDateUseCase>()
    private lateinit var viewModel: CalendarViewModel

    @Before
    fun setUp() {
        viewModel = CalendarViewModel(getTasksByDateUseCase)
    }

    @Test
    fun `loadTasksByDate should update tasks` () = runTest {
        // Arrange
        val date = LocalDate.now()
        val start = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1

        val expectedTasks = listOf(
            Task(1, "Task 1", "Desc", start + 10000, start + 20000)
        )

        coEvery { getTasksByDateUseCase(start, end) } returns expectedTasks

        // Act
        viewModel.selectDate(date)

        // Assert
        advanceUntilIdle()
        assertEquals(expectedTasks, viewModel.tasks.value)
    }
}