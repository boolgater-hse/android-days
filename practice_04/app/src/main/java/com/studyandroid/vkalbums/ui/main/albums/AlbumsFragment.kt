package com.studyandroid.vkalbums.ui.main.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.studyandroid.vkalbums.R
import com.studyandroid.vkalbums.databinding.FragmentAlbumsBinding
import com.studyandroid.vkalbums.viewmodel.MainViewModel

class AlbumsFragment : Fragment() {

    private var _binding: FragmentAlbumsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAlbumsBinding.inflate(inflater, container, false)
        val view = binding.root

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val adapter = AlbumsListAdapter()
        binding.albumsRecyclerView.adapter = adapter
        binding.albumsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        mainViewModel.getUsersAlbumsListResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                val albums = response.body()?.data?.items
                if (albums != null) {
                    albums.forEach { album ->
                        val yTypeImage = album.thumbSizes.find { it.type == "y" }
                        if (yTypeImage != null) {
                            album.thumbUrl = yTypeImage.url
                        } else {
                            val maxResolutionImage = album.thumbSizes.maxWith { left, right ->
                                return@maxWith compareValues(
                                    left.height * left.width,
                                    right.height * right.width
                                )
                            }
                            album.thumbUrl = maxResolutionImage.url
                        }
                    }
                    if (albums.isNotEmpty()) {
                        adapter.setData(albums)
                    } else {
                        binding.albumsUserNoAlbums.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(activity, getString(R.string.unknown_error), Toast.LENGTH_SHORT)
                        .show()
                }
            } else if (response.code() == 997) {
                Toast.makeText(activity,
                    getString(R.string.internet_conn_issues),
                    Toast.LENGTH_SHORT).show()
            }
        }

        val currentUser = arguments?.getLong("user_id")
        if (currentUser != null) {
            mainViewModel.getUsersAlbumsList(currentUser)
        } else {
            Toast.makeText(activity, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
