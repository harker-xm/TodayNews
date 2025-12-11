package com.example.toutiao.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.toutiao.utils.MarkdownParser
import com.example.toutiao.utils.NewsContentElement

/**
 * 富文本内容展示组件
 */
@Composable
fun RichTextContent(
    content: String,
    modifier: Modifier = Modifier
) {
    val elements = MarkdownParser.parseContent(content)

    Column(modifier = modifier) {
        elements.forEachIndexed { index, element ->
            when (element) {
                is NewsContentElement.Text -> {
                    RichTextBlock(
                        text = element.content,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                is NewsContentElement.Image -> {
                    NewsImage(
                        imageUrl = element.url,
                        description = element.description,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    )
                }
            }
        }
    }
}

/**
 * 文本块组件，支持段落格式化
 */
@Composable
fun RichTextBlock(
    text: String,
    modifier: Modifier = Modifier
) {
    // 分割段落（按两个换行符）
    val paragraphs = text.split("\n\n")

    Column(modifier = modifier) {
        paragraphs.forEachIndexed { index, paragraph ->
            if (paragraph.isNotBlank()) {
                // 进一步按单换行分割（处理换行）
                val lines = paragraph.split("\n")

                lines.forEachIndexed { lineIndex, line ->
                    if (line.isNotBlank()) {
                        Text(
                            text = line.trim(),
                            fontSize = 16.sp,
                            lineHeight = 26.sp,
                            modifier = Modifier.padding(bottom = 4.dp),
                            textAlign = TextAlign.Justify
                        )
                    }
                }

                // 段落之间添加更多间距
                if (index < paragraphs.size - 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

/**
 * 新闻图片组件
 */
@Composable
fun NewsImage(
    imageUrl: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        // 图片
        AsyncImage(
            model = imageUrl,
            contentDescription = description,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        // 图片描述
        if (description.isNotBlank() && description != "图片描述") {
            Text(
                text = description,
                fontSize = 14.sp,
                color = androidx.compose.ui.graphics.Color.Gray,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}