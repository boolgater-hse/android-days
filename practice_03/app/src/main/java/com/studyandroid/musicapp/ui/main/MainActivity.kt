package com.studyandroid.musicapp.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.studyandroid.musicapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.main_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val tracksListener = View.OnClickListener {
            findNavController(R.id.main_fragmentcontainerview)
                .navigate(R.id.action_tracks_fragment_to_add_track_dialog_fragment)
        }
        val albumsListener = View.OnClickListener {
            findNavController(R.id.main_fragmentcontainerview)
                .navigate(R.id.action_albums_fragment_to_add_album_dialog_fragment)
        }
        val artistsListener = View.OnClickListener {
            findNavController(R.id.main_fragmentcontainerview)
                .navigate(R.id.action_artists_fragment_to_add_artist_dialog_fragment)
        }
        val libraryListener = View.OnClickListener {
            Toast.makeText(this, getString(R.string.library_tap_on_track), Toast.LENGTH_SHORT)
                .show()
        }

        val addButton = findViewById<FloatingActionButton>(R.id.main_add_button)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragmentcontainerview) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.main_bottom_nav).setupWithNavController(
            navController
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.tracks_fragment -> {
                    addButton.setOnClickListener(tracksListener)
                }
                R.id.albums_fragment -> {
                    addButton.setOnClickListener(albumsListener)
                }
                R.id.artists_fragment -> {
                    addButton.setOnClickListener(artistsListener)
                }
                R.id.library_fragment -> {
                    addButton.setOnClickListener(libraryListener)
                }
            }
        }
    }
}
