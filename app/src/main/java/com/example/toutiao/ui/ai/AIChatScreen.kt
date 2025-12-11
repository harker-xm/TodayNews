// 文件：AIChatScreen.kt
package com.example.toutiao.ui.ai

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.toutiao.model.NewsItem
import com.example.toutiao.model.Role
import com.example.toutiao.model.UIChatMessage
import com.example.toutiao.viewmodel.AIChatViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIChatScreen(
    navController: NavController,
    newsContext: List<NewsItem> = getSampleNews()
) {
    val viewModel: AIChatViewModel = viewModel()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var userInput by remember { mutableStateOf("") }

    // 处理错误消息
    LaunchedEffect(errorMessage) {
        errorMessage?.let { error ->
            println("AI对话错误: $error")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 顶部导航栏
        TopAppBar(
            title = {
                Text(
                    text = "AI新闻助手",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "返回",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFD43C33)
            ),
            actions = {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                }
            }
        )

        // 错误提示
        errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEBEE)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = error,
                        color = Color(0xFFD32F2F),
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { viewModel.clearError() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "关闭",
                            tint = Color(0xFFD32F2F)
                        )
                    }
                }
            }
        }

        // 新闻上下文摘要
        NewsContextCard(newsContext = newsContext)

        // 聊天消息列表
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Top,
            reverseLayout = true
        ) {
            items(chatMessages.reversed()) { message ->
                ChatBubble(message = message)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // 输入区域
        InputArea(
            userInput = userInput,
            isLoading = isLoading,
            onInputChange = { userInput = it },
            onSend = {
                if (userInput.isNotBlank() && !isLoading) {
                    viewModel.sendMessage(userInput, newsContext)
                    userInput = ""
                }
            }
        )
    }
}

@Composable
fun ChatBubble(message: UIChatMessage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.role == Role.USER) Alignment.End else Alignment.Start
    ) {
        // 思考内容（灰色，在回答上方）
        if (message.thinkingContent.isNotEmpty() && message.role == Role.ASSISTANT) {
            Card(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "🤔 思考中...",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = message.thinkingContent,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }

        // 消息内容
        Card(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .padding(horizontal = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.role == Role.USER) Color(0xFFD43C33) else Color(0xFFF0F0F0)
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (message.role == Role.USER) 2.dp else 1.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // 消息头
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // 用户图标
                    if (message.role == Role.USER) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "用户",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        // AI图标
//                        Icon(
//                            imageVector = Icons.Default.AutoAwesome,
//                            contentDescription = "AI助手",
//                            tint = Color(0xFFD43C33),
//                            modifier = Modifier.size(16.dp)
//                        )
                    }
                    Text(
                        text = if (message.role == Role.USER) "您" else "AI助手",
                        color = if (message.role == Role.USER) Color.White else Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 消息内容
                Text(
                    text = message.content,
                    color = if (message.role == Role.USER) Color.White else Color.Black,
                    fontSize = 14.sp
                )

                // 加载指示器（当AI消息未完成且内容为空时显示）
                if (message.role == Role.ASSISTANT && !message.isComplete && message.content.isEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(3) { index ->
                            DotLoader(index = index)
                        }
                    }
                }
            }
        }

        // 时间戳
        Text(
            text = formatTimestamp(message.timestamp),
            color = Color.Gray,
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun DotLoader(index: Int) {
    val delay = index * 200L

    // 使用动画让圆点闪烁 - 修复：使用 durationMillis 参数
    val alpha by animateFloatAsState(
        targetValue = if (delay % 600 < 200) 1f else 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600),  // 修复：改为 durationMillis
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotLoader"
    )

    Box(
        modifier = Modifier
            .size(6.dp)
            .background(
                color = Color(0xFFD43C33).copy(alpha = alpha),
                shape = CircleShape
            )
    )
}

@Composable
fun InputArea(
    userInput: String,
    isLoading: Boolean,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = userInput,
            onValueChange = onInputChange,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            placeholder = { Text("输入您的问题...") },
            shape = RoundedCornerShape(28.dp),
            readOnly = isLoading,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFD43C33),
                unfocusedBorderColor = Color.Gray
            ),
            trailingIcon = {
                if (userInput.isNotEmpty()) {
                    IconButton(
                        onClick = { onInputChange("") },
                        enabled = !isLoading  // 这里使用 enabled
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "清空",
                            tint = Color.Gray
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        FloatingActionButton(
            onClick = {
                if (userInput.isNotBlank() && !isLoading) {
                    onSend()
                }
            },
            containerColor = Color(0xFFD43C33),
            modifier = Modifier.size(56.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "发送",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun NewsContextCard(newsContext: List<NewsItem>) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
//                    Icon(
//                        imageVector = Icons.Default.Article,
//                        contentDescription = "新闻",
//                        tint = Color(0xFFD43C33),
//                        modifier = Modifier.size(18.dp)
//                    )
                    Text(
                        text = "新闻上下文（${newsContext.size}条）",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                TextButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        text = if (isExpanded) "收起" else "展开",
                        fontSize = 12.sp
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                newsContext.forEachIndexed { index, news ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "${index + 1}. ${news.title}",
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = (news.content?.take(100) ?: "暂无详细内容") + "...",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "来源：${news.source}",
                                fontSize = 10.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }
            } else {
                // 折叠状态下显示简要信息
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "包含${newsContext.size}条新闻，包括：${newsContext.take(3).joinToString("、") { it.title }}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60000 -> "刚刚"
        diff < 3600000 -> "${diff / 60000}分钟前"
        diff < 86400000 -> "${diff / 3600000}小时前"
        else -> SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(timestamp)
    }
}

// 获取示例新闻数据
fun getSampleNews(): List<NewsItem> {
    return listOf(
        NewsItem.TextOnly(
            id = 1,
            title = "习近平总书记关于中华合作重要论述",
            source = "新华社",
            commentCount = 123,
            content = "习近平总书记的重要论述为中华合作指明了方向，强调要加强国际合作，推动构建人类命运共同体。习近平总书记强调，中国将始终坚持和平发展道路，积极参与全球治理，为世界和平与发展作出新的更大贡献。"
        ),
        NewsItem.RightImage(
            id = 2,
            title = "我国已有近320公里高铁常态化按350公里高标运营",
            source = "人民日报",
            commentCount = 456,
            imageUrl = "https://example.com/highspeed_rail.jpg",
            content = "我国高铁技术取得新突破，运营里程和速度再创新高，标志着中国高铁技术达到世界领先水平。截至目前，全国高铁运营里程已超过4.2万公里，其中近320公里高铁线路实现常态化350公里时速运营。"
        ),
        NewsItem.ThreeImage(
            id = 3,
            title = "腾讯被骗后悬赏1000瓶老干妈",
            source = "腾讯新闻",
            commentCount = 789,
            imageUrls = listOf("https://example.com/tencent1.jpg", "https://example.com/tencent2.jpg", "https://example.com/tencent3.jpg"),
            content = "腾讯与老干妈合作中出现误会，腾讯发布悬赏公告，双方最终达成和解，成为商业合作中的一段趣闻。事件源于腾讯起诉老干妈拖欠广告费，后经查明系有人伪造老干妈印章与腾讯签订合同。"
        ),
        NewsItem.Video(
            id = 4,
            title = "这档社交观察类综艺火了，桃花坞是如何做到的",
            source = "娱乐新闻",
            commentCount = 234,
            coverUrl = "https://example.com/video_cover.jpg",
            duration = "15:30",
            content = "近期一档名为《桃花坞》的社交观察类综艺节目引发广泛关注。节目通过真实的社交场景，观察不同性格人群的互动，探讨现代社交关系。节目创新性地采用了无剧本录制模式，让嘉宾在相对自由的环境中进行真实互动。"
        )
    )
}

// 预览函数
@Preview(showBackground = true)
@Composable
fun AIChatScreenPreview() {
    AIChatScreen(
        navController = androidx.navigation.compose.rememberNavController(),
        newsContext = getSampleNews()
    )
}

@Preview
@Composable
fun ChatBubblePreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ChatBubble(
            message = UIChatMessage(
                id = "1",
                role = Role.USER,
                content = "请问关于高铁的新闻有哪些？",
                timestamp = System.currentTimeMillis() - 1000
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        ChatBubble(
            message = UIChatMessage(
                id = "2",
                role = Role.ASSISTANT,
                content = "根据新闻上下文，我国高铁技术取得新突破，已有近320公里高铁实现常态化350公里时速运营。",
                thinkingContent = "用户询问高铁新闻，我需要从新闻上下文中查找相关信息。",
                timestamp = System.currentTimeMillis() - 500
            )
        )
    }
}