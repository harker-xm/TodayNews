// 文件：TopBar.kt（部分修改）
package com.example.toutiao.ui.topBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AutoAwesome

@Composable
fun TopBar(
    onAIClick: () -> Unit = {},
    onSearchClick: (String) -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFD43C33))
            .padding(top = 24.dp)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // 第一行：天气信息和AI按钮横向排列
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧：天气信息
            Text(
                text = "北京 · 多云 14°",
                color = Color.White,
                fontSize = 16.sp
            )

            // 右侧：AI按钮 - 修复点击事件
            TextButton(
                onClick = {
                    println("TopBar中的AI按钮被点击")
                    onAIClick()
                },
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    Icon(
//                        imageVector = Icons.Filled.AutoAwesome,
//                        contentDescription = "AI问答",
//                        tint = Color.White,
//                        modifier = Modifier.size(18.dp)
//                    )
                    Text(
                        text = "AI回答",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 第二行：搜索栏
        SearchBar(
            onSearchClick = onSearchClick
        )
    }
}

// 预览函数
@Preview
@Composable
fun TopBarPreview() {
    TopBar(
        onAIClick = { println("预览中点击AI按钮") },
        onSearchClick = { println("预览中搜索: $it") }
    )
}