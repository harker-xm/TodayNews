//package com.example.toutiao.data
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import androidx.room.TypeConverters
//import com.example.toutiao.model.NewsItem
//import com.example.toutiao.model.NewsType
//
//@Entity(tableName = "news")
//@TypeConverters(Converters::class)
//data class NewsEntity(
//    @PrimaryKey val id: Int,
//    val type: NewsType,
//    val title: String,
//    val source: String,
//    val commentCount: Int,
//    val publishTime: Long = System.currentTimeMillis(),
//
//    // 以下字段根据类型可为空
//    val mainImageUrl: String? = null,      // 单图新闻图片
//    val videoCoverUrl: String? = null,     // 视频封面
//    val videoDuration: String? = null,     // 视频时长
//    val content: String? = null,           // 新闻正文
//    val imageUrls: List<String>? = null    // 三图新闻的图片列表
//) {
//    // 转换为 domain 层的 NewsItem（包含 content）
//    fun toNewsItem(): NewsItem {
//        return when (type) {
//            NewsType.TEXT_ONLY -> NewsItem.TextOnly(
//                id = id,
//                title = title,
//                source = source,
//                commentCount = commentCount,
//                content = content
//            )
//            NewsType.RIGHT_IMAGE -> NewsItem.RightImage(
//                id = id,
//                title = title,
//                source = source,
//                commentCount = commentCount,
//                imageUrl = mainImageUrl ?: "",
//                content = content
//            )
//            NewsType.THREE_IMAGE -> NewsItem.ThreeImage(
//                id = id,
//                title = title,
//                source = source,
//                commentCount = commentCount,
//                imageUrls = imageUrls ?: emptyList(),
//                content = content
//            )
//            NewsType.VIDEO -> NewsItem.Video(
//                id = id,
//                title = title,
//                source = source,
//                commentCount = commentCount,
//                coverUrl = videoCoverUrl ?: "",
//                duration = videoDuration ?: "",
//                content = content
//            )
//        }
//    }
//
//    companion object {
//        // 从 NewsItem 创建 NewsEntity（包含 content）
//        fun fromNewsItem(newsItem: NewsItem): NewsEntity {
//            return when (newsItem) {
//                is NewsItem.TextOnly -> NewsEntity(
//                    id = newsItem.id,
//                    type = NewsType.TEXT_ONLY,
//                    title = newsItem.title,
//                    source = newsItem.source,
//                    commentCount = newsItem.commentCount,
//                    content = newsItem.content
//                )
//                is NewsItem.RightImage -> NewsEntity(
//                    id = newsItem.id,
//                    type = NewsType.RIGHT_IMAGE,
//                    title = newsItem.title,
//                    source = newsItem.source,
//                    commentCount = newsItem.commentCount,
//                    mainImageUrl = newsItem.imageUrl,
//                    content = newsItem.content
//                )
//                is NewsItem.ThreeImage -> NewsEntity(
//                    id = newsItem.id,
//                    type = NewsType.THREE_IMAGE,
//                    title = newsItem.title,
//                    source = newsItem.source,
//                    commentCount = newsItem.commentCount,
//                    imageUrls = newsItem.imageUrls,
//                    content = newsItem.content
//                )
//                is NewsItem.Video -> NewsEntity(
//                    id = newsItem.id,
//                    type = NewsType.VIDEO,
//                    title = newsItem.title,
//                    source = newsItem.source,
//                    commentCount = newsItem.commentCount,
//                    videoCoverUrl = newsItem.coverUrl,
//                    videoDuration = newsItem.duration,
//                    content = newsItem.content
//                )
//            }
//        }
//    }
//}

package com.example.toutiao.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.toutiao.model.NewsType

@Entity(tableName = "news")
@TypeConverters(Converters::class)
data class NewsEntity(
    @PrimaryKey val id: Int,
    val type: NewsType,
    val title: String,
    val source: String,
    val commentCount: Int,
    val publishTime: Long = System.currentTimeMillis(),

    // 以下字段根据类型可为空
    val mainImageUrl: String? = null,      // 单图新闻图片
    val videoCoverUrl: String? = null,     // 视频封面
    val videoDuration: String? = null,     // 视频时长
    val content: String? = null,           // 新闻正文
    val imageUrls: List<String>? = null,   // 三图新闻的图片列表

    // 新增字段：用于分页和排序
    val category: String = "推荐",           // 新闻分类（对应频道）
    val weight: Int = 0,                   // 权重，用于排序（热门度）
    val pageIndex: Int = 0                  // 原始数据中的页码（用于模拟分页）
) {
    // 辅助函数：转换为NewsItem
    fun toNewsItem(): com.example.toutiao.model.NewsItem {
        return when (type) {
            NewsType.TEXT_ONLY -> com.example.toutiao.model.NewsItem.TextOnly(
                id, title, source, commentCount, content
            )
            NewsType.RIGHT_IMAGE -> com.example.toutiao.model.NewsItem.RightImage(
                id, title, source, commentCount, mainImageUrl ?: "", content
            )
            NewsType.THREE_IMAGE -> com.example.toutiao.model.NewsItem.ThreeImage(
                id, title, source, commentCount, imageUrls ?: emptyList(), content
            )
            NewsType.VIDEO -> com.example.toutiao.model.NewsItem.Video(
                id, title, source, commentCount, videoCoverUrl ?: "", videoDuration ?: "", content
            )
        }
    }

    companion object {
        // 从NewsItem创建NewsEntity
        fun fromNewsItem(newsItem: com.example.toutiao.model.NewsItem, category: String = "推荐"): NewsEntity {
            return when (newsItem) {
                is com.example.toutiao.model.NewsItem.TextOnly -> NewsEntity(
                    id = newsItem.id,
                    type = NewsType.TEXT_ONLY,
                    title = newsItem.title,
                    source = newsItem.source,
                    commentCount = newsItem.commentCount,
                    content = newsItem.content,
                    category = category,
                    weight = newsItem.commentCount * 10 // 根据评论数计算权重
                )
                is com.example.toutiao.model.NewsItem.RightImage -> NewsEntity(
                    id = newsItem.id,
                    type = NewsType.RIGHT_IMAGE,
                    title = newsItem.title,
                    source = newsItem.source,
                    commentCount = newsItem.commentCount,
                    mainImageUrl = newsItem.imageUrl,
                    content = newsItem.content,
                    category = category,
                    weight = newsItem.commentCount * 10
                )
                is com.example.toutiao.model.NewsItem.ThreeImage -> NewsEntity(
                    id = newsItem.id,
                    type = NewsType.THREE_IMAGE,
                    title = newsItem.title,
                    source = newsItem.source,
                    commentCount = newsItem.commentCount,
                    imageUrls = newsItem.imageUrls,
                    content = newsItem.content,
                    category = category,
                    weight = newsItem.commentCount * 10
                )
                is com.example.toutiao.model.NewsItem.Video -> NewsEntity(
                    id = newsItem.id,
                    type = NewsType.VIDEO,
                    title = newsItem.title,
                    source = newsItem.source,
                    commentCount = newsItem.commentCount,
                    videoCoverUrl = newsItem.coverUrl,
                    videoDuration = newsItem.duration,
                    content = newsItem.content,
                    category = category,
                    weight = newsItem.commentCount * 15 // 视频权重更高
                )
            }
        }
    }
}