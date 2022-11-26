package com.studyandroid.musicapp.ui.main.add

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.studyandroid.musicapp.R
import com.studyandroid.musicapp.data.model.Artist
import com.studyandroid.musicapp.viewmodel.ArtistsViewModel

class AddArtistDialogFragment : BottomSheetDialogFragment() {

    private lateinit var artistsViewModel: ArtistsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_artist_dialog, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        artistsViewModel = ViewModelProvider(this)[ArtistsViewModel::class.java]

        view.findViewById<Button>(R.id.add_artist_add_button).setOnClickListener {
            val name = view.findViewById<EditText>(R.id.add_artist_name_edittext)
            if (name.text.toString().isNotBlank()) {
                val artist = Artist(name = name.text.toString())

                artistsViewModel.addArtist(artist)
                findNavController().popBackStack()
            } else {
                name.error = getString(R.string.err_field_cant_be_blank)
            }
        }

        return view
    }
}
