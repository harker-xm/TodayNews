// 文件：model/AINewsAdapter.kt
package com.example.toutiao.model

/**
 * 用于AI对话的新闻数据适配器
 * 将原始的NewsItem转换为AI对话需要的格式
 */
object AINewsAdapter {

    /**
     * 将NewsItem转换为AI对话需要的简单格式
     */
    data class AINewsItem(
        val id: Int,
        val title: String,
        val content: String?,
        val source: String,
        val time: String = "刚刚"  // 默认为"刚刚"，因为NewsItem没有时间字段
    )

    /**
     * 从NewsItem列表转换为AINewsItem列表
     */
    fun convertToAINewsItems(newsItems: List<NewsItem>): List<AINewsItem> {
        return newsItems.map { news ->
            AINewsItem(
                id = news.id,
                title = news.title,
                content = news.content,
                source = news.source,
                time = generateTimePlaceholder(news)
            )
        }
    }

    /**
     * 生成时间占位符
     * 由于NewsItem没有时间字段，这里使用一些逻辑来生成
     */
    private fun generateTimePlaceholder(news: NewsItem): String {
        // 可以根据id或其他逻辑生成相对时间
        return when (news.id % 3) {
            0 -> "刚刚"
            1 -> "1小时前"
            else -> "今天"
        }
    }

    /**
     * 构建新闻上下文文本（供AI使用）
     */
    fun buildNewsContext(newsItems: List<NewsItem>): String {
        val aiNewsItems = convertToAINewsItems(newsItems)

        return if (aiNewsItems.isNotEmpty()) {
            "当前新闻上下文：\n\n" +
                    aiNewsItems.joinToString("\n\n") { news ->
                        "标题：${news.title}\n" +
                                "来源：${news.source}\n" +
                                "时间：${news.time}\n" +
                                "内容摘要：${news.content?.take(200) ?: "点击查看详情"}..."
                    }
        } else {
            "暂无新闻上下文"
        }
    }
}