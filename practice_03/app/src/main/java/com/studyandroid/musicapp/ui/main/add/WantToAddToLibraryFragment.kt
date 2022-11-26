package com.studyandroid.musicapp.ui.main.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.studyandroid.musicapp.R
import com.studyandroid.musicapp.data.model.Library
import com.studyandroid.musicapp.viewmodel.LibraryViewModel
import com.studyandroid.musicapp.viewmodel.UsersViewModel

class WantToAddToLibraryFragment : BottomSheetDialogFragment() {

    private lateinit var libraryViewModel: LibraryViewModel
    private lateinit var usersViewModel: UsersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_want_to_add_to_library, container, false)

        val trackId = arguments?.getLong("track_id")!!

        libraryViewModel = ViewModelProvider(this)[LibraryViewModel::class.java]
        usersViewModel = ViewModelProvider(this)[UsersViewModel::class.java]

        view.findViewById<Button>(R.id.add_track_library_yes)?.setOnClickListener {
            usersViewModel.getUsersId.observe(viewLifecycleOwner) {
                it?.let {
                    usersViewModel.getUsersId.postValue(null)
                    libraryViewModel.addLibrary(Library(libraryOwnerId = it, trackId = trackId))
                }
            }
            usersViewModel.getUsersId(libraryViewModel.currentUser)
        }

        view.findViewById<Button>(R.id.add_track_library_no)?.setOnClickListener {
            findNavController().popBackStack()
        }

        libraryViewModel.libraryInsertionStatus.observe(viewLifecycleOwner) {
            it?.let {
                libraryViewModel.libraryInsertionStatus.postValue(null)
                if (it) {
                    Toast.makeText(
                        activity,
                        getString(R.string.track_successfully_added),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.err_track_already_in_library),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                findNavController().popBackStack()
            }
        }

        return view
    }
}
