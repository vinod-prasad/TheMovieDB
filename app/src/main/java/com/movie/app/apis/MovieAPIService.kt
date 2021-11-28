package com.movie.app.apis

import com.movie.app.models.Movie
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface MovieAPIService {

    @GET("movielist.json")
    suspend fun getPopularMovies() : Response<List<Movie>>
    suspend fun getMovieDetails(): Response<Movie>

    companion object {
        var retrofitService: MovieAPIService? = null
        fun getInstance() : MovieAPIService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://howtodoandroid.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(MovieAPIService::class.java)
            }
            return retrofitService!!
        }

    }
}