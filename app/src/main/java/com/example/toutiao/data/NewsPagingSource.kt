package com.example.toutiao.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.toutiao.model.NewsItem
import kotlinx.coroutines.delay

/**
 * 新闻分页数据源
 * 负责从数据库或网络获取分页数据
 */
class NewsPagingSource(
    private val repository: NewsRepository,
    private val category: String = "推荐",  // 新闻分类
) : PagingSource<Int, NewsItem>() {

    companion object {
        const val PAGE_SIZE = 10  // 每页加载的数据量
        const val INITIAL_PAGE = 1  // 初始页码
    }

    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? {
        // 当刷新时，返回最近的加载位置
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsItem> {
        return try {

            // 获取页码，如果为null则从初始页码开始
            val page = params.key ?: INITIAL_PAGE
            val pageSize = params.loadSize

            // 从Repository获取数据（这里需要Repository支持分页查询）
            val newsList = repository.getNewsByCategoryPaged(
                category = category,
                page = page,
                pageSize = pageSize
            )

            // 计算下一页和上一页的key
            val nextKey = if (newsList.size < pageSize) null else page + 1
            val prevKey = if (page == INITIAL_PAGE) null else page - 1

            LoadResult.Page(
                data = newsList,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            // 加载失败时返回错误
            LoadResult.Error(e)
        }
    }
}