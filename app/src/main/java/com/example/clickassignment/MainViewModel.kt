package com.example.clickassignment

import android.util.Log
import androidx.compose.material3.formatWithSkeleton
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.clickassignment.data.model.Content
import com.example.clickassignment.data.repo.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

data class ScreenUiState(
    val content : List<Content> = emptyList(),
    val isLoading : Boolean = false,
    val errorMsg : String? = null
)

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private var _uiState = MutableStateFlow(ScreenUiState())
    val uiState : StateFlow<ScreenUiState> = _uiState.asStateFlow()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication
                val repository = application.container.mainRepository // Assuming container contains the repository
                MainViewModel(repository)  // Pass the repository to the ViewModel constructor
            }
        }
    }

    init{
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            try {
                // Show loading state
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Fetch the data in a background thread
                val response = withContext(Dispatchers.IO) { repository.getData() }

                Log.d("MainViewModel", response.toString())

                // Parse the response into a list of Content
                val contentList = response.choices.map { choice ->
                     choice.message.content
                }

                Log.d("MainViewModel", "Content List ${contentList.toString()} ")

                // Update the UI state on the main thread with content
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    content = contentList,
                    errorMsg = null // Clear any previous error
                )

                Log.d("MainViewModel", contentList.toString())
            } catch (e: Exception) {
                e.printStackTrace()

                // Update the UI state with an error message
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    content = emptyList(),
                    errorMsg = e.localizedMessage ?: "An unexpected error occurred"
                )
            }
        }
    }


//    private fun parseContent(content: String): Content {
//        return try {
//            // Remove surrounding quotes and escape sequences
//            val formattedContent = content.replace("\\n", "") // Remove `\n`
//                .replace("\\","") // Replace escaped quotes
//                .trim()
//
//            // Parse JSON using Gson
//            Gson().fromJson(formattedContent, Content::class.java)
//        } catch (e: JsonSyntaxException) {
//            e.printStackTrace()
//            // Return a fallback Content in case of parsing error
//            Content(
//                title = listOf("Parsing error"),
//                description = "Could not parse content"
//            )
//        }
//    }

    // Parse the content string to the Content data model
    fun parseContent(contentString: String): Content {
        return try {
            val gson = Gson()
            gson.fromJson(contentString, Content::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Content() // Return an empty Content if parsing fails
        }
    }

}