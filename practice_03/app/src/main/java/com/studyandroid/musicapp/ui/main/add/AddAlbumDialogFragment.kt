package com.studyandroid.musicapp.ui.main.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.studyandroid.musicapp.R
import com.studyandroid.musicapp.data.converter.Converters.Companion.formatter
import com.studyandroid.musicapp.data.enums.GenreEnum
import com.studyandroid.musicapp.data.model.Album
import com.studyandroid.musicapp.viewmodel.AlbumsViewModel
import com.studyandroid.musicapp.viewmodel.ArtistsViewModel

class AddAlbumDialogFragment : BottomSheetDialogFragment() {

    private lateinit var artistsViewModel: ArtistsViewModel
    private lateinit var albumsViewModel: AlbumsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_album_dialog, container, false)

        artistsViewModel = ViewModelProvider(this)[ArtistsViewModel::class.java]
        albumsViewModel = ViewModelProvider(this)[AlbumsViewModel::class.java]

        val genreSpinner = view.findViewById<Spinner>(R.id.add_album_genre_spinner)
        genreSpinner.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                GenreEnum.values()
            )

        view.findViewById<Button>(R.id.add_album_add_button).setOnClickListener {
            var error = false

            val title = view.findViewById<EditText>(R.id.add_album_title_edittext)
            val numOfTracks = view.findViewById<EditText>(R.id.add_album_num_of_tracks_edittext)
            val releaseDate = view.findViewById<EditText>(R.id.add_album_release_date_edittext)
            val artistName = view.findViewById<EditText>(R.id.add_album_artist_name_edittext)
            val genre = genreSpinner.selectedItem as GenreEnum

            if (title.text.toString().isBlank()) {
                title.error = getString(R.string.err_field_cant_be_blank)
                error = true
            }
            if (numOfTracks.text.toString().isBlank()) {
                numOfTracks.error = getString(R.string.err_field_cant_be_blank)
                error = true
            } else {
                if (!numOfTracks.text.toString().isDigitsOnly()) {
                    numOfTracks.error = getString(R.string.err_field_only_numbers)
                    error = true
                }
            }
            if (releaseDate.text.toString().isBlank()) {
                releaseDate.error = getString(R.string.err_field_cant_be_blank)
                error = true
            } else {
                if (!releaseDate.text.toString()
                        .matches("^(3[01]|[12][0-9]|0?[1-9])/(1[012]|0?[1-9])/\\d{4}\$".toRegex())
                ) {
                    releaseDate.error = getString(R.string.err_use_format)
                    error = true
                }
            }
            if (artistName.text.toString().isBlank()) {
                artistName.error = getString(R.string.err_field_cant_be_blank)
                error = true
            }

            artistsViewModel.isArtistExists.observe(viewLifecycleOwner) {
                it?.let {
                    artistsViewModel.isArtistExists.postValue(null)
                    if (it == 0L) {
                        artistName.error = getString(R.string.err_artist_doesnt_exist)
                        error = true
                    } else if (!error) {
                        val album = Album(
                            title = title.text.toString(),
                            genre = genre,
                            numOfTracks = numOfTracks.text.toString().toLong(),
                            releaseDate = formatter.parse(releaseDate.text.toString()),
                            artistId = it
                        )
                        albumsViewModel.addAlbum(album)
                        findNavController().popBackStack()
                    }
                }
            }
            if (artistName.text.toString().isBlank()) {
                artistName.error = getString(R.string.err_field_cant_be_blank)
            } else {
                artistsViewModel.isArtistExists(artistName.text.toString())
            }
        }

        return view
    }
}
