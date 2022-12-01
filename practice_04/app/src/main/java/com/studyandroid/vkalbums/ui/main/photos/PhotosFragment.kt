package com.studyandroid.vkalbums.ui.main.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.studyandroid.vkalbums.R
import com.studyandroid.vkalbums.databinding.FragmentPhotosBinding
import com.studyandroid.vkalbums.viewmodel.MainViewModel

class PhotosFragment : Fragment() {

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    private var currentUser: Long = 0L
    private var currentAlbum: String = ""

    private var currentOffset = 0
    private var offsetStep = 100
    private val defaultCount = 100
    private var numberOfElements = -1L

    var loading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        val view = binding.root

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val adapter = PhotoListAdapter()
        binding.photosRecyclerView.adapter = adapter
        binding.photosRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        mainViewModel.getAlbumPhotosListResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                numberOfElements = (response.body()?.data?.count) ?: -1
                val photos = response.body()?.data?.items
                if (numberOfElements != -1L && photos != null) {
                    photos.forEach { photo ->
                        val yTypeImage = photo.sizes.find { it.type == "y" }
                        if (yTypeImage != null) {
                            photo.photoUrl = yTypeImage.url
                        } else {
                            val maxResolutionImage = photo.sizes.maxWith { left, right ->
                                return@maxWith compareValues(
                                    left.height * left.width,
                                    right.height * right.width
                                )
                            }
                            photo.photoUrl = maxResolutionImage.url
                        }
                    }
                    if (adapter.itemCount == 0) {
                        adapter.addData(photos)
                    } else {
                        adapter.expandData(photos)
                        loading = false
                    }
                } else {
                    Toast.makeText(activity, getString(R.string.unknown_error), Toast.LENGTH_SHORT)
                        .show()
                }
            } else if (response.code() == 997) {
                Toast.makeText(activity,
                    getString(R.string.internet_conn_issues),
                    Toast.LENGTH_SHORT).show()
                loading = false
            }
        }

        currentUser = arguments?.getLong("user_id") ?: -1
        currentAlbum = arguments?.getString("album_id") ?: ""
        if (currentUser != -1L && currentAlbum.isNotBlank()) {
            mainViewModel.getAlbumPhotosList(
                currentUser,
                currentAlbum,
                currentOffset,
                defaultCount
            )
        } else {
            Toast.makeText(activity, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
        }

        binding.photosRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!loading &&
                    currentOffset < numberOfElements &&
                    !recyclerView.canScrollVertically(1)
                ) {
                    loading = true

                    if (currentOffset + offsetStep < numberOfElements) {
                        currentOffset += offsetStep
                    } else {
                        currentOffset = numberOfElements.toInt()
                    }

                    mainViewModel.getAlbumPhotosList(
                        currentUser,
                        currentAlbum,
                        currentOffset,
                        defaultCount
                    )
                }
            }
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        Glide.get(requireContext()).clearMemory()

        _binding = null
    }
}
