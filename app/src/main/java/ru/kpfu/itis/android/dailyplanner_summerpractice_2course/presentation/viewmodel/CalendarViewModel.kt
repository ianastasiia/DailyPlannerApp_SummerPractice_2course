package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.model.Task
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.usecase.GetTasksByDateUseCase
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getTasksByDateUseCase: GetTasksByDateUseCase,
): ViewModel() {
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate : StateFlow<LocalDate> = _selectedDate

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks : StateFlow<List<Task>> = _tasks

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        loadTasksByDate(date)
    }

    private fun loadTasksByDate(date: LocalDate) {
        viewModelScope.launch {
            val startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1

            _tasks.value = getTasksByDateUseCase(
                dateStart = startOfDay,
                dateFinish = endOfDay
            )
        }
    }
}