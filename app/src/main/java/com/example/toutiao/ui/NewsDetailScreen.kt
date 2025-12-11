//package com.example.toutiao.ui
//
//import androidx.compose.foundation.*
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Share
//import androidx.compose.material.icons.filled.ThumbUp
////import androidx.compose.material.icons.filled.Comment
////import androidx.compose.material.icons.filled.Bookmark
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import coil.compose.AsyncImage
//import com.example.toutiao.viewmodel.NewsViewModel
//import java.text.SimpleDateFormat
//import java.util.*
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun NewsDetailScreen(
//    navController: NavController,
//    newsId: Int,
//    viewModel: NewsViewModel = viewModel()
//) {
//    // 从 ViewModel 获取新闻数据
//    val newsList by viewModel.newsList.collectAsState()
//    val news = newsList.find { it.id == newsId }
//
//    var liked by remember { mutableStateOf(false) }
//    var bookmarked by remember { mutableStateOf(false) }
//    var likeCount by remember { mutableStateOf(news?.commentCount ?: 0) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("新闻详情", fontSize = 18.sp) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.navigateUp() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
//                    }
//                },
//                actions = {
////                    IconButton(onClick = { bookmarked = !bookmarked }) {
////                        Icon(
////                            Icons.Default.Bookmark,
////                            contentDescription = "收藏",
////                            tint = if (bookmarked) Color(0xFFFFC107) else Color.Gray
////                        )
////                    }
//                    IconButton(onClick = { /* 分享功能 */ }) {
//                        Icon(Icons.Default.Share, contentDescription = "分享")
//                    }
//                }
//            )
//        },
//        bottomBar = {
//            BottomActionBar(
//                liked = liked,
//                likeCount = likeCount,
//                onLikeClick = {
//                    liked = !liked
//                    likeCount = if (liked) likeCount + 1 else likeCount - 1
//                },
//                commentCount = news?.commentCount ?: 0,
//                onCommentClick = { /* 跳转到评论页 */ }
//            )
//        }
//    ) { paddingValues ->
//        if (news == null) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues),
//                contentAlignment = Alignment.Center
//            ) {
//                Text("未找到新闻内容", fontSize = 16.sp, color = Color.Gray)
//            }
//        } else {
//            NewsDetailContent(
//                news = news,
//                modifier = Modifier
//                    .padding(paddingValues)
//                    .verticalScroll(rememberScrollState())
//            )
//        }
//    }
//}
//
//@Composable
//fun NewsDetailContent(news: com.example.toutiao.model.NewsItem, modifier: Modifier = Modifier) {
//    Column(modifier = modifier) {
//        // 标题区域
//        Text(
//            text = news.title,
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            lineHeight = 32.sp,
//            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
//        )
//
//        // 新闻信息栏
//        NewsInfoBar(news = news)
//
//        // 根据新闻类型显示不同内容
//        when (news) {
//            is com.example.toutiao.model.NewsItem.TextOnly -> {
//                // 纯文本新闻，直接显示内容
//                NewsContentText(content = news.content ?: "暂无正文内容")
//            }
//
//            is com.example.toutiao.model.NewsItem.RightImage -> {
//                // 右图新闻：先显示图片，再显示内容
//                RightImageContent(news = news)
//            }
//
//            is com.example.toutiao.model.NewsItem.ThreeImage -> {
//                // 三图新闻：先显示三张图片，再显示内容
//                ThreeImageContent(news = news)
//            }
//
//            is com.example.toutiao.model.NewsItem.Video -> {
//                // 视频新闻：显示视频封面和播放按钮，再显示内容
//                VideoContent(news = news)
//            }
//        }
//
//        // 相关推荐（占位）
////        RelatedNewsSection()
//
//        Spacer(modifier = Modifier.height(80.dp)) // 给底部栏留出空间
//    }
//}
//
//@Composable
//fun NewsInfoBar(news: com.example.toutiao.model.NewsItem) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 20.dp, vertical = 12.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // 来源
//        Text(
//            text = news.source,
//            fontSize = 14.sp,
//            color = Color(0xFF2196F3),
//            fontWeight = FontWeight.Medium
//        )
//
//        Spacer(modifier = Modifier.width(12.dp))
//
//        // 分割点
//        Text("•", color = Color.Gray)
//
//        Spacer(modifier = Modifier.width(12.dp))
//
//        // 发布时间（模拟）
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
//        val publishTime = dateFormat.format(Date())
//        Text(
//            text = publishTime,
//            fontSize = 14.sp,
//            color = Color.Gray
//        )
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        // 评论数
//        Row(verticalAlignment = Alignment.CenterVertically) {
////            Icon(
////                imageVector = Icons.Default.Comment,
////                contentDescription = "评论",
////                modifier = Modifier.size(16.dp),
////                tint = Color.Gray
////            )
//            Spacer(modifier = Modifier.width(4.dp))
//            Text(
//                text = "${news.commentCount}",
//                fontSize = 14.sp,
//                color = Color.Gray
//            )
//        }
//    }
//
//    Divider(modifier = Modifier.padding(horizontal = 20.dp))
//}
//
//@Composable
//fun NewsContentText(content: String) {
//    Text(
//        text = content,
//        fontSize = 16.sp,
//        lineHeight = 26.sp,
//        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
//        textAlign = TextAlign.Justify
//    )
//}
//
//@Composable
//fun RightImageContent(news: com.example.toutiao.model.NewsItem.RightImage) {
//    Column(
//        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
//    ) {
//        // 图片
//        AsyncImage(
//            model = news.imageUrl,
//            contentDescription = "新闻图片",
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(220.dp)
//                .clip(RoundedCornerShape(8.dp)),
//            contentScale = ContentScale.Crop
//        )
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        // 图片说明
//        Text(
//            text = "图片来源：${news.source}",
//            fontSize = 14.sp,
//            color = Color.Gray,
//            modifier = Modifier.padding(horizontal = 4.dp)
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // 正文内容
//        NewsContentText(content = news.content ?: "暂无正文内容")
//    }
//}
//
//@Composable
//fun ThreeImageContent(news: com.example.toutiao.model.NewsItem.ThreeImage) {
//    Column(
//        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
//    ) {
//        // 三张图片
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            news.imageUrls.forEachIndexed { index, url ->
//                AsyncImage(
//                    model = url,
//                    contentDescription = "新闻图片${index + 1}",
//                    modifier = Modifier
//                        .weight(1f)
//                        .height(150.dp)
//                        .clip(RoundedCornerShape(8.dp)),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        // 图片说明
//        Text(
//            text = "组图来源：${news.source}",
//            fontSize = 14.sp,
//            color = Color.Gray,
//            modifier = Modifier.padding(horizontal = 4.dp)
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // 正文内容
//        NewsContentText(content = news.content ?: "暂无正文内容")
//    }
//}
//
//@Composable
//fun VideoContent(news: com.example.toutiao.model.NewsItem.Video) {
//    Column(
//        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(240.dp)
//                .clip(RoundedCornerShape(12.dp))
//                .background(Color.Black)
//        ) {
//            AsyncImage(
//                model = news.coverUrl,
//                contentDescription = "视频封面",
//                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.Crop
//            )
//
//            // 播放按钮
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Card(
//                    modifier = Modifier.size(60.dp),
//                    shape = RoundedCornerShape(30.dp),
//                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
//                ) {
//                    Box(
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "▶",
//                            fontSize = 30.sp,
//                            color = Color.Black
//                        )
//                    }
//                }
//            }
//
//            // 时长
//            Box(
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .padding(12.dp)
//            ) {
//                Card(
//                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f))
//                ) {
//                    Text(
//                        text = news.duration,
//                        color = Color.White,
//                        fontSize = 12.sp,
//                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        // 视频信息
//        Text(
//            text = "视频时长：${news.duration} | 来源：${news.source}",
//            fontSize = 14.sp,
//            color = Color.Gray,
//            modifier = Modifier.padding(horizontal = 4.dp)
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // 正文内容
//        NewsContentText(content = news.content ?: "暂无正文内容")
//    }
//}
//
//@Composable
//fun BottomActionBar(
//    liked: Boolean,
//    likeCount: Int,
//    onLikeClick: () -> Unit,
//    commentCount: Int,
//    onCommentClick: () -> Unit
//) {
//    Surface(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(60.dp),
//        tonalElevation = 8.dp,
//        shadowElevation = 8.dp
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 20.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // 点赞按钮
//            Row(
//                modifier = Modifier
//                    .weight(1f)
//                    .clickable { onLikeClick() },
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.Default.ThumbUp,
//                    contentDescription = "点赞",
//                    tint = if (liked) Color(0xFFFF3B30) else Color.Gray,
//                    modifier = Modifier.size(24.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = if (likeCount > 0) likeCount.toString() else "点赞",
//                    color = if (liked) Color(0xFFFF3B30) else Color.Gray
//                )
//            }
//
//            // 评论按钮
//            Row(
//                modifier = Modifier
//                    .weight(1f)
//                    .clickable { onCommentClick() },
//                verticalAlignment = Alignment.CenterVertically
//            ) {
////                Icon(
////                    imageVector = Icons.Default.Comment,
////                    contentDescription = "评论",
////                    tint = Color.Gray,
////                    modifier = Modifier.size(24.dp)
////                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = if (commentCount > 0) "$commentCount 评论" else "评论",
//                    color = Color.Gray
//                )
//            }
//        }
//    }
//}
//
////@Composable
////fun RelatedNewsSection() {
////    Column(
////        modifier = Modifier
////            .fillMaxWidth()
////            .padding(20.dp)
////    ) {
////        Text(
////            text = "相关推荐",
////            fontSize = 18.sp,
////            fontWeight = FontWeight.Bold,
////            modifier = Modifier.padding(bottom = 12.dp)
////        )
////
////        repeat(2) { index ->
////            Card(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(vertical = 6.dp),
////                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
////                colors = CardDefaults.cardColors(containerColor = Color.White)
////            ) {
////                Row(
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .padding(12.dp)
////                ) {
////                    Column(
////                        modifier = Modifier.weight(1f)
////                    ) {
////                        Text(
////                            text = "相关新闻标题 ${index + 1}",
////                            fontSize = 15.sp,
////                            fontWeight = FontWeight.Medium,
////                            maxLines = 2,
////                            lineHeight = 20.sp
////                        )
////                        Spacer(modifier = Modifier.height(4.dp))
////                        Text(
////                            text = "来源 • 2小时前 • 123评论",
////                            fontSize = 12.sp,
////                            color = Color.Gray
////                        )
////                    }
////
////                    AsyncImage(
////                        model = "https://picsum.photos/80/60?random=$index",
////                        contentDescription = "相关新闻图片",
////                        modifier = Modifier
////                            .size(80.dp, 60.dp)
////                            .clip(RoundedCornerShape(6.dp)),
////                        contentScale = ContentScale.Crop
////                    )
////                }
////            }
////        }
////    }
////}


package com.example.toutiao.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.toutiao.model.NewsItem
import com.example.toutiao.ui.components.RichTextContent
import com.example.toutiao.viewmodel.NewsViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    navController: NavController,
    newsId: Int,
    viewModel: NewsViewModel = viewModel()
) {
    // 使用 LaunchedEffect 获取新闻详情
    var news by remember { mutableStateOf<NewsItem?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(newsId) {
        isLoading = true
        errorMessage = null

        try {
            // 从 ViewModel 获取新闻详情
            val result = viewModel.getNewsById(newsId)
            news = result
        } catch (e: Exception) {
            errorMessage = e.message ?: "加载失败"
        } finally {
            isLoading = false
        }
    }

    var liked by remember { mutableStateOf(false) }
    var bookmarked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableStateOf(news?.commentCount ?: 0) }

    // 当新闻数据变化时更新点赞数
    LaunchedEffect(news) {
        likeCount = news?.commentCount ?: 0
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("新闻详情", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
//                    IconButton(onClick = { bookmarked = !bookmarked }) {
//                        Icon(
//                            Icons.Default.Bookmark,
//                            contentDescription = "收藏",
//                            tint = if (bookmarked) Color(0xFFFFC107) else Color.Gray
//                        )
//                    }
                    IconButton(onClick = { /* 分享功能 */ }) {
                        Icon(Icons.Default.Share, contentDescription = "分享")
                    }
                }
            )
        },
        bottomBar = {
            BottomActionBar(
                liked = liked,
                likeCount = likeCount,
                onLikeClick = {
                    liked = !liked
                    likeCount = if (liked) likeCount + 1 else likeCount - 1
                },
                commentCount = news?.commentCount ?: 0,
                onCommentClick = { /* 跳转到评论页 */ }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("加载失败", fontSize = 16.sp, color = Color.Red)
                    Text(errorMessage!!, fontSize = 14.sp, color = Color.Gray)
                }
            }
        } else if (news == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("未找到新闻内容", fontSize = 16.sp, color = Color.Gray)
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // 标题区域
                Text(
                    text = news!!.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                )

                // 新闻信息栏
                NewsInfoBar(news = news!!)

                // 根据新闻类型显示不同的封面
                when (news) {
                    is NewsItem.RightImage -> {
                        RightImageCover(news = news as NewsItem.RightImage)
                    }
                    is NewsItem.ThreeImage -> {
                        ThreeImageCover(news = news as NewsItem.ThreeImage)
                    }
                    is NewsItem.Video -> {
                        VideoCover(news = news as NewsItem.Video)
                    }
                    else -> {
                        // 默认显示
                        DefaultCover(news = news!!)
                    }
                }

                // 分隔线
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.3f)
                )

                // 正文内容 - 使用富文本组件
                if (news!!.content.isNullOrBlank()) {
                    Text(
                        text = "暂无正文内容",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 20.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    RichTextContent(
                        content = news!!.content!!,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }

                // 相关推荐（占位）
                RelatedNewsSection()

                Spacer(modifier = Modifier.height(80.dp)) // 给底部栏留出空间
            }
        }
    }
}

@Composable
fun NewsInfoBar(news: NewsItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 来源
        Text(
            text = news.source,
            fontSize = 14.sp,
            color = Color(0xFF2196F3),
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 分割点
        Text("•", color = Color.Gray)

        Spacer(modifier = Modifier.width(12.dp))

        // 发布时间（模拟）
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
        val publishTime = dateFormat.format(Date())
        Text(
            text = publishTime,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.weight(1f))

        // 评论数
        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(
//                imageVector = Icons.Default.Comment,
//                contentDescription = "评论",
//                modifier = Modifier.size(16.dp),
//                tint = Color.Gray
//            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${news.commentCount}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }

    Divider(modifier = Modifier.padding(horizontal = 20.dp))
}

@Composable
fun DefaultCover(news: NewsItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        AsyncImage(
            model = if (news is NewsItem.RightImage) (news as NewsItem.RightImage).imageUrl
            else if (news is NewsItem.ThreeImage) (news as NewsItem.ThreeImage).imageUrls.firstOrNull()
            else if (news is NewsItem.Video) (news as NewsItem.Video).coverUrl
            else null,
            contentDescription = "新闻封面",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun RightImageCover(news: NewsItem.RightImage) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        AsyncImage(
            model = news.imageUrl,
            contentDescription = "新闻封面",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        // 图片角标
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f))
            ) {
                Text(
                    text = "单图",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ThreeImageCover(news: NewsItem.ThreeImage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            news.imageUrls.forEachIndexed { index, url ->
                AsyncImage(
                    model = url,
                    contentDescription = "新闻图片${index + 1}",
                    modifier = Modifier
                        .weight(1f)
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // 图片说明
        Text(
            text = "组图：${news.title}",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun VideoCover(news: NewsItem.Video) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        AsyncImage(
            model = news.coverUrl,
            contentDescription = "视频封面",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        // 播放按钮
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.size(70.dp),
                shape = RoundedCornerShape(35.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▶",
                        fontSize = 35.sp,
                        color = Color.Black
                    )
                }
            }
        }

        // 时长
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f))
            ) {
                Text(
                    text = news.duration,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        // 视频角标
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.9f))
            ) {
                Text(
                    text = "视频",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun RelatedNewsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Text(
            text = "相关推荐",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 这里可以添加相关推荐的新闻卡片
        // 暂时使用占位文本
        Text(
            text = "相关推荐功能开发中...",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 20.dp)
        )
    }
}

@Composable
fun BottomActionBar(
    liked: Boolean,
    likeCount: Int,
    onLikeClick: () -> Unit,
    commentCount: Int,
    onCommentClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 点赞按钮
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onLikeClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "点赞",
                    tint = if (liked) Color(0xFFFF3B30) else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (likeCount > 0) likeCount.toString() else "点赞",
                    color = if (liked) Color(0xFFFF3B30) else Color.Gray
                )
            }

            // 评论按钮
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onCommentClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Icon(
//                    imageVector = Icons.Default.Comment,
//                    contentDescription = "评论",
//                    tint = Color.Gray,
//                    modifier = Modifier.size(24.dp)
//                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (commentCount > 0) "$commentCount 评论" else "评论",
                    color = Color.Gray
                )
            }
        }
    }
}