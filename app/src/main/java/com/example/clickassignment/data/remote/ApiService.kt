package com.example.clickassignment.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.clickassignment.data.model.ApiResponse
import com.example.clickassignment.data.model.Content
import com.example.clickassignment.data.model.Message
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import retrofit2.http.GET
import java.lang.reflect.Type



interface ApiService {
    @GET("6HBE/") // Example API endpoint
    suspend fun getData(): ApiResponse
}


object RetrofitInstance {
    private const val BASE_URL = "https://www.jsonkeeper.com/b/" // Example base URL

    val gson = GsonBuilder()
        .registerTypeAdapter(Message::class.java, MessageDeserializer())
        .create()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}

class MessageDeserializer : JsonDeserializer<Message> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Message {
        val jsonObject = json.asJsonObject

        // Deserialize `role` (assume it is always present)
        val role = jsonObject["role"].asString

        // Safely handle `content`
        val contentJson = jsonObject["content"]?.asString ?: "{}" // Default to an empty JSON object
        val gson = Gson()
        val content = gson.fromJson(contentJson, Content::class.java)

        // Safely handle `refusal`
        val refusal = if (jsonObject["refusal"] is JsonNull) null else jsonObject["refusal"]?.asString

        return Message(
            role = role,
            content = content,
            refusal = refusal
        )
    }
}


