//package com.example.toutiao.data
//
//import androidx.room.*
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface NewsDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertNews(news: NewsEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAllNews(newsList: List<NewsEntity>)
//
//    @Query("SELECT * FROM news ORDER BY publishTime DESC")
//    fun getAllNews(): Flow<List<NewsEntity>>
//
//    @Query("SELECT * FROM news WHERE id = :id")
//    suspend fun getNewsById(id: Int): NewsEntity?
//
//    // 搜索功能
//    @Query("SELECT * FROM news WHERE title LIKE '%' || :query || '%' OR source LIKE '%' || :query || '%'")
//    fun searchNews(query: String): Flow<List<NewsEntity>>
//
//    @Query("SELECT COUNT(*) FROM news")
//    suspend fun getCount(): Int
//
//    @Query("DELETE FROM news")
//    suspend fun deleteAll()
//}

package com.example.toutiao.data

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    // 原有方法保持不变...

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: NewsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNews(newsList: List<NewsEntity>)

    @Query("SELECT * FROM news ORDER BY publishTime DESC")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news WHERE id = :id")
    suspend fun getNewsById(id: Int): NewsEntity?

    // 搜索功能
    @Query("SELECT * FROM news WHERE title LIKE '%' || :query || '%' OR source LIKE '%' || :query || '%'")
    fun searchNews(query: String): Flow<List<NewsEntity>>

    @Query("SELECT COUNT(*) FROM news")
    suspend fun getCount(): Int

    @Query("DELETE FROM news")
    suspend fun deleteAll()

    // 新增：按分类分页查询
    @Query("SELECT * FROM news WHERE category = :category ORDER BY weight DESC, publishTime DESC LIMIT :pageSize OFFSET (:page - 1) * :pageSize")
    suspend fun getNewsByCategoryPaged(
        category: String,
        page: Int,
        pageSize: Int
    ): List<NewsEntity>

    // 新增：按分类获取新闻数量
    @Query("SELECT COUNT(*) FROM news WHERE category = :category")
    suspend fun getCountByCategory(category: String): Int

    // 新增：获取所有分类
    @Query("SELECT DISTINCT category FROM news")
    suspend fun getAllCategories(): List<String>
}