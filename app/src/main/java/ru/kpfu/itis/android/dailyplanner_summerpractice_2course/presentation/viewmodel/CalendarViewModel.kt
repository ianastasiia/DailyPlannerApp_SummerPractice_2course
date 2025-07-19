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
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getTasksByDateUseCase: GetTasksByDateUseCase,
) : ViewModel() {
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _startOfDay = MutableStateFlow(0L)
    val startOfDay: StateFlow<Long> = _startOfDay

    init {
        loadTasksByDate(LocalDate.now())
    }

    fun selectDate(date: LocalDate) {
        if (_selectedDate != date) {
            _selectedDate.value = date
            _tasks.value = emptyList()
            loadTasksByDate(date)
        }
    }

    private fun loadTasksByDate(date: LocalDate) {
        viewModelScope.launch {
            val zoneId = ZoneId.systemDefault()
            val start = date.atStartOfDay(zoneId).toInstant().toEpochMilli()
            val end = date.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli() - 1

            _startOfDay.value = start
            _tasks.value = getTasksByDateUseCase(
                dateStart = start,
                dateFinish = end
            )
        }
    }

    fun refreshTasks() {
        loadTasksByDate(_selectedDate.value)
    }
}