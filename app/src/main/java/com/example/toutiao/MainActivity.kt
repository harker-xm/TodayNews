//package com.example.toutiao
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.navigation.compose.rememberNavController
//import com.example.toutiao.navigation.AppNavGraph
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            val navController = rememberNavController()
//            AppNavGraph(navController)
//        }
//    }
//}

package com.example.toutiao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.toutiao.navigation.AppNavGraph
import com.example.toutiao.viewmodel.NewsViewModel
import com.example.toutiao.viewmodel.NewsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val newsViewModel: NewsViewModel = viewModel(
                factory = NewsViewModelFactory(applicationContext)
            )
            AppNavGraph(navController = navController, newsViewModel = newsViewModel)
        }
    }
}
