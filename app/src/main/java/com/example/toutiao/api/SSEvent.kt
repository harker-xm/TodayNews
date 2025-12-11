// 文件：api/SSEvent.kt
package com.example.toutiao.api

data class SSEvent(
    val type: String, // "thinking", "content", "thinking_done", "done", "error", "closed"
    val content: String = ""
)