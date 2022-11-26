package com.studyandroid.musicapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.studyandroid.musicapp.R
import com.studyandroid.musicapp.adapter.ArtistsListAdapter
import com.studyandroid.musicapp.ui.welcome.LoginActivity
import com.studyandroid.musicapp.viewmodel.AlbumsViewModel
import com.studyandroid.musicapp.viewmodel.ArtistsViewModel
import com.studyandroid.musicapp.viewmodel.TracksViewModel

class ArtistsFragment : Fragment() {

    private lateinit var artistsViewModel: ArtistsViewModel
    private lateinit var albumsViewModel: AlbumsViewModel
    private lateinit var tracksViewModel: TracksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_artists, container, false)

        artistsViewModel = ViewModelProvider(this)[ArtistsViewModel::class.java]
        albumsViewModel = ViewModelProvider(this)[AlbumsViewModel::class.java]
        tracksViewModel = ViewModelProvider(this)[TracksViewModel::class.java]

        val adapter = ArtistsListAdapter(artistsViewModel, albumsViewModel, tracksViewModel)
        val recyclerView = view.findViewById<RecyclerView>(R.id.artists_fragment_recyclerview)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        artistsViewModel.getAllArtists.observe(viewLifecycleOwner) { artists ->
            adapter.setData(artists)
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.artists_toolbar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.toolbar_logout -> {
                        val sharedPref = activity?.getSharedPreferences(
                            getString(R.string.pref_current_user), Context.MODE_PRIVATE
                        )
                        with(sharedPref?.edit()) {
                            this?.putString(getString(R.string.pref_current_user), "")
                            this?.apply()
                        }
                        val intent = Intent(activity, LoginActivity::class.java)
                        startActivity(intent)
                        activity?.finishAffinity()
                        true
                    }
                    R.id.artists_sort_by_alphabet_az -> {
                        adapter.sortByAtoZ()
                        Toast.makeText(activity,
                            getString(R.string.sorted_by_A_to_Z),
                            Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.artists_sort_by_alphabet_za -> {
                        adapter.sortByZtoA()
                        Toast.makeText(activity,
                            getString(R.string.sorted_by_Z_to_A),
                            Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.artists_sort_by_default -> {
                        val id = findNavController().currentDestination?.id
                        findNavController().popBackStack(id!!, true)
                        findNavController().navigate(id)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return view
    }
}
