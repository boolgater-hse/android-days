package com.studyandroid.musicapp.ui.main.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.studyandroid.musicapp.R
import com.studyandroid.musicapp.data.model.Artist
import com.studyandroid.musicapp.viewmodel.ArtistsViewModel

class UpdateArtistFragment : BottomSheetDialogFragment() {

    private lateinit var artistsViewModel: ArtistsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_artist_dialog, container, false)

        artistsViewModel = ViewModelProvider(this)[ArtistsViewModel::class.java]

        val artistId = arguments?.getLong("artist_id")!!
        val name = arguments?.getString("name")!!

        val nameEdittext = view.findViewById<EditText>(R.id.update_artist_name_edittext)
        nameEdittext.setText(name, TextView.BufferType.EDITABLE)
        view.findViewById<Button>(R.id.update_artist_add_button).setOnClickListener {
            var error = false

            if (nameEdittext.text.toString().isBlank()) {
                nameEdittext.error = getString(R.string.err_field_cant_be_blank)
                error = true
            }

            if (!error) {
                if (nameEdittext.text.toString() != name) {
                    artistsViewModel.isArtistExists.observe(viewLifecycleOwner) {
                        it?.let {
                            if (it == 0L) {
                                artistsViewModel.updateArtist(
                                    Artist(
                                        artistId = artistId,
                                        name = nameEdittext.text.toString()
                                    )
                                )
                                findNavController().popBackStack()
                            } else {
                                Toast.makeText(
                                    activity,
                                    getString(R.string.err_artist_already_exists),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    artistsViewModel.isArtistExists(nameEdittext.text.toString())
                } else {
                    findNavController().popBackStack()
                }
            }
        }

        return view
    }
}
