package com.example.toutiao.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.example.toutiao.viewmodel.NewsViewModel
import com.example.toutiao.model.NewsItem
import com.example.toutiao.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    navController: NavController,
    searchQuery: String? = "",
    newsViewModel: NewsViewModel
) {
    val searchResults by newsViewModel.searchResults.collectAsState()
    val currentQuery by newsViewModel.searchQuery.collectAsState()

    // 如果从导航传递了查询参数，则执行搜索
    LaunchedEffect(searchQuery) {
        if (!searchQuery.isNullOrBlank()) {
            newsViewModel.searchNews(searchQuery)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "搜索结果",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    if (currentQuery.isNotEmpty()) {
                        Text(
                            text = "「$currentQuery」",
                            modifier = Modifier.padding(end = 8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 搜索结果统计
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "共找到 ${searchResults.size} 条结果",
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (currentQuery.isNotEmpty()) {
                        TextButton(onClick = { newsViewModel.clearSearch() }) {
                            Text("清空搜索")
                        }
                    }
                }
            }

            // 搜索结果列表
            if (searchResults.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (currentQuery.isEmpty()) "请输入搜索关键词" else "未找到相关结果",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(searchResults) { item ->
                        when (item) {
                            is NewsItem.TextOnly -> {
                                NewsCardTextOnly(
                                    title = item.title,
                                    source = item.source,
                                    commentCount = item.commentCount,
                                    onClick = { navController.navigate("detail/${item.id}") }
                                )
                            }

                            is NewsItem.RightImage -> {
                                NewsCardRightImage(
                                    title = item.title,
                                    imageUrl = item.imageUrl,
                                    source = item.source,
                                    commentCount = item.commentCount,
                                    onClick = { navController.navigate("detail/${item.id}") }
                                )
                            }

                            is NewsItem.ThreeImage -> {
                                NewsCardThreeImage(
                                    title = item.title,
                                    imageUrls = item.imageUrls,
                                    source = item.source,
                                    commentCount = item.commentCount,
                                    onClick = { navController.navigate("detail/${item.id}") }
                                )
                            }

                            is NewsItem.Video -> {
                                NewsCardVideo(
                                    title = item.title,
                                    coverUrl = item.coverUrl,
                                    duration = item.duration,
                                    source = item.source,
                                    commentCount = item.commentCount,
                                    onClick = { navController.navigate("detail/${item.id}") }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}