package com.movie.app.repositories

import com.movie.app.apis.RetrofitAPIService

class MoviesRepository constructor(private val retrofitAPIService: RetrofitAPIService) {

    suspend fun getPopularMovies() = retrofitAPIService.getPopularMovies()
    suspend fun getTopRatedMovies() = retrofitAPIService.getTopRatedMovies()
    suspend fun getMovieDetails(movieId:Int) = retrofitAPIService.getMovieDetails(movieId)

}