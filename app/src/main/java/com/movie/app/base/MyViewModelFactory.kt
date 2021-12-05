package com.movie.app.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movie.app.repositories.MoviesRepository
import com.movie.app.viewmodels.MovieDetailsViewModel
import com.movie.app.viewmodels.PopularMoviesViewModel

/**
 * Custom ViewModel Factory
 */
class MyViewModelFactory constructor(private val repository: MoviesRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PopularMoviesViewModel::class.java) -> {
                PopularMoviesViewModel(this.repository) as T
            }
            modelClass.isAssignableFrom(MovieDetailsViewModel::class.java) -> {
                MovieDetailsViewModel(this.repository) as T
            }
            else -> {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}