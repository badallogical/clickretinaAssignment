package com.example.clickassignment.data.repo

import android.util.Log
import com.example.clickassignment.data.model.ApiResponse
import com.example.clickassignment.data.remote.ApiService

class MainRepository(private val apiService: ApiService) {

    suspend fun getData(): ApiResponse {
        // Make the network call using Retrofit or your network library
        val response = apiService.getData()
        Log.d("ApiService", response.toString() )
        return response
    }

}