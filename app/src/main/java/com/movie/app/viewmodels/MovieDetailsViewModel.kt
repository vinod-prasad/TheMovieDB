package com.movie.app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movie.app.models.MovieDetails
import com.movie.app.models.PopularMovies
import com.movie.app.repositories.MoviesRepository
import kotlinx.coroutines.*

class MovieDetailsViewModel constructor(private val moviesRepository: MoviesRepository): ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val movieDetails = MutableLiveData<MovieDetails>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getMovieDetailsById(movieId:Int) {

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = moviesRepository.getMovieDetails(movieId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    movieDetails.postValue(response.body())
                    loading.value = false
                } else {
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