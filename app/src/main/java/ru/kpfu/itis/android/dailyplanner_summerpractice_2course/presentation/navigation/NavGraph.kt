package ru.kpfu.itis.android.dailyplanner_summerpractice_2course.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.presentation.screen.CalendarScreen
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.presentation.screen.CreateEditTaskScreen
import ru.kpfu.itis.android.dailyplanner_summerpractice_2course.presentation.screen.TaskDetailScreen

sealed class Screen(val route: String) {
    data object Calendar : Screen("calendar")
    data object CurrentTask : Screen("current_task/{taskId}") {
        fun createRoute(taskId: Long) = "current_task/$taskId"
    }

    data object CreateEditTask : Screen("task/edit/{taskId}") {
        fun createRoute(taskId: Long = -1L) = "task/edit/$taskId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Calendar.route
    ) {
        composable(Screen.Calendar.route) { CalendarScreen(navController = navController) }
        composable(
            Screen.CurrentTask.route,
            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
        ) { navBackStackEntry ->
            val taskId = navBackStackEntry.arguments?.getLong("taskId") ?: -1L
            TaskDetailScreen(navController = navController, taskId = taskId)
        }
        composable(
            Screen.CreateEditTask.route,
            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
        ) { navBackStackEntry ->
            val taskId = navBackStackEntry.arguments?.getLong("taskId") ?: -1L
            CreateEditTaskScreen(navController = navController, taskId = taskId)
        }
    }
}
