package com.movie.app.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.movie.app.BuildConfig
import com.movie.app.R
import com.movie.app.databinding.MovieListItemBinding
import com.movie.app.models.PopularMovies
import com.movie.app.ui.PopularMoviesFragmentDirections
import timber.log.Timber

/**
 * We have used ListAdaptor.
 */
class PopularMoviesAdapter(private val context: Context) :
    ListAdapter<PopularMovies.Result, PopularMoviesAdapter.ViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(context)
        var binding = MovieListItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { popularMovieResult ->
            with(holder) {
                itemView.tag = holder
                binding.name.text = popularMovieResult.title
                Glide.with(holder.itemView.context)
                    .load("${BuildConfig.ORIGINAL_IMAGE_URL}${popularMovieResult.poster_path}")
                    .into(holder.binding.imageview)

                //Click Event
                holder.binding.imageview.setOnClickListener {
                    Timber.e("Selected movie : ${popularMovieResult.title}")

                    //Using SafeArgs Concept
                    val action = PopularMoviesFragmentDirections.actionMoviesFragmentToDetailsFragment()
                    action.movieId = popularMovieResult.id
                    it.findNavController().navigate(action)

                    //Using bundle Concept
                    /*val bundle = Bundle()
                    bundle.putInt(BUNDLE_KEY_SEL_MOV_ID, popularMovieResult.id)
                    holder.itemView.findNavController().navigate(
                        R.id.action_MoviesFragment_to_DetailsFragment, bundle
                    )*/
                }
            }
        }
    }

    class ViewHolder(var binding: MovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private class DiffCallBack : DiffUtil.ItemCallback<PopularMovies.Result>() {

        override fun areItemsTheSame(
            oldItem: PopularMovies.Result,
            newItem: PopularMovies.Result
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: PopularMovies.Result,
            newItem: PopularMovies.Result
        ): Boolean {
            return oldItem == newItem
        }
    }
}