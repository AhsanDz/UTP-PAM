package com.example.todoapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todoapp.data.Task
import com.example.todoapp.screens.DetailScreen
import com.example.todoapp.screens.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val tasks = remember { mutableStateListOf<Task>() }

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(
                tasks = tasks,
                onAddTask = { title ->
                    val newId = (tasks.maxOfOrNull { it.id } ?: 0) + 1
                    tasks.add(Task(id = newId, title = title))
                },
                onDeleteTask = { task -> tasks.remove(task) },
                onTaskClick = { task ->
                    navController.navigate("detail/${task.id}")
                }
            )
        }

        composable(
            route = "detail/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0
            val task = tasks.find { it.id == taskId }

            if (task != null) {
                DetailScreen(
                    task = task,
                    onToggleComplete = {
                        val index = tasks.indexOfFirst { it.id == taskId }
                        if (index != -1) {
                            tasks[index] = tasks[index].copy(
                                isCompleted = !tasks[index].isCompleted
                            )
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }
    }
}