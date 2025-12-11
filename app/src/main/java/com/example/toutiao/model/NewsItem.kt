package com.example.toutiao.model

/**
 * 统一的 NewsItem 数据模型（增加 content 字段以支持详情页）
 *
 * 所有子类都继承这四个基础字段 + 可选正文 content。
 */
sealed class NewsItem(
    val id: Int,
    val title: String,
    val source: String,
    val commentCount: Int,
    val content: String? = null
) {
    class TextOnly(
        id: Int,
        title: String,
        source: String,
        commentCount: Int,
        content: String? = null
    ) : NewsItem(id, title, source, commentCount, content)

    class RightImage(
        id: Int,
        title: String,
        source: String,
        commentCount: Int,
        val imageUrl: String,
        content: String? = null
    ) : NewsItem(id, title, source, commentCount, content)

    class ThreeImage(
        id: Int,
        title: String,
        source: String,
        commentCount: Int,
        val imageUrls: List<String>,
        content: String? = null
    ) : NewsItem(id, title, source, commentCount, content)

    class Video(
        id: Int,
        title: String,
        source: String,
        commentCount: Int,
        val coverUrl: String,
        val duration: String,
        content: String? = null
    ) : NewsItem(id, title, source, commentCount, content)
}
