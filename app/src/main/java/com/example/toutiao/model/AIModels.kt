// 文件：models/AIModels.kt
package com.example.toutiao.model

import com.google.gson.annotations.SerializedName


data class ChatMessage(
    @SerializedName("role")
    val role: String, // "system", "user", "assistant"

    @SerializedName("content")
    val content: String
)
data class ChatRequest(
    @SerializedName("model")
    val model: String = "deepseek-ai/DeepSeek-V3.2",

    @SerializedName("messages")
    val messages: List<ChatMessage>,

    @SerializedName("stream")
    val stream: Boolean = true,

    @SerializedName("max_tokens")
    val maxTokens: Int = 1000,

    @SerializedName("temperature")
    val temperature: Double = 0.7
)

// 添加 Message 类，用于非流式响应
data class Message(
    @SerializedName("role")
    val role: String? = null,

    @SerializedName("content")
    val content: String? = null
)

// 修改 ChatResponse，支持两种格式
data class ChatResponse(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("object")
    val objectType: String? = null,

    @SerializedName("created")
    val created: Long? = null,

    @SerializedName("model")
    val model: String? = null,

    @SerializedName("choices")
    val choices: List<Choice>? = null
)

data class Choice(
    @SerializedName("index")
    val index: Int? = null,

    @SerializedName("delta")
    val delta: Delta? = null,

    @SerializedName("message")
    val message: Message? = null,

    @SerializedName("finish_reason")
    val finishReason: String? = null
)

data class Delta(
    @SerializedName("role")
    val role: String? = null,

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("reasoning_content")
    val reasoningContent: String? = null
)

// 用于SSE流式响应的数据类
data class SSEResponse(
    val id: String? = null,
    val event: String? = null,
    val data: String? = null
)

// UI层使用的消息模型
data class UIChatMessage(
    val id: String,
    val role: Role,
    val content: String,
    val thinkingContent: String = "",
    val isComplete: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

enum class Role {
    USER,
    ASSISTANT,
    SYSTEM
}