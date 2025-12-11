package com.example.toutiao.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.toutiao.data.AppDatabase
import com.example.toutiao.data.NewsLocalDataSource
import com.example.toutiao.data.NewsRepository

class NewsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            val database = AppDatabase.getDatabase(context)
            val repository = NewsRepository(database.newsDao())
            val localDataSource = NewsLocalDataSource()
            return NewsViewModel(repository, localDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}