package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey @ColumnInfo(name="id") val id: Long,
    @ColumnInfo(name="name") val name: String,
    @ColumnInfo(name="description") val description: String,
    @ColumnInfo(name="date_start") val dateStart: Long,
    @ColumnInfo(name="date_finish") val dateFinish: Long,
)
