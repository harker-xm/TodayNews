package com.example.toutiao.utils

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * 新闻内容元素
 */
sealed class NewsContentElement {
    data class Text(val content: String) : NewsContentElement()
    data class Image(val url: String, val description: String) : NewsContentElement()
}

/**
 * Markdown 解析器
 */
object MarkdownParser {

    /**
     * 解析包含 Markdown 图片语法的文本
     * 格式: ![图片描述](图片URL)
     */
    fun parseContent(content: String): List<NewsContentElement> {
        val elements = mutableListOf<NewsContentElement>()

        var currentText = StringBuilder()
        var i = 0

        while (i < content.length) {
            // 检查是否以 "![" 开头
            if (i + 1 < content.length && content[i] == '!' && content[i + 1] == '[') {
                // 如果有累积的文本，先添加
                if (currentText.isNotEmpty()) {
                    elements.add(NewsContentElement.Text(currentText.toString()))
                    currentText.clear()
                }

                // 查找图片描述的结束位置 "]"
                val descStart = i + 2
                val descEnd = content.indexOf(']', descStart)
                if (descEnd == -1) {
                    // 格式错误，继续处理
                    currentText.append(content.substring(i))
                    break
                }

                // 获取图片描述
                val description = content.substring(descStart, descEnd)

                // 查找图片URL的开始位置 "("
                val urlStart = content.indexOf('(', descEnd)
                if (urlStart == -1) {
                    // 格式错误，继续处理
                    currentText.append(content.substring(i))
                    break
                }

                // 查找图片URL的结束位置 ")"
                val urlEnd = content.indexOf(')', urlStart + 1)
                if (urlEnd == -1) {
                    // 格式错误，继续处理
                    currentText.append(content.substring(i))
                    break
                }

                // 获取图片URL
                val imageUrl = content.substring(urlStart + 1, urlEnd)

                // 添加图片元素
                elements.add(NewsContentElement.Image(imageUrl, description))

                // 移动索引
                i = urlEnd + 1
            } else {
                // 普通文本，添加到当前文本
                currentText.append(content[i])
                i++
            }
        }

        // 处理剩余的文本
        if (currentText.isNotEmpty()) {
            elements.add(NewsContentElement.Text(currentText.toString()))
        }

        return elements
    }
}