package com.example.toutiao.data

import com.example.toutiao.model.NewsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * 本地内存数据源，作为数据库的备用
 */
class NewsLocalDataSource {
    private val _mockNews = com.example.toutiao.data.MockNews.newsList
    val mockNews: List<NewsItem> get() = _mockNews

    fun getAllNews(): Flow<List<NewsItem>> {
        return flowOf(_mockNews)
    }

    fun searchNews(query: String): List<NewsItem> {
        return if (query.isBlank()) {
            _mockNews
        } else {
            _mockNews.filter { news ->
                news.title.contains(query, ignoreCase = true) ||
                        news.source.contains(query, ignoreCase = true)
            }
        }
    }

    fun getNewsById(id: Int): NewsItem? {
        return _mockNews.find { it.id == id }
    }
}