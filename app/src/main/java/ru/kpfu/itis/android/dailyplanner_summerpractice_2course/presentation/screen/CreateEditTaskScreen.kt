@file:OptIn(ExperimentalMaterial3Api::class)

package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.R
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.presentation.viewmodel.CalendarViewModel
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.presentation.viewmodel.CreateEditTaskScreenViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CreateEditTaskScreen(
    navController: NavController,
    taskId: Long,
    viewModel: CreateEditTaskScreenViewModel = hiltViewModel(),
    calendarViewModel: CalendarViewModel = hiltViewModel(),
) {
    val task by viewModel.task.collectAsState()
    val isSaved by viewModel.isSaved.collectAsState()
    val errors by viewModel.errors.collectAsState()

    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    LaunchedEffect(isSaved) {
        if (isSaved) {
            calendarViewModel.refreshTasks()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (taskId > 0) stringResource(R.string.edit_task)
                        else stringResource(R.string.create_task)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (errors.isNotEmpty()) {
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    errors.forEach { errorRes ->
                        Text(
                            text = stringResource(errorRes),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }

            OutlinedTextField(
                value = task.name,
                onValueChange = viewModel::updateName,
                label = { Text(stringResource(R.string.title)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                isError = errors.contains(R.string.error_title_empty)
            )

            OutlinedTextField(
                value = task.description,
                onValueChange = viewModel::updateDescription,
                label = { Text(stringResource(R.string.description)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
                    .padding(bottom = 16.dp),
                maxLines = 5
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        stringResource(R.string.start),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    DatePickerButton(
                        timestamp = task.dateStart,
                        onDateSelected = viewModel::updateStartTime
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        stringResource(R.string.end),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    DatePickerButton(
                        timestamp = task.dateFinish,
                        onDateSelected = viewModel::updateEndTime
                    )
                }
            }


            Button(
                onClick = viewModel::saveTask,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = errors.isEmpty()
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Composable
fun DatePickerButton(
    timestamp: Long,
    onDateSelected: (Long) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val formatter = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }

    val dateString = remember(timestamp) {
        formatter.format(Date(timestamp))
    }

    Button(
        onClick = { showDatePicker = true },
        modifier = Modifier.widthIn(min = 150.dp)
    ) {
        Text(dateString)
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { dateMillis ->
                showDatePicker = false
                onDateSelected(dateMillis)
            },
            onDismiss = { showDatePicker = false },
            initialDate = timestamp
        )
    }
}

@Composable
fun DatePickerDialog(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
    initialDate: Long = System.currentTimeMillis()
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    val formatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_date_and_time)) },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.date_with_value, formatter.format(Date(selectedDate))),style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = stringResource(R.string.time_with_value, timeFormatter.format(Date(selectedDate))),style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        Text(stringResource(R.string.adjust_date))
                        Row {
                            Button(onClick = { selectedDate -= 86_400_000 /* 1 день */ }) {
                                Text("-1 " + stringResource(R.string.day))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { selectedDate += 86_400_000 }) {
                                Text("+1 " + stringResource(R.string.day))
                            }
                        }
                    }
                    
                    Column {
                        Text(stringResource(R.string.adjust_time))
                        Row {
                            Button(onClick = { selectedDate -= 3_600_000 /* 1 час */ }) {
                                Text("-1h")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { selectedDate += 3_600_000 }) {
                                Text("+1h")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Button(onClick = { selectedDate -= 300_000 /* 5 минут */ }) {
                                Text("-5m")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { selectedDate += 300_000 }) {
                                Text("+5m")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onDateSelected(selectedDate) }) {
                Text(stringResource(R.string.select))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}