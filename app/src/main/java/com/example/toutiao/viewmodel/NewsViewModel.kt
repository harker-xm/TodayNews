//package com.example.toutiao.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.flow.*
//import kotlinx.coroutines.launch
//import com.example.toutiao.data.*
//import com.example.toutiao.model.NewsItem
//import java.util.regex.Pattern
//
///**
// * 增强版 ViewModel：支持双重数据源（数据库 + 内存）
// * 提供同步读取单条新闻的方法（suspend getNewsDetail）
// */
//class NewsViewModel(
//    private val repository: NewsRepository,
//    private val localDataSource: NewsLocalDataSource
//) : ViewModel() {
//
//    // 数据源状态
//    private val _dataSource = MutableStateFlow(DataSource.DATABASE)
//    val dataSource = _dataSource.asStateFlow()
//
//    // 新闻列表
//    private val _newsList = MutableStateFlow<List<NewsItem>>(emptyList())
//    val newsList = _newsList.asStateFlow()
//
//    // 搜索结果
//    private val _searchResults = MutableStateFlow<List<NewsItem>>(emptyList())
//    val searchResults = _searchResults.asStateFlow()
//
//    // 搜索关键词
//    private val _searchQuery = MutableStateFlow("")
//    val searchQuery = _searchQuery.asStateFlow()
//
//    // 加载状态
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading = _isLoading.asStateFlow()
//
//    init {
//        loadInitialData()
//    }
//
//    private fun loadInitialData() {
//        viewModelScope.launch {
//            _isLoading.value = true
//
//            // 检查数据库是否有数据
//            val count = repository.getNewsCount()
//
//// 更新数据库改这里！！！！！！！！
//            if (count == 0) {
////                 数据库为空，使用本地数据并插入数据库
//                _dataSource.value = DataSource.LOCAL
//                _newsList.value = localDataSource.mockNews
//
//                // 异步插入到数据库
//                launch {
//                    repository.insertAllNews(localDataSource.mockNews)
//                    _dataSource.value = DataSource.DATABASE
//
//                    // 从数据库中读取并更新 list（可选）
//                    repository.getAllNews().collect { news ->
//                        _newsList.value = news
//                    }
//                }
//            } else {
//                // 从数据库加载
//                _dataSource.value = DataSource.DATABASE
//                repository.getAllNews().collect { news ->
//                    _newsList.value = news
//                }
//            }
//
//            _isLoading.value = false
//        }
//    }
//
//    /**
//     * 同步获取指定 id 的新闻详情（包含 content）。
//     * 这是一个 suspend 函数，UI 层应在协程中调用（例如 LaunchedEffect）
//     */
//    suspend fun getNewsDetail(id: Int): NewsItem? {
//        return when (_dataSource.value) {
//            DataSource.DATABASE -> repository.getNewsById(id)
//            DataSource.LOCAL -> localDataSource.getNewsById(id)
//        }
//    }
//
//    // 以下保留搜索与刷新等方法（你已有的逻辑，我不做改动）
//    fun searchNews(pattern: String, caseSensitive: Boolean = false) {
//        _searchQuery.value = pattern
//
//        if (pattern.isBlank()) {
//            _searchResults.value = emptyList()
//            return
//        }
//
//        viewModelScope.launch {
//            when (_dataSource.value) {
//                DataSource.DATABASE -> {
//                    // 从数据库搜索
//                    repository.searchNews(pattern).collect { results ->
//                        val filtered = applyRegexFilter(results, pattern, caseSensitive)
//                        _searchResults.value = filtered
//                    }
//                }
//                DataSource.LOCAL -> {
//                    val results = localDataSource.searchNews(pattern)
//                    val filtered = applyRegexFilter(results, pattern, caseSensitive)
//                    _searchResults.value = filtered
//                }
//            }
//        }
//    }
//
//    private fun applyRegexFilter(
//        newsList: List<NewsItem>,
//        pattern: String,
//        caseSensitive: Boolean
//    ): List<NewsItem> {
//        return try {
//            val regex = if (caseSensitive) {
//                Pattern.compile(pattern)
//            } else {
//                Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)
//            }
//
//            newsList.filter { news ->
//                regex.matcher(news.title).find() || regex.matcher(news.source).find()
//            }
//        } catch (e: Exception) {
//            newsList.filter { news ->
//                news.title.contains(pattern, ignoreCase = !caseSensitive) ||
//                        news.source.contains(pattern, ignoreCase = !caseSensitive)
//            }
//        }
//    }
//
//    fun clearSearch() {
//        _searchQuery.value = ""
//        _searchResults.value = emptyList()
//    }
//
//    fun refreshData() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            repository.clearAll()
//            repository.insertAllNews(localDataSource.mockNews)
//            _isLoading.value = false
//        }
//    }
//
//    fun switchDataSource(source: DataSource) {
//        _dataSource.value = source
//        when (source) {
//            DataSource.DATABASE -> {
//                viewModelScope.launch {
//                    repository.getAllNews().collect { news ->
//                        _newsList.value = news
//                    }
//                }
//            }
//            DataSource.LOCAL -> {
//                _newsList.value = localDataSource.mockNews
//            }
//        }
//    }
//}
//
//enum class DataSource {
//    DATABASE,
//    LOCAL
//}

// NewsViewModel.kt
package com.example.toutiao.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.toutiao.data.*
import com.example.toutiao.model.NewsItem
import java.util.regex.Pattern

class NewsViewModel(
    private val repository: NewsRepository,
    private val localDataSource: NewsLocalDataSource
) : ViewModel() {

    // === 状态管理 ===
    private val _selectedCategory = MutableStateFlow("推荐")
    val selectedCategory = _selectedCategory.asStateFlow()

    // 分页数据流 - 直接存储 Flow<PagingData<NewsItem>>
    private val _pagingDataFlow = MutableStateFlow<Flow<PagingData<NewsItem>>?>(null)
    val pagingDataFlow: StateFlow<Flow<PagingData<NewsItem>>?> = _pagingDataFlow.asStateFlow()

    // 新闻列表状态
    private val _newsListState = MutableStateFlow<NewsListState>(NewsListState.Loading)
    val newsListState = _newsListState.asStateFlow()

    // 刷新状态
    private val _refreshState = MutableStateFlow<RefreshState>(RefreshState.Idle)
    val refreshState = _refreshState.asStateFlow()

    // 搜索相关状态
    private val _searchResults = MutableStateFlow<List<NewsItem>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 所有分类列表
    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories = _categories.asStateFlow()

    init {
        loadInitialData()
        loadCategories()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _newsListState.value = NewsListState.Loading

            // 检查数据库是否有数据
            val count = repository.getNewsCount()

//            if (count == 0) {
                // 数据库为空，使用本地数据并插入数据库
                val allCategories = listOf("推荐", "热点", "科技", "国际", "军事", "财经", "北京", "娱乐")
                localDataSource.mockNews.chunked(3).forEachIndexed { index, newsChunk ->
                    val category = allCategories[index % allCategories.size]
                    repository.insertAllNews(newsChunk, category)
                }
//            }

            // 初始化分页数据流
            initPagingData()
        }
    }

    private fun initPagingData() {
        // 直接获取 NewsItem 的 PagingData
        val newsItemPagingFlow = repository.getNewsPagingData(_selectedCategory.value)

        _pagingDataFlow.value = newsItemPagingFlow
        _newsListState.value = NewsListState.Success(newsItemPagingFlow)
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val categories = repository.getAllCategories()
            if (categories.isNotEmpty()) {
                _categories.value = categories
            } else {
                // 默认分类
                _categories.value = listOf("推荐", "热点", "科技", "国际", "军事", "财经", "北京", "娱乐")
            }
        }
    }

    /**
     * 切换分类
     */
    fun selectCategory(category: String) {
        _selectedCategory.value = category
        initPagingData()
    }

    /**
     * 刷新数据
     */
    fun refresh() {
        viewModelScope.launch {
            _refreshState.value = RefreshState.Refreshing

            try {
                // 模拟网络请求延迟
                kotlinx.coroutines.delay(1500)
                // 重新初始化分页数据
                initPagingData()
                _refreshState.value = RefreshState.Success
                // 3秒后自动重置刷新状态
                launch {
                    kotlinx.coroutines.delay(3000)
                    if (_refreshState.value is RefreshState.Success) {
                        _refreshState.value = RefreshState.Idle
                    }
                }
            } catch (e: Exception) {
                _refreshState.value = RefreshState.Error("刷新失败: ${e.message}")
            }
        }
    }

    /**
     * 重置刷新状态
     */
    fun resetRefreshState() {
        _refreshState.value = RefreshState.Idle
    }

    /**
     * 使用正则表达式搜索新闻
     */
    fun searchNews(pattern: String, caseSensitive: Boolean = false) {
        _searchQuery.value = pattern

        if (pattern.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            // 从数据库搜索
            repository.searchNews(pattern).collect { results ->
                // 如果需要正则匹配，在内存中过滤
                val filtered = applyRegexFilter(results, pattern, caseSensitive)
                _searchResults.value = filtered
            }
        }
    }

    /**
     * 应用正则表达式过滤
     */
    private fun applyRegexFilter(
        newsList: List<NewsItem>,
        pattern: String,
        caseSensitive: Boolean
    ): List<NewsItem> {
        return try {
            val regex = if (caseSensitive) {
                Pattern.compile(pattern)
            } else {
                Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)
            }

            newsList.filter { news ->
                regex.matcher(news.title).find() || regex.matcher(news.source).find()
            }
        } catch (e: Exception) {
            // 正则表达式编译失败，使用简单包含
            newsList.filter { news ->
                news.title.contains(pattern, ignoreCase = !caseSensitive) ||
                        news.source.contains(pattern, ignoreCase = !caseSensitive)
            }
        }
    }

    /**
     * 清除搜索结果
     */
    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    /**
     * 获取新闻详情（通过ID）
     */
    suspend fun getNewsById(id: Int): NewsItem? {
        return repository.getNewsById(id)
    }
}

// 状态类
sealed class NewsListState {
    object Loading : NewsListState()
    data class Success(val data: Flow<PagingData<NewsItem>>) : NewsListState()
    data class Error(val message: String) : NewsListState()
    object Empty : NewsListState()  // 新增 Empty 状态
}

sealed class RefreshState {
    object Idle : RefreshState()
    object Refreshing : RefreshState()
    object Success : RefreshState()  // 保持为 object，因为你的代码中使用的是 RefreshState.Success
    data class Error(val message: String) : RefreshState()
}

sealed class LoadMoreState {
    object Idle : LoadMoreState()
    object Loading : LoadMoreState()
    data class Error(val message: String) : LoadMoreState()
    object EndOfList : LoadMoreState()  // 新增 EndOfList 状态
}
