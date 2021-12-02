package com.movie.app.ui

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
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

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var screenName: String
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.toolbar.visibility = View.GONE

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

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
    private val listener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
//            Timber.e("===> ${destination.id} : ${destination.label}")
            appBarLayoutConfigOnFragmentLaunch(destination.id)
        }

    private fun appBarLayoutConfigOnFragmentLaunch(fragId: Int) {

        when (fragId) {
            R.id.fragPopularMovies -> {
                screenName = getString(R.string.app_name)
                binding.appBarLayout.setExpanded(true, true)
            }
            R.id.fragMovieDetails -> {
                screenName = getString(R.string.app_name)
                binding.appBarLayout.setExpanded(false, true)
            }
        }
    }

    /*private fun appBarLayoutConfigOnSwipe() {
        binding.appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                    //  Collapsed
                    //                Log.w(TAG, "onOffsetChanged: ======Collapsed======" + verticalOffset);
                    appBarLayout.setBackgroundResource(R.color.appBarLayout_background)
                    binding.toolbar.setBackgroundResource(R.color.colorPrimary)
                    binding.toolbar.visibility = View.VISIBLE
                    binding.toolbarTitle.setText(screenName)
                    binding.toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.White))
                } else {
                    //Expanded
                    //                Log.d(TAG, "onOffsetChanged: ======Expanded======" + verticalOffset);
                    appBarLayout.setBackgroundResource(R.color.appBarLayout_background)
                    toolbar.setBackgroundResource(R.color.colorPrimary)
                    toolbarTitle.setText(R.string.app_name_translation)
                    toolbar.visibility = View.VISIBLE
                    toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.White))
                }
            }
        )
    }*/
}