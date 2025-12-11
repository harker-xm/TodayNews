package com.example.toutiao.ui.topBar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 仿今日头条搜索框 - 增强版
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    onSearchClick: (String) -> Unit = {}  // 修改：回调时传递搜索内容
) {
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(48.dp)
            .background(Color.White, shape = RoundedCornerShape(7.dp))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        // 左侧搜索图标
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "搜索",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )

        // 使用 BasicTextField 替代原来的提示文本
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 36.dp, end = 80.dp)
                .align(Alignment.CenterStart)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (text.isNotBlank()) {
                        onSearchClick(text)
                        keyboardController?.hide()
                    }
                }
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // 当没有焦点且文本为空时显示提示
                    if (text.isEmpty() && !isFocused) {
                        FadingText(
                            text = "习近平总书记最新重要论述 | 乌拉圭...",
                            modifier = Modifier.fillMaxWidth(),
                            fadeWidth = 40.dp
                        )
                    }
                    innerTextField()
                }
            }
        )

        // 清除按钮
        if (text.isNotEmpty()) {
            IconButton(
                onClick = { text = "" },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 60.dp)
                    .size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "清除",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // 右侧「搜索」按钮
        Text(
            text = "搜索",
            fontSize = 15.sp,
            color = Color(0xFFD43C33),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    if (text.isNotBlank()) {
                        onSearchClick(text)
                        keyboardController?.hide()
                    }
                }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}