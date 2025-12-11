// 文件：api/AIService.kt
package com.example.toutiao.api

import com.example.toutiao.model.ChatRequest  // 改为 model
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AIService {
    @Headers(
        "Content-Type: application/json",
        "Accept: text/event-stream"
    )
    @POST("v1/chat/completions")
    suspend fun streamChatCompletion(
        @Header("Authorization") authHeader: String,
        @Body chatRequest: ChatRequest
    ): Response<ResponseBody>

    // 非流式版本
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun chatCompletion(
        @Header("Authorization") authHeader: String,
        @Body chatRequest: ChatRequest
    ): Response<com.example.toutiao.model.ChatResponse>  // 改为 model
}