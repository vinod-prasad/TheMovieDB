package com.movie.app.apis

import com.movie.app.BuildConfig
import com.movie.app.models.MovieDetails
import com.movie.app.models.PopularMovies
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import okhttp3.OkHttpClient
import retrofit2.http.Path
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.HttpUrl
import okhttp3.Interceptor

interface RetrofitAPIService {

    @GET("top_rated")
    suspend fun getTopRatedMovies(): Response<List<PopularMovies>>

    @GET("popular")
    suspend fun getPopularMovies(): Response<PopularMovies>

    @GET("{id}")
    suspend fun getMovieDetails(@Path("id") movieId: Int): Response<MovieDetails>

    companion object {

        private var retrofitAPIService: RetrofitAPIService? = null

        fun getInstance(): RetrofitAPIService {

            if (retrofitAPIService == null) {

                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                val clientDefaultConfigInterceptor = Interceptor { chain: Interceptor.Chain ->
                    var request = chain.request()
                    val url: HttpUrl =
                        request.url.newBuilder()
                            .addQueryParameter("api_key", "5a3706d8cf58850fe3ad58eeee37c1d0")
                            .addQueryParameter("language", "en-US")
                            .addQueryParameter("page", "1")
                            .build()
                    request = request.newBuilder().url(url).build()
                    chain.proceed(request)
                }

                // OkHttpClient. Be conscious with the order
                val okHttpClient: OkHttpClient = OkHttpClient()
                    .newBuilder()
                    .addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(clientDefaultConfigInterceptor)
                    .build()

                val retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BuildConfig.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                retrofitAPIService = retrofit.create(RetrofitAPIService::class.java)
            }
            return retrofitAPIService!!
        }
    }
}