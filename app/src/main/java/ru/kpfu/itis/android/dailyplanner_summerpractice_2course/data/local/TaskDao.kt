package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Query("SELECT id, name, description, date_start, date_finish FROM tasks WHERE date_start BETWEEN :dateStart AND :dateFinish")
    suspend fun getTasksByDate(dateStart: Long, dateFinish: Long): List<TaskEntity>

    @Query("SELECT id, name, description, date_start, date_finish FROM tasks WHERE id = :taskId ")
    suspend fun getTaskById(taskId: Long): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long)
}