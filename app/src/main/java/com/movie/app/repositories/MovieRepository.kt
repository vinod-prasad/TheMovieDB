package com.movie.app.repositories

import com.movie.app.apis.MovieAPIService

class MovieRepository constructor(private val movieAPIService: MovieAPIService) {

    suspend fun getPopularMovies() = movieAPIService.getPopularMovies()
    suspend fun getMovieDetails() = movieAPIService.getMovieDetails()

}