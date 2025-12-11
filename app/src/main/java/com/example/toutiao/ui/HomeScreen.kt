//package com.example.toutiao.ui
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.toutiao.R
//import com.example.toutiao.viewmodel.NewsViewModel
//import com.example.toutiao.ui.components.ChannelTabRow
//import com.example.toutiao.ui.topBar.TopBar
//
//@Composable
//fun HomeScreen(
//    navController: NavController,
//    newsViewModel: NewsViewModel
//) {
//    val newsList by newsViewModel.newsList.collectAsState()
//    val isLoading by newsViewModel.isLoading.collectAsState()
//    val dataSource by newsViewModel.dataSource.collectAsState()
//
//    val channels = listOf("推荐", "热点", "科技", "国际", "军事", "财经", "北京", "娱乐")
//    var selectedIndex by remember { mutableIntStateOf(0) }
//    var selectedBottomNavIndex by remember { mutableIntStateOf(0) }
//
//    // 定义底部导航项
//    val bottomNavItems = listOf(
//        BottomNavItem("首页", R.drawable.home),
//        BottomNavItem("视频", R.drawable.video),
//        BottomNavItem("任务", R.drawable.task),
//        BottomNavItem("我的", R.drawable.profile)
//    )
//
//    Scaffold(
//        topBar = {
//            // 只在首页显示顶部栏
//            if (selectedBottomNavIndex == 0) {
//                TopBar(
//                    onSearchClick = { query ->
//                        if (query.isNotEmpty()) {
//                            navController.navigate("search/${query}")
//                        }
//                    }
//                )
//            }
//        },
//        bottomBar = {
//            NavigationBar(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(80.dp),
//                containerColor = Color.White,
//                contentColor = Color.Black
//            ) {
//                bottomNavItems.forEachIndexed { index, item ->
//                    NavigationBarItem(
//                        icon = {
//                            Icon(
//                                painter = painterResource(id = item.iconResId),
//                                contentDescription = item.label,
//                                modifier = Modifier.size(24.dp),
//                                tint = if (selectedBottomNavIndex == index) {
//                                    Color(0xFFE63946)  // 选中时红色
//                                } else {
//                                    Color(0xFF666666)  // 未选中时灰色
//                                }
//                            )
//                        },
//                        label = {
//                            Text(
//                                text = item.label,
//                                fontSize = 12.sp,
//                                color = if (selectedBottomNavIndex == index) {
//                                    Color(0xFFE63946)
//                                } else {
//                                    Color(0xFF666666)
//                                }
//                            )
//                        },
//                        selected = selectedBottomNavIndex == index,
//                        onClick = {
//                            selectedBottomNavIndex = index
//                        }
//                    )
//                }
//            }
//        },
//        modifier = Modifier.fillMaxSize()
//    ) { innerPadding ->
//        // 根据底部导航栏选择显示不同页面
//        when (selectedBottomNavIndex) {
//            0 -> {  // 首页
//                HomeContent(
//                    navController = navController,
//                    newsList = newsList,
//                    isLoading = isLoading,
//                    dataSource = dataSource,
//                    channels = channels,
//                    selectedIndex = selectedIndex,
//                    onChannelSelected = { selectedIndex = it },
//                    innerPadding = innerPadding
//                )
//            }
//            1 -> {  // 视频页
//                VideoScreen()
//            }
//            2 -> {  // 任务页
//                TaskScreen()
//            }
//            3 -> {  // 我的页
//                ProfileScreen()
//            }
//        }
//    }
//}
//
//@Composable
//fun HomeContent(
//    navController: NavController,
//    newsList: List<com.example.toutiao.model.NewsItem>,
//    isLoading: Boolean,
//    dataSource: com.example.toutiao.viewmodel.DataSource,
//    channels: List<String>,
//    selectedIndex: Int,
//    onChannelSelected: (Int) -> Unit,
//    innerPadding: PaddingValues
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(innerPadding)
//    ) {
//        // 显示数据源状态（调试用）
////        if (isLoading) {
////            LinearProgressIndicator(
////                modifier = Modifier.fillMaxWidth()
////            )
////        }
//
//        // 调试信息：显示当前数据源
//        Text(
//            text = "数据源: ${dataSource.name} (${newsList.size}条)",
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 4.dp),
//            fontSize = 12.sp,
//            color = Color.Gray
//        )
//
//        // 频道标签行
//        ChannelTabRow(
//            channels = channels,
//            selectedIndex = selectedIndex,
//            onChannelSelected = onChannelSelected
//        )
//
//        // 新闻列表
//        NewsFeedScreen(
//            newsList = newsList,
//            onClickNews = { id ->
//                navController.navigate("detail/$id")
//            }
//        )
//    }
//}
//
//data class BottomNavItem(
//    val label: String,
//    val iconResId: Int
//)

package com.example.toutiao.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.example.toutiao.R
import com.example.toutiao.viewmodel.NewsViewModel
import com.example.toutiao.viewmodel.RefreshState
import com.example.toutiao.ui.components.ChannelTabRow
import com.example.toutiao.ui.topBar.TopBar
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator

@Composable
fun HomeScreen(
    navController: NavController,
    newsViewModel: NewsViewModel
) {
    // 获取状态
    val newsListState by newsViewModel.newsListState.collectAsState()
    val refreshState by newsViewModel.refreshState.collectAsState()
    val selectedCategory by newsViewModel.selectedCategory.collectAsState()
    val categories by newsViewModel.categories.collectAsState()

    var selectedIndex by remember { mutableIntStateOf(0) }
    var selectedBottomNavIndex by remember { mutableIntStateOf(0) }

    // 定义底部导航项
    val bottomNavItems = listOf(
        BottomNavItem("首页", R.drawable.home),
        BottomNavItem("视频", R.drawable.video),
        BottomNavItem("任务", R.drawable.task),
        BottomNavItem("我的", R.drawable.profile)
    )

    Scaffold(
        topBar = {
            // 只在首页显示顶部栏
            if (selectedBottomNavIndex == 0) {
                TopBar(
                    onAIClick = {
                        navController.navigate("ai_chat")
                    },
                    onSearchClick = { query ->
                        if (query.isNotEmpty()) {
                            navController.navigate("search/${query}")
                        }
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                containerColor = Color.White,
                contentColor = Color.Black
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = item.iconResId),
                                contentDescription = item.label,
                                modifier = Modifier.size(24.dp),
                                tint = if (selectedBottomNavIndex == index) {
                                    Color(0xFFE63946)
                                } else {
                                    Color(0xFF666666)
                                }
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 12.sp,
                                color = if (selectedBottomNavIndex == index) {
                                    Color(0xFFE63946)
                                } else {
                                    Color(0xFF666666)
                                }
                            )
                        },
                        selected = selectedBottomNavIndex == index,
                        onClick = {
                            selectedBottomNavIndex = index
                        }
                    )
                }
            }
        },
//        floatingActionButton = {
//            // 刷新按钮（可选）
//            if (selectedBottomNavIndex == 0) {
//                ExtendedFloatingActionButton(
//                    onClick = { newsViewModel.refresh() },
//                    icon = {
//                        Icon(Icons.Default.Refresh, "刷新")
//                    },
//                    text = { Text("刷新") },
//                    expanded = refreshState is RefreshState.Idle,
//                    modifier = Modifier.padding(bottom = 80.dp)
//                )
//            }
//        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        // 根据底部导航栏选择显示不同页面
        when (selectedBottomNavIndex) {
            0 -> {  // 首页
                HomeContent(
                    navController = navController,
                    newsListState = newsListState,
                    refreshState = refreshState,
                    selectedCategory = selectedCategory,
                    categories = categories,
                    onCategorySelected = { category, index ->
                        selectedIndex = index
                        newsViewModel.selectCategory(category)
                    },
                    onRefresh = { newsViewModel.refresh() },
                    innerPadding = innerPadding
                )
            }
            1 -> {  // 视频页
                VideoScreen()
            }
            2 -> {  // 任务页
                TaskScreen()
            }
            3 -> {  // 我的页
                ProfileScreen()
            }
        }
    }
}

@Composable
fun HomeContent(
    navController: NavController,
    newsListState: com.example.toutiao.viewmodel.NewsListState,
    refreshState: RefreshState,
    selectedCategory: String,
    categories: List<String>,
    onCategorySelected: (String, Int) -> Unit,
    onRefresh: () -> Unit,
    innerPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        // 频道标签行
        val selectedIndex = categories.indexOf(selectedCategory)
        ChannelTabRow(
            channels = categories,
            selectedIndex = if (selectedIndex >= 0) selectedIndex else 0,
            onChannelSelected = { index ->
                if (index < categories.size) {
                    onCategorySelected(categories[index], index)
                }
            }
        )

        // 根据新闻列表状态显示不同内容
        when (newsListState) {
            is com.example.toutiao.viewmodel.NewsListState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is com.example.toutiao.viewmodel.NewsListState.Success -> {
                // 使用下拉刷新包装新闻列表
                SwipeRefreshWithPaging(
                    newsListState = newsListState,
                    refreshState = refreshState,
                    onRefresh = onRefresh,
                    navController = navController
                )
            }

            is com.example.toutiao.viewmodel.NewsListState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "加载失败",
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(onClick = { onRefresh() }) {
                            Text("重试")
                        }
                    }
                }
            }

            is com.example.toutiao.viewmodel.NewsListState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("暂无新闻内容", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun SwipeRefreshWithPaging(
    newsListState: com.example.toutiao.viewmodel.NewsListState.Success,
    refreshState: RefreshState,
    onRefresh: () -> Unit,
    navController: NavController
) {
    // 获取分页数据
    val pagingDataFlow = newsListState.data
    val pagingItems = pagingDataFlow.collectAsLazyPagingItems()

    // 下拉刷新状态
    val isRefreshing = refreshState is RefreshState.Refreshing
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    // 监听刷新状态变化
    LaunchedEffect(refreshState) {
        if (refreshState is RefreshState.Success || refreshState is RefreshState.Error) {
            // 刷新完成，刷新分页数据
            pagingItems.refresh()
        }
    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = onRefresh,
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                scale = true,
                backgroundColor = Color.White,
                contentColor = Color(0xFFE63946)
            )
        }
    ) {
        // 新闻列表（支持分页）
        PagingNewsFeedScreen(
            pagingItems = pagingItems,
            onClickNews = { id ->
                navController.navigate("detail/$id")
            }
        )
    }
}

data class BottomNavItem(
    val label: String,
    val iconResId: Int
)