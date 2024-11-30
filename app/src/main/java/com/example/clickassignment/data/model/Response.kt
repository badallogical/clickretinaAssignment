package com.example.clickassignment.data.model

data class ApiResponse(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage,
    val system_fingerprint: String? = null // Nullable as it can be null
)

data class Choice(
    val index: Int,
    val message: Message,
    val logprobs: Any? = null, // Nullable as it can be null
    val finish_reason: String
)

data class Message(
    val role: String,
    val content: Content,
    val refusal: String? = null // Nullable as it can be null
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

data class Content(
    val titles : List<String> = emptyList(),
    val description : String = ""
)