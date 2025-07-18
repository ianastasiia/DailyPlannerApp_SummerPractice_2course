package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Database(entities = [TaskEntity::class], version = 1, exportSchema = true)
abstract class PlannerDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: PlannerDatabase? = null

        fun getInstance(context: Context): PlannerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlannerDatabase::class.java,
                    "planner_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
        private suspend fun prepopulateDatabase(context: Context) {
            val json = context.assets.open("tasks.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<TaskEntity>>() {}.type
            val tasks = Gson().fromJson<List<TaskEntity>>(json, type)

            getInstance(context).taskDao().insertAll(tasks)
        }
    }
}