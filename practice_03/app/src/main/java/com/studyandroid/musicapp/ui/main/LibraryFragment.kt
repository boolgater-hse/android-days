package com.studyandroid.musicapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.studyandroid.musicapp.R
import com.studyandroid.musicapp.adapter.LibraryListAdapter
import com.studyandroid.musicapp.ui.welcome.LoginActivity
import com.studyandroid.musicapp.viewmodel.LibraryViewModel

class LibraryFragment : Fragment() {

    private lateinit var libraryViewModel: LibraryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        libraryViewModel = ViewModelProvider(this)[LibraryViewModel::class.java]

        val adapter = LibraryListAdapter(libraryViewModel)
        val recyclerView = view.findViewById<RecyclerView>(R.id.library_fragment_recyclerview)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        libraryViewModel.usersLibrary.observe(viewLifecycleOwner) { tracks ->
            adapter.setData(tracks)
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.library_toolbar, menu)
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
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return view
    }
}
