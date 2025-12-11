package com.example.toutiao.api

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.util.concurrent.TimeUnit

object AIClient {
    private const val BASE_URL = "https://api-inference.modelscope.cn/v1"
    private const val API_KEY = "your api key here"

    private val gson = Gson()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.SECONDS) // SSE read timeout: 0 == infinite
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader("Authorization", "Bearer $API_KEY")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "text/event-stream")
                .addHeader("User-Agent", "Android-App-Toutiao/1.0")
            chain.proceed(requestBuilder.build())
        }
        .build()

    /**
     * messages: List<com.example.toutiao.model.ChatMessage>
     * onEvent: 回调 SSEvent(type, content)
     */
    fun streamChatWithSSE(
        messages: List<com.example.toutiao.model.ChatMessage>,
        enableThinking: Boolean = true,
        onEvent: (SSEvent) -> Unit
    ): EventSource {
        val requestBodyJson = buildRequestBody(messages, enableThinking)

        Log.d("AIClient", "发送请求到: $BASE_URL/chat/completions")
        Log.d("AIClient", "请求体: $requestBodyJson")

        val request = Request.Builder()
            .url("$BASE_URL/chat/completions")
            .post(requestBodyJson.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        return EventSources.createFactory(okHttpClient)
            .newEventSource(request, createEventSourceListener(onEvent))
    }

    private fun buildRequestBody(
        messages: List<com.example.toutiao.model.ChatMessage>,
        enableThinking: Boolean
    ): String {
        // messages -> JSON array (role + content string)
        val messagesJson = messages.map { msg ->
            mapOf("role" to msg.role, "content" to msg.content)
        }

        // extra_body 与 Python 示例一致
        val requestMap = mutableMapOf<String, Any>(
            "model" to "deepseek-ai/DeepSeek-V3.2",
            "messages" to messagesJson,
            "stream" to true,
            "temperature" to 0.7,
            "max_tokens" to 2000
        )

        // ModelScope Python 示例使用 extra_body 来开启 thinking
        requestMap["extra_body"] = mapOf("enable_thinking" to enableThinking)

        return gson.toJson(requestMap)
    }

    private fun createEventSourceListener(onEvent: (SSEvent) -> Unit): EventSourceListener {
        return object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: okhttp3.Response) {
                Log.d("AIClient", "SSE 连接已建立，状态码: ${response.code}")
                if (!response.isSuccessful) {
                    val rb = try { response.body?.string() } catch (e: Exception) { null }
                    Log.e("AIClient", "连接失败响应: $rb")
                    onEvent(SSEvent(type = "error", content = "连接失败: ${response.code}"))
                }
            }

            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                // SSE 服务端结束标识可能是 [DONE] 或者空行
                if (data.trim() == "[DONE]") {
                    onEvent(SSEvent(type = "done"))
                    return
                }

                try {
                    // DeepSeek / ModelScope 在 stream 模式下，每个 chunk 数据是 JSON，结构类似 OpenAI delta
                    val chatResp = gson.fromJson(data, com.example.toutiao.model.ChatResponse::class.java)
                    val delta = chatResp.choices?.firstOrNull()?.delta

                    val thinkingChunk = delta?.reasoningContent ?: delta?.let { getField(delta, "reasoningContent") }
                    val contentChunk = delta?.content ?: delta?.let { getField(delta, "content") }

                    if (!thinkingChunk.isNullOrBlank()) {
                        onEvent(SSEvent(type = "thinking", content = thinkingChunk))
                    }
                    if (!contentChunk.isNullOrBlank()) {
                        onEvent(SSEvent(type = "content", content = contentChunk))
                    }
                } catch (e: Exception) {
                    // 解析失败，可能 data 本身不是 JSON（有些情况服务会发 plain text 错误）
                    Log.w("AIClient", "解析事件失败: $data", e)
                }
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: okhttp3.Response?) {
                val err = t?.message ?: "未知错误"
                val status = response?.code ?: -1
                var respBody: String? = null
                try { respBody = response?.body?.string() } catch (_: Exception) {}
                Log.e("AIClient", "请求失败: $err, 状态码: $status")
                Log.e("AIClient", "响应体: $respBody")
                onEvent(SSEvent(type = "error", content = "API请求失败: $err (状态码: $status)"))
            }

            override fun onClosed(eventSource: EventSource) {
                onEvent(SSEvent(type = "closed"))
            }
        }
    }

    // 备用：非流式调用（同步/异步都可）
    suspend fun simpleChat(messages: List<com.example.toutiao.model.ChatMessage>, enableThinking: Boolean = false): String {
        return withContext(Dispatchers.IO) {
            try {
                val req = buildSimpleRequestBody(messages, enableThinking)
                Log.d("AIClient", "简单请求体: $req")
                val request = Request.Builder()
                    .url("$BASE_URL/chat/completions")
                    .post(req.toRequestBody("application/json".toMediaTypeOrNull()))
                    .build()

                val resp = okHttpClient.newCall(request).execute()
                val bodyStr = resp.body?.string()
                Log.d("AIClient", "响应码: ${resp.code}")
                if (resp.isSuccessful) {
                    bodyStr ?: ""
                } else {
                    "请求失败 (${resp.code}): ${bodyStr?.take(200)}"
                }
            } catch (e: Exception) {
                Log.e("AIClient", "简单对话失败", e)
                "网络错误: ${e.message}"
            }
        }
    }

    private fun buildSimpleRequestBody(messages: List<com.example.toutiao.model.ChatMessage>, enableThinking: Boolean): String {
        val messagesJsonStr = messages.joinToString(",") { m ->
            // 需对 content 做转义以避免 JSON 格式错误
            val escaped = m.content.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
            """{"role":"${m.role}","content":"$escaped"}"""
        }
        val extra = if (enableThinking) """, "extra_body": {"enable_thinking": true}""" else ""
        return """
        {
          "model": "deepseek-ai/DeepSeek-V3.2",
          "messages": [ $messagesJsonStr ],
          "stream": false,
          "temperature": 0.7,
          "max_tokens": 1000
          $extra
        }
        """.trimIndent()
    }

    private fun getField(delta: Any, fieldName: String): String? {
        return try {
            val map = gson.fromJson(gson.toJson(delta), Map::class.java)
            val v = map[fieldName] ?: map[fieldName.replaceFirstChar { it.lowercaseChar() }]
            v?.toString()
        } catch (e: Exception) {
            null
        }
    }
}

fun String.toRequestBody(mediaType: okhttp3.MediaType?) =
    okhttp3.RequestBody.create(mediaType, this)
