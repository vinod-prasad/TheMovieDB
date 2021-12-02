package com.movie.app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.movie.app.adapters.PopularMoviesAdapter
import com.movie.app.apis.RetrofitAPIService
import com.movie.app.base.MyViewModelFactory
import com.movie.app.databinding.FragmentMoviesBinding
import com.movie.app.repositories.MoviesRepository
import com.movie.app.viewmodels.PopularMoviesViewModel
import timber.log.Timber


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PopularMoviesFragment : Fragment() {

    private lateinit var viewModel: PopularMoviesViewModel
    private var _binding: FragmentMoviesBinding? = null
    private lateinit var adapter: PopularMoviesAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofitService = RetrofitAPIService.getInstance()
        val mainRepository = MoviesRepository(retrofitService)

        context?.let {
            adapter = PopularMoviesAdapter(it)
        }

        binding.recyclerview.adapter = adapter
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(mainRepository)
        ).get(PopularMoviesViewModel::class.java)

        viewModel.movieList.observe(viewLifecycleOwner, { response ->
            var topTenMovies = response.results
                .sortedBy { it.popularity }
                .subList(0, 10)
            adapter.submitList(topTenMovies)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
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