package ru.kpfu.itis.android.dailyplanner_summerpractice_2course

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.model.Task
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.usecase.GetTasksByDateUseCase
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.presentation.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CalendarViewModel
    private val getTasksByDateUseCase = mockk<GetTasksByDateUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CalendarViewModel(getTasksByDateUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadTasksByDate should update tasks`() = runTest(testDispatcher) {
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
        advanceUntilIdle()

        // Assert
        assertEquals(expectedTasks, viewModel.tasks.value)
    }
}