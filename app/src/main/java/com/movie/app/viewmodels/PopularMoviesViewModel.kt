package com.movie.app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movie.app.models.PopularMovies
import com.movie.app.repositories.MoviesRepository
import com.movie.app.util.Utility
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * ViewModel
 */
class PopularMoviesViewModel constructor(private val moviesRepository: MoviesRepository): ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val movieList = MutableLiveData<PopularMovies>()
    var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

        Timber.e("Exception handled: ${throwable.localizedMessage}")
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getAllMovies() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = moviesRepository.getPopularMovies()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    movieList.postValue(response.body())
                    loading.value = false
                    Timber.e("Response : ${response.body()} ")
                } else {
                    Timber.e("Error : ${response.message()} ")
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        loading.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}