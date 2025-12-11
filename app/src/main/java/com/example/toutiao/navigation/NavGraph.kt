package com.example.toutiao.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.toutiao.ui.HomeScreen
import com.example.toutiao.ui.NewsDetailScreen
import com.example.toutiao.ui.SearchResultScreen
import com.example.toutiao.ui.ai.AIChatScreen
import com.example.toutiao.ui.ai.getSampleNews
import com.example.toutiao.viewmodel.NewsViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    newsViewModel: NewsViewModel
) {

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController, newsViewModel)
        }

        composable("ai_chat") {
            AIChatScreen(
                navController = navController,
                newsContext = getSampleNews()
            )
        }

        composable("detail/{newsId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("newsId")?.toInt() ?: 0
            NewsDetailScreen(
                navController = navController,
                newsId = id,
                viewModel = newsViewModel
            )
        }

        // 搜索结果页面路由
        composable("search/{query}") { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchResultScreen(
                navController = navController,
                searchQuery = query,
                newsViewModel = newsViewModel
            )
        }

        // 无参数的搜索页面（显示空状态）
        composable("search") {
            SearchResultScreen(
                navController = navController,
                searchQuery = null,
                newsViewModel = newsViewModel
            )
        }
    }
}
