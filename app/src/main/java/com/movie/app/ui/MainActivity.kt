package com.movie.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.material.appbar.AppBarLayout
import com.movie.app.R
import com.movie.app.databinding.ActivityMainBinding
import timber.log.Timber
import com.bumptech.glide.Glide
import com.movie.app.BuildConfig
import kotlin.math.abs


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
     * Fragment Change Listener
     */
    private val listener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            Timber.d("==> OnDestinationChangedListener:: ${destination.id} : ${destination.label}")
            appBarLayoutConfigOnFragmentLaunch(destination.id)
        }

    /**
     * Event trigger when fragment/destination changes
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
                    Timber.d("onOffsetChanged: ======Collapsed======$verticalOffset");
                    appBarLayout.setBackgroundResource(R.color.appBarLayout_background)
                    binding.toolbar.setBackgroundResource(R.color.colorPrimary)
                    binding.toolbar.visibility = View.VISIBLE
                    binding.toolbarTitle.text = screenName
                    binding.toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white))
                } else {
                    //Expanded
                    Timber.d("onOffsetChanged: ======Expanded======$verticalOffset");
                    appBarLayout.setBackgroundResource(R.color.appBarLayout_background)
                    binding.toolbar.setBackgroundResource(R.color.colorPrimary)
                    binding.toolbar.visibility = View.VISIBLE
                    binding.toolbarTitle.text = screenName
                    binding.toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
            }
        )
    }

    override fun posterImageUpdate(posterPath: String) {
        loadPosterImage(posterPath)
    }

    private fun loadPosterImage(posterPath:String){
        Glide.with(binding.commonImageview.context)
            .load("${BuildConfig.ORIGINAL_IMAGE_URL}${posterPath}")
            .into(binding.commonImageview)
    }
    private fun clearPosterImage(){
        binding.commonImageview.setImageResource(android.R.color.transparent)
    }
}