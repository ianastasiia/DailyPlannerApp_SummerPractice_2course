package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.model.Task
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@Composable
fun HourTaskList(tasks: List<Task>, onTaskClick: (Long) -> Unit) {
    LazyColumn {
        items(24) { hour ->
            val startHour = hour
            val endHour = hour + 1
            val hourTasks = tasks.filter { task ->
                val taskStartHour = Instant.ofEpochMilli(task.dateStart).atZone(ZoneId.systemDefault()).hour
                val taskEndHour = Instant.ofEpochMilli(task.dateFinish).atZone(ZoneId.systemDefault()).hour

                (taskStartHour <= endHour && taskEndHour >= startHour)
            }

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "${startHour}:00 - ${endHour}:00",
                    style = MaterialTheme.typography.bodyLarge
                )

                hourTasks.forEach { task->
                    TaskItem(task = task) {
                        onTaskClick(task.id)
                    }
                }

            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text (
                text = task.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Время: ${formatTime(task.dateStart)} - ${formatTime(task.dateFinish)}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

private fun formatTime(timestamp: Long): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
}