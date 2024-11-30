package com.example.clickassignment.data.repo

import android.content.Context
import com.example.clickassignment.data.remote.RetrofitInstance

class RepoContainer(private val context : Context) {

    val mainRepository : MainRepository by lazy{
        MainRepository(RetrofitInstance.api)
    }

}