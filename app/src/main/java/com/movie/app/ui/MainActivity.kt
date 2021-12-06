package com.movie.app.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.material.appbar.AppBarLayout
import com.movie.app.R
import com.movie.app.databinding.ActivityMainBinding
import timber.log.Timber
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.movie.app.BuildConfig
import kotlin.math.abs

/**
 * Single host activity of app for all fragments[app screens]
 */
class MainActivity : AppCompatActivity(), MovieDetailsFragment.MovieDetailsCallBack {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var screenName: String
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        // We can use "CollapsingToolbarLayout" for better user's Visual Experience
        appBarLayoutConfigOnSwipe()

        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        navController.removeOnDestinationChangedListener(listener)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(listener)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    /**
     * Event trigger / Fragment Change Listener
     */
    private val listener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            Timber.d("==> OnDestinationChangedListener:: ${destination.id} : ${destination.label}")
            appBarLayoutConfigOnFragmentLaunch(destination.id)
        }

    /**
     *  Identify the fragment when fragment/destination changes and do the
     *  handling depending on navigation destination(Screen).
     */
    private fun appBarLayoutConfigOnFragmentLaunch(fragId: Int) {

        when (fragId) {
            R.id.fragPopularMovies -> {
                binding.collapsingToolbarLayout.visibility = View.GONE
                binding.appBarLayout.setExpanded(false, true)
                screenName = getString(R.string.popular_movies_fragment_label)
                clearPosterImage()
            }
            R.id.fragMovieDetails -> {
                binding.collapsingToolbarLayout.visibility = View.VISIBLE
                binding.appBarLayout.setExpanded(true, true)
                screenName = getString(R.string.movie_details_fragment_label)
            }
        }
    }

    /**
     *  We can handle toolbarTitle and visibility on state change of
     *  CollapsingToolbarLayout from Collapsed to Expanded and vise versa
     */
    private fun appBarLayoutConfigOnSwipe() {
        binding.appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                    //  Collapsed
                    appBarLayout.setBackgroundResource(R.color.appBarLayout_background)
                    binding.toolbar.setBackgroundResource(R.color.colorPrimary)
                    binding.toolbar.visibility = View.VISIBLE
                    binding.toolbarTitle.text = screenName
                    binding.toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white))
                } else {
                    //Expanded
                    appBarLayout.setBackgroundResource(R.color.appBarLayout_background)
                    binding.toolbar.setBackgroundResource(R.color.colorPrimary)
                    binding.toolbar.visibility = View.VISIBLE
                    binding.toolbarTitle.text = screenName
                    binding.toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
            }
        )
    }

    /**
     * Get the poster path from Movie Details fragment
     */
    override fun posterImageUpdate(posterPath: String) {
        loadPosterImage(posterPath)
    }

    /**
     * Load image in CollapsingToolbarLayout
     */
    private fun loadPosterImage(posterPath: String) {
        binding.progressDialog.visibility = View.VISIBLE
        Glide.with(binding.commonImageview.context)
            .load("${BuildConfig.ORIGINAL_IMAGE_URL}${posterPath}")
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Timber.e("onLoadFailed: ${e.toString()}")
                    Toast.makeText(this@MainActivity, getString(R.string.unable_to_load_img), Toast.LENGTH_LONG).show()
                    binding.progressDialog.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Timber.d("OnResourceReady")
                    binding.progressDialog.visibility = View.GONE
                    return false
                }
            })
            .into(binding.commonImageview)
    }

    /**
     * Clear the older image while loading new image.
     */
    private fun clearPosterImage() {
        binding.commonImageview.setImageResource(android.R.color.transparent)
    }
}