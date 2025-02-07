package com.example.testapp.ui.navGraph

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.testapp.ui.repositoryScreen.RepositoryScreen
import com.example.testapp.ui.searchScreen.SearchScreen

const val mainScreen = "MainScreen"

@Preview(showBackground = true)
@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost( // контейнер, который управляет навигацией между экранами.
        navController = navController,
        startDestination = mainScreen
    ) {
        composable(mainScreen) {
            // маршрут основного экрана
            SearchScreen(navController)
        }

        composable(
            // маршрут для экрана репозитория
            "repository/{owner}/{repo}", // строка маршрута
            arguments = listOf( // аргументы маршрута
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType }
            )
        ) {// получает аргументы и передаёт их в RepositoryScreen
            backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner").orEmpty()
            val repo = backStackEntry.arguments?.getString("repo").orEmpty()
            RepositoryScreen(owner, repo, navController)
        }
    }
}
