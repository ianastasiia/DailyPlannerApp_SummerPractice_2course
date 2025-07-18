package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.presentation.viewmodel.TaskDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskDetailScreen(
    navController: NavController,
    taskId: Long,
    viewModel: TaskDetailViewModel = hiltViewModel(),
) {
    val task by viewModel.task.collectAsState()

    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    task?.let {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = task!!.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Дата: ${formatDate(task!!.dateStart)}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Время: ${formatTime(task!!.dateStart)} - ${formatTime(task!!.dateFinish)}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = task!!.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Задача не найдена")
        }
    }
}

private fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(timestamp))
}

private fun formatTime(timestamp: Long): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
}