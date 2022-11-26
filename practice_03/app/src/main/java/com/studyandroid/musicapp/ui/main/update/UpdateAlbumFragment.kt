package com.studyandroid.musicapp.ui.main.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.studyandroid.musicapp.R
import com.studyandroid.musicapp.data.converter.Converters
import com.studyandroid.musicapp.data.converter.Converters.Companion.formatter
import com.studyandroid.musicapp.data.enums.GenreEnum
import com.studyandroid.musicapp.data.model.Album
import com.studyandroid.musicapp.viewmodel.AlbumsViewModel
import com.studyandroid.musicapp.viewmodel.ArtistsViewModel
import com.studyandroid.musicapp.viewmodel.TracksViewModel
import java.util.*

class UpdateAlbumFragment : BottomSheetDialogFragment() {

    private lateinit var albumsViewModel: AlbumsViewModel
    private lateinit var artistsViewModel: ArtistsViewModel
    private lateinit var tracksViewModel: TracksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_album, container, false)

        albumsViewModel = ViewModelProvider(this)[AlbumsViewModel::class.java]
        artistsViewModel = ViewModelProvider(this)[ArtistsViewModel::class.java]
        tracksViewModel = ViewModelProvider(this)[TracksViewModel::class.java]

        val albumId = arguments?.getLong("album_id")!!
        val title = arguments?.getString("title")!!
        val genre = arguments?.getString("genre")!!
        val numOfTracks = arguments?.getLong("num_of_tracks")!!
        val releaseDate = arguments?.getLong("release_date")!!
        val artistName = arguments?.getString("artist_name")!!

        Converters.calendar.timeInMillis = releaseDate
        val releaseDateString =
            "${Converters.calendar.get(Calendar.DATE)}/${Converters.calendar.get(Calendar.MONTH) + 1}/${
                Converters.calendar.get(Calendar.YEAR)
            }"

        val titleEditText = view.findViewById<EditText>(R.id.update_album_title_edittext)
        titleEditText.setText(title, TextView.BufferType.EDITABLE)

        val numOfTracksEditText =
            view.findViewById<EditText>(R.id.update_album_num_of_tracks_edittext)
        numOfTracksEditText.setText(numOfTracks.toString(), TextView.BufferType.EDITABLE)

        val releaseDateEditText =
            view.findViewById<EditText>(R.id.update_album_release_date_edittext)
        releaseDateEditText.setText(releaseDateString, TextView.BufferType.EDITABLE)

        val genreSpinner = view.findViewById<Spinner>(R.id.update_album_genre_spinner)
        genreSpinner.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                GenreEnum.values()
            )
        genreSpinner.setSelection(GenreEnum.values().map { it.toString() }.toMutableList()
            .indexOf(genre))

        val artistNameEditText = view.findViewById<EditText>(R.id.update_album_artist_name_edittext)
        artistNameEditText.setText(artistName, TextView.BufferType.EDITABLE)

        view.findViewById<Button>(R.id.update_album_update_button).setOnClickListener {
            var error = false

            if (titleEditText.toString().isBlank()) {
                titleEditText.error = getString(R.string.err_field_cant_be_blank)
                error = true
            }
            if (numOfTracksEditText.text.toString().isBlank()) {
                numOfTracksEditText.error = getString(R.string.err_field_cant_be_blank)
                error = true
            } else {
                if (!numOfTracksEditText.text.toString().isDigitsOnly()) {
                    numOfTracksEditText.error = getString(R.string.err_field_only_numbers)
                    error = true
                }
            }
            if (releaseDateEditText.text.toString().isBlank()) {
                releaseDateEditText.error = getString(R.string.err_field_cant_be_blank)
                error = true
            } else {
                if (!releaseDateEditText.text.toString()
                        .matches("^(3[01]|[12][0-9]|0?[1-9])/(1[012]|0?[1-9])/\\d{4}\$".toRegex())
                ) {
                    releaseDateEditText.error = getString(R.string.err_use_format)
                    error = true
                }
            }
            if (artistNameEditText.text.toString().isBlank()) {
                artistNameEditText.error = getString(R.string.err_field_cant_be_blank)
                error = true
            }

            artistsViewModel.isArtistExists.observe(viewLifecycleOwner) {
                it?.let {
                    artistsViewModel.isArtistExists.postValue(null)
                    if (it == 0L) {
                        artistNameEditText.error = getString(R.string.err_artist_doesnt_exist)
                        error = true
                    } else {
                        val album = Album(
                            albumId = albumId,
                            title = titleEditText.text.toString(),
                            genre = genreSpinner.selectedItem as GenreEnum,
                            numOfTracks = numOfTracksEditText.text.toString().toLong(),
                            releaseDate = formatter.parse(releaseDateEditText.text.toString()),
                            artistId = it
                        )
                        albumsViewModel.updateAlbum(album)
                        tracksViewModel.updateArtistIdWhereAlbumId(it, albumId)
                        findNavController().popBackStack()
                    }
                }
            }

            if (!error) {
                artistsViewModel.isArtistExists(artistNameEditText.text.toString())
            }
        }

        return view
    }
}
