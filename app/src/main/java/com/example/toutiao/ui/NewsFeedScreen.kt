//package com.example.toutiao.ui
//
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.runtime.Composable
//import androidx.compose.foundation.layout.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.toutiao.model.NewsItem
//import com.example.toutiao.ui.components.*
//
//@Composable
//fun NewsFeedScreen(
//    newsList: List<NewsItem>,
//    onClickNews: (Int) -> Unit
//) {
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(vertical = 8.dp)
//    ) {
//        items(newsList) { item ->
//            when (item) {
//
//                is NewsItem.TextOnly -> {
//                    NewsCardTextOnly(
//                        title = item.title,
//                        source = item.source,
//                        commentCount = item.commentCount,
//                        onClick = { onClickNews(item.id) }
//                    )
//                }
//
//                is NewsItem.RightImage -> {
//                    NewsCardRightImage(
//                        title = item.title,
//                        imageUrl = item.imageUrl,
//                        source = item.source,
//                        commentCount = item.commentCount,
//                        onClick = { onClickNews(item.id) }
//                    )
//                }
//
//                is NewsItem.ThreeImage -> {
//                    NewsCardThreeImage(
//                        title = item.title,
//                        imageUrls = item.imageUrls,
//                        source = item.source,
//                        commentCount = item.commentCount,
//                        onClick = { onClickNews(item.id) }
//                    )
//                }
//
//                is NewsItem.Video -> {
//                    NewsCardVideo(
//                        title = item.title,
//                        coverUrl = item.coverUrl,
//                        duration = item.duration,
//                        source = item.source,
//                        commentCount = item.commentCount,
//                        onClick = { onClickNews(item.id) }
//                    )
//                }
//            }
//        }
//    }
//}

package com.example.toutiao.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.toutiao.model.NewsItem
import com.example.toutiao.ui.components.*

@Composable
fun NewsFeedScreen(
    newsList: List<NewsItem>,
    onClickNews: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(newsList) { item ->
            NewsListItem(newsItem = item, onClick = onClickNews)
        }
    }
}