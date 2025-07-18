package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.domain.model

data class Task(
    val id: Long,
    val name: String,
    val description: String,
    val dateStart: Long,
    val dateFinish: Long,
)
