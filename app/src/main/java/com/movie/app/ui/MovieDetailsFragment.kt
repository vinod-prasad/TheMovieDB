package com.movie.app.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.movie.app.BuildConfig
import com.movie.app.R
import com.movie.app.apis.RetrofitAPIService
import com.movie.app.base.MyViewModelFactory
import com.movie.app.databinding.FragmentMovieDetailsBinding
import com.movie.app.repositories.MoviesRepository
import com.movie.app.util.Utility
import com.movie.app.viewmodels.MovieDetailsViewModel
import timber.log.Timber

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MovieDetailsFragment : Fragment() {

    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var movieDetailsCallBack: MovieDetailsCallBack
    private var _binding: FragmentMovieDetailsBinding? = null
    private lateinit var args: MovieDetailsFragmentArgs

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        movieDetailsCallBack = activity as MovieDetailsCallBack
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofitService = RetrofitAPIService.getInstance()
        val mainRepository = MoviesRepository(retrofitService)

        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(mainRepository)
        ).get(MovieDetailsViewModel::class.java)

        viewModel.movieDetails.observe(viewLifecycleOwner, {
            it.apply {
                binding.name.text = title
                binding.overview.text = overview
                movieDetailsCallBack.posterImageUpdate(poster_path)
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, {

            val networkState = Utility.isOnline(activity as Context)
            if (!networkState)
                Toast.makeText(activity, getString(R.string.no_network), Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        })

        //Using SafeArgs Concept
        val bundle = arguments
        if (bundle == null) {
            Timber.e("MovieDetailsFragmentArgs did not received Movie Id")
            return
        }
        val args = MovieDetailsFragmentArgs.fromBundle(bundle)
        viewModel.getMovieDetailsById(args.movieId)

        //Using bundle Concept
        /*var movieId = 580489 //Default movie ID
        arguments?.let {
            movieId = it.getInt(Constants.BUNDLE_KEY_SEL_MOV_ID, movieId)
            viewModel.getMovieDetailsById(movieId)
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface MovieDetailsCallBack {
        fun posterImageUpdate(posterPath: String)
    }
}