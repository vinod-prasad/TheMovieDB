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
import com.movie.app.R
import com.movie.app.adapters.PopularMoviesAdapter
import com.movie.app.apis.RetrofitAPIService
import com.movie.app.base.MyViewModelFactory
import com.movie.app.databinding.FragmentMoviesBinding
import com.movie.app.repositories.MoviesRepository
import com.movie.app.util.Utility
import com.movie.app.viewmodels.PopularMoviesViewModel
import timber.log.Timber


/**
 * The fragment is used to list the Top 10 popular movies and the default destination in the navigation.
 */
class PopularMoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private lateinit var viewModel: PopularMoviesViewModel
    private lateinit var adapter: PopularMoviesAdapter

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofitService = RetrofitAPIService.getInstance()
        val moviesRepository = MoviesRepository(retrofitService)

        context?.let {
            adapter = PopularMoviesAdapter(it)
        }

        binding.recyclerview.adapter = adapter
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(moviesRepository)
        ).get(PopularMoviesViewModel::class.java)

        viewModel.movieList.observe(viewLifecycleOwner, { response ->

            var maxCount = 10 //Max ten movie need to show

            // safe maxCount depends on response if size is less then 10
            if (response.results.size < maxCount)
                maxCount = response.results.size

            //Filter top ten movies
            var topTenMovies = response.results
                .sortedBy { it.popularity }
                .subList(0, maxCount)
            adapter.submitList(topTenMovies)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, {

            val networkState = Utility.isOnline(activity as Context)
            if (!networkState)
                Toast.makeText(activity, getString(R.string.no_network), Toast.LENGTH_LONG).show()
            else
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        })

        viewModel.getAllMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}