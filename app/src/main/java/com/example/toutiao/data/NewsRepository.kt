//package com.example.toutiao.data
//
//import com.example.toutiao.model.NewsItem
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//
//class NewsRepository(
//    private val newsDao: NewsDao
//) {
//    fun getAllNews(): Flow<List<NewsItem>> {
//        return newsDao.getAllNews().map { entities ->
//            entities.map { it.toNewsItem() }
//        }
//    }
//
//    suspend fun insertNews(news: NewsItem) {
//        newsDao.insertNews(NewsEntity.fromNewsItem(news))
//    }
//
//    suspend fun insertAllNews(newsList: List<NewsItem>) {
//        val entities = newsList.map { NewsEntity.fromNewsItem(it) }
//        newsDao.insertAllNews(entities)
//    }
//
//    fun searchNews(query: String): Flow<List<NewsItem>> {
//        return newsDao.searchNews(query).map { entities ->
//            entities.map { it.toNewsItem() }
//        }
//    }
//
//    suspend fun getNewsCount(): Int {
//        return newsDao.getCount()
//    }
//
//    suspend fun clearAll() {
//        newsDao.deleteAll()
//    }
//
//    // 新增：根据 id 查询单条新闻（包含 content）
//    suspend fun getNewsById(id: Int): NewsItem? {
//        val entity = newsDao.getNewsById(id)
//        return entity?.toNewsItem()
//    }
//}

package com.example.toutiao.data

import androidx.paging.*
import com.example.toutiao.model.NewsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NewsRepository(
    private val newsDao: NewsDao
) {

    fun getAllNews(): Flow<List<com.example.toutiao.model.NewsItem>> {
        return newsDao.getAllNews().map { entities ->
            entities.map { it.toNewsItem() }
        }
    }

    suspend fun insertNews(news: com.example.toutiao.model.NewsItem, category: String = "推荐") {
        newsDao.insertNews(NewsEntity.fromNewsItem(news, category))
    }

    suspend fun insertAllNews(newsList: List<com.example.toutiao.model.NewsItem>, category: String = "推荐") {
        val entities = newsList.map { NewsEntity.fromNewsItem(it, category) }
        newsDao.insertAllNews(entities)
    }

    fun searchNews(query: String): Flow<List<com.example.toutiao.model.NewsItem>> {
        return newsDao.searchNews(query).map { entities ->
            entities.map { it.toNewsItem() }
        }
    }

    suspend fun getNewsCount(): Int {
        return newsDao.getCount()
    }

    suspend fun clearAll() {
        newsDao.deleteAll()
    }

    // 返回 NewsEntity 的 PagingData，让 ViewModel 处理转换
    fun getNewsPagingData(category: String): Flow<PagingData<NewsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = NewsPagingSource.PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = NewsPagingSource.PAGE_SIZE * 2
            ),
            pagingSourceFactory = {
                NewsPagingSource(this, category)
            }
        ).flow
    }

    // 返回 NewsEntity 列表
    suspend fun getNewsByCategoryPaged(
        category: String,
        page: Int,
        pageSize: Int
    ): List<NewsItem> {
        return newsDao.getNewsByCategoryPaged(category, page, pageSize)
            .map { it.toNewsItem() }
    }

    // 获取所有分类
    suspend fun getAllCategories(): List<String> {
        return newsDao.getAllCategories()
    }

    // 按分类获取新闻数量
    suspend fun getCountByCategory(category: String): Int {
        return newsDao.getCountByCategory(category)
    }

    // 通过ID获取单个新闻（供详情页使用）
    suspend fun getNewsById(id: Int): NewsItem? {
        return newsDao.getNewsById(id)?.toNewsItem()
    }
}