// 文件：viewmodel/AIChatViewModel.kt
package com.example.toutiao.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toutiao.api.AIClient
import com.example.toutiao.api.SSEvent
import com.example.toutiao.model.NewsItem
import com.example.toutiao.model.ChatMessage
import com.example.toutiao.model.UIChatMessage
import com.example.toutiao.model.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.sse.EventSource

class AIChatViewModel : ViewModel() {
    private val _chatMessages = MutableStateFlow<List<UIChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<UIChatMessage>> = _chatMessages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private var currentEventSource: EventSource? = null

    fun sendMessage(
        userInput: String,
        newsContext: List<NewsItem>
    ) {
        viewModelScope.launch {
            try {
                if (userInput.isBlank()) return@launch

                _isLoading.value = true
                _errorMessage.value = null

                // 添加用户消息
                val userMessage = UIChatMessage(
                    id = System.currentTimeMillis().toString(),
                    role = Role.USER,
                    content = userInput
                )
                _chatMessages.value = _chatMessages.value + userMessage

                // 添加AI消息占位符
                val aiMessageId = (System.currentTimeMillis() + 1).toString()
                val aiMessage = UIChatMessage(
                    id = aiMessageId,
                    role = Role.ASSISTANT,
                    content = "",
                    isComplete = false
                )
                _chatMessages.value = _chatMessages.value + aiMessage

                // 构建简单的消息列表
                val messages = buildSimpleMessages(userInput, newsContext)

                // 取消之前的请求
                currentEventSource?.cancel()

                // 使用流式请求
                currentEventSource = AIClient.streamChatWithSSE(messages) { event ->
                    updateAIMessage(aiMessageId, event)
                }

            } catch (e: Exception) {
                _errorMessage.value = "发送消息失败: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    private fun buildSimpleMessages(
        userInput: String,
        newsContext: List<NewsItem>
    ): List<ChatMessage> {
        val messages = mutableListOf<ChatMessage>()

        // 构建极简系统消息
        val newsTitles = newsContext.take(3).joinToString("、") { it.title }
        val systemContent = "你是新闻助手。当前新闻：$newsTitles。请根据新闻回答问题。"

        messages.add(ChatMessage(
            role = "system",
            content = systemContent
        ))

        // 用户当前消息
        messages.add(ChatMessage(
            role = "user",
            content = userInput
        ))

        return messages
    }

    private fun updateAIMessage(messageId: String, event: SSEvent) {
        val currentMessages = _chatMessages.value.toMutableList()
        val messageIndex = currentMessages.indexOfFirst { it.id == messageId }

        if (messageIndex != -1) {
            val oldMessage = currentMessages[messageIndex]

            when (event.type) {
                "content" -> {
                    currentMessages[messageIndex] = oldMessage.copy(
                        content = oldMessage.content + event.content
                    )
                }
                "done" -> {
                    currentMessages[messageIndex] = oldMessage.copy(
                        isComplete = true
                    )
                    _isLoading.value = false
                }
                "error" -> {
                    currentMessages[messageIndex] = oldMessage.copy(
                        content = "AI服务暂时不可用: ${event.content}",
                        isComplete = true
                    )
                    _errorMessage.value = event.content
                    _isLoading.value = false
                }
                "closed" -> {
                    if (!oldMessage.isComplete) {
                        currentMessages[messageIndex] = oldMessage.copy(
                            isComplete = true
                        )
                    }
                    _isLoading.value = false
                }
            }

            _chatMessages.value = currentMessages
        }
    }

    // 备选方法：使用非流式对话
    suspend fun sendMessageSimple(
        userInput: String,
        newsContext: List<NewsItem>
    ) {
        try {
            _isLoading.value = true
            _errorMessage.value = null

            // 添加用户消息
            val userMessage = UIChatMessage(
                id = System.currentTimeMillis().toString(),
                role = Role.USER,
                content = userInput
            )
            _chatMessages.value = _chatMessages.value + userMessage

            // 添加AI消息占位符
            val aiMessageId = (System.currentTimeMillis() + 1).toString()
            val aiMessage = UIChatMessage(
                id = aiMessageId,
                role = Role.ASSISTANT,
                content = "正在思考...",
                isComplete = false
            )
            _chatMessages.value = _chatMessages.value + aiMessage

            // 构建消息
            val messages = buildSimpleMessages(userInput, newsContext)

            // 使用简单对话
            val response = AIClient.simpleChat(messages)

            // 更新消息
            updateAIMessageSimple(aiMessageId, response)

        } catch (e: Exception) {
            _errorMessage.value = "发送消息失败: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    private fun updateAIMessageSimple(messageId: String, content: String) {
        val currentMessages = _chatMessages.value.toMutableList()
        val messageIndex = currentMessages.indexOfFirst { it.id == messageId }

        if (messageIndex != -1) {
            currentMessages[messageIndex] = currentMessages[messageIndex].copy(
                content = content,
                isComplete = true
            )
            _chatMessages.value = currentMessages
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearChat() {
        _chatMessages.value = emptyList()
        currentEventSource?.cancel()
        currentEventSource = null
    }

    override fun onCleared() {
        super.onCleared()
        currentEventSource?.cancel()
    }
}