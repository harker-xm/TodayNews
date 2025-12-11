package com.example.toutiao.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
//import androidx.paging.compose.items
import com.example.toutiao.model.NewsItem
import com.example.toutiao.ui.components.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.itemsIndexed

@Composable
fun PagingNewsFeedScreen(
    pagingItems: LazyPagingItems<NewsItem>,
    onClickNews: (Int) -> Unit
) {
    val lazyListState = rememberLazyListState()

    // 监听滚动到底部，自动加载更多
    val isNearBottom = remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItemsCount = layoutInfo.totalItemsCount

            if (lastVisibleItem != null && totalItemsCount > 0) {
                lastVisibleItem.index >= totalItemsCount - 5  // 距离底部5个item时触发
            } else {
                false
            }
        }
    }

    // 当接近底部时自动加载更多
    LaunchedEffect(isNearBottom.value) {
        if (isNearBottom.value && pagingItems.loadState.append is LoadState.NotLoading) {
            pagingItems.retry()  // 触发加载更多
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        // 新闻列表项 - 使用 itemsIndexed 替代 items 以便获取索引
        itemsIndexed(pagingItems.itemSnapshotList.items) { index, newsItem ->
            if (newsItem != null) {
                NewsListItem(newsItem = newsItem, onClick = onClickNews)
            }
        }

        // 加载状态指示器
        item {
            when (pagingItems.loadState.append) {
                is LoadState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                            color = Color(0xFFE63946)
                        )
                    }
                }

                is LoadState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "加载失败",
                                color = Color.Red,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "点击重试",
                                color = Color.Gray,
                                modifier = Modifier.clickable {
                                    pagingItems.retry()
                                }
                            )
                        }
                    }
                }

                else -> {
                    // 加载完成或空闲状态
                    if (pagingItems.itemCount == 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("暂无新闻内容", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsListItem(
    newsItem: NewsItem,
    onClick: (Int) -> Unit
) {
    when (newsItem) {
        is NewsItem.TextOnly -> {
            NewsCardTextOnly(
                title = newsItem.title,
                source = newsItem.source,
                commentCount = newsItem.commentCount,
                onClick = { onClick(newsItem.id) }
            )
        }

        is NewsItem.RightImage -> {
            NewsCardRightImage(
                title = newsItem.title,
                imageUrl = newsItem.imageUrl,
                source = newsItem.source,
                commentCount = newsItem.commentCount,
                onClick = { onClick(newsItem.id) }
            )
        }

        is NewsItem.ThreeImage -> {
            NewsCardThreeImage(
                title = newsItem.title,
                imageUrls = newsItem.imageUrls,
                source = newsItem.source,
                commentCount = newsItem.commentCount,
                onClick = { onClick(newsItem.id) }
            )
        }

        is NewsItem.Video -> {
            NewsCardVideo(
                title = newsItem.title,
                coverUrl = newsItem.coverUrl,
                duration = newsItem.duration,
                source = newsItem.source,
                commentCount = newsItem.commentCount,
                onClick = { onClick(newsItem.id) }
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}