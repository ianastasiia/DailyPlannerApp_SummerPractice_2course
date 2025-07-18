package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.R
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.model.Task
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.usecase.GetTaskByIdUseCase
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.usecase.InsertTaskUseCase
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.usecase.UpdateTaskUseCase
import javax.inject.Inject

@HiltViewModel
class CreateEditTaskScreenViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
) : ViewModel() {
    private val _task = MutableStateFlow(Task(0, "", "", System.currentTimeMillis(), System.currentTimeMillis() + 3_600_000))
    val task: StateFlow<Task> = _task

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved

    private val _errors = MutableStateFlow<Set<Int>>(emptySet())
    val errors: StateFlow<Set<Int>> = _errors

    fun loadTask(taskId: Long) {
        if (taskId > 0) {
            viewModelScope.launch {
                getTaskByIdUseCase(taskId)?.let {
                    _task.value = it
                    validate()
                }
            }
        }
    }

    fun updateName(name: String) {
        _task.value = _task.value.copy(name = name)
        validate()
    }

    fun updateDescription(description: String) {
        _task.value = _task.value.copy(description = description)
    }

    fun updateStartTime(timestamp: Long) {
        _task.value = _task.value.copy(dateStart = timestamp)
        validate()
    }

    fun updateEndTime(timestamp: Long) {
        _task.value = _task.value.copy(dateFinish = timestamp)
        validate()
    }

    private fun validate() {
        val newErrors = mutableSetOf<Int>()

        if (_task.value.name.isBlank()) {
            newErrors.add(R.string.error_title_empty)
        }

        if (_task.value.dateStart >= _task.value.dateFinish) {
            newErrors.add(R.string.error_time_invalid)
        }

        _errors.value = newErrors
    }

    fun saveTask() {
        validate()

        if (_errors.value.isEmpty()) {
            viewModelScope.launch {
                if (_task.value.id > 0) {
                    updateTaskUseCase(_task.value)
                } else {
                    insertTaskUseCase(_task.value)
                }
                _isSaved.value = true
            }
        }
    }
}