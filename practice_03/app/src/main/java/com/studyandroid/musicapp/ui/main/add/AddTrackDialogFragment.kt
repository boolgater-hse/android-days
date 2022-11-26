package com.studyandroid.musicapp.ui.main.add

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
import com.studyandroid.musicapp.data.enums.BitDepthEnum
import com.studyandroid.musicapp.data.enums.MediaTypeEnum
import com.studyandroid.musicapp.data.enums.SampleRateEnum
import com.studyandroid.musicapp.data.model.Track
import com.studyandroid.musicapp.utils.combineWith
import com.studyandroid.musicapp.viewmodel.AlbumsViewModel
import com.studyandroid.musicapp.viewmodel.ArtistsViewModel
import com.studyandroid.musicapp.viewmodel.TracksViewModel


class AddTrackDialogFragment : BottomSheetDialogFragment() {

    private lateinit var albumsViewModel: AlbumsViewModel
    private lateinit var artistsViewModel: ArtistsViewModel
    private lateinit var tracksViewModel: TracksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_track_dialog, container, false)

        artistsViewModel = ViewModelProvider(this)[ArtistsViewModel::class.java]
        albumsViewModel = ViewModelProvider(this)[AlbumsViewModel::class.java]
        tracksViewModel = ViewModelProvider(this)[TracksViewModel::class.java]

        val mediaTypeSpinner = view.findViewById<Spinner>(R.id.add_track_media_type_spinner)
        mediaTypeSpinner.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                MediaTypeEnum.values()
            )

        val bitDepthSpinner = view.findViewById<Spinner>(R.id.add_track_bit_depth_spinner)
        bitDepthSpinner.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                BitDepthEnum.values()
            )

        val sampleRateSpinner = view.findViewById<Spinner>(R.id.add_track_sample_rate_spinner)
        sampleRateSpinner.adapter =
            ArrayAdapter(requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                SampleRateEnum.values().map {
                    String.format("%.2f", it.rate).toDouble()
                })

        mediaTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long,
            ) {
                if (MediaTypeEnum.values()[position] == MediaTypeEnum.MP3) {
                    bitDepthSpinner.visibility = View.GONE
                    sampleRateSpinner.visibility = View.GONE
                } else {
                    bitDepthSpinner.visibility = View.VISIBLE
                    sampleRateSpinner.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        view.findViewById<Button>(R.id.add_track_add_button).setOnClickListener {
            var error = false

            val trackName = view.findViewById<EditText>(R.id.add_track_name_edittext)
            val numOfTrack = view.findViewById<EditText>(R.id.add_track_num_of_track_edittext)
            val albumName = view.findViewById<EditText>(R.id.add_track_album_name_edittext)
            val artistName = view.findViewById<EditText>(R.id.add_track_artist_name_edittext)
            val mediaType = mediaTypeSpinner.selectedItem as MediaTypeEnum
            val bitDepth = bitDepthSpinner.selectedItem as BitDepthEnum
            val sampleRate = SampleRateEnum.fromDouble(sampleRateSpinner.selectedItem as Double)!!

            if (trackName.text.toString().isBlank()) {
                trackName.error = getString(R.string.err_field_cant_be_blank)
                error = true
            }
            if (numOfTrack.text.toString().isBlank()) {
                numOfTrack.error = getString(R.string.err_field_cant_be_blank)
                error = true
            } else if (!numOfTrack.text.toString().isDigitsOnly()) {
                numOfTrack.error = getString(R.string.err_field_only_numbers)
                error = true
            }

            if (albumName.text.toString().isBlank()) {
                albumName.error = getString(R.string.err_field_cant_be_blank)
                error = true
            }
            if (artistName.text.toString().isBlank()) {
                artistName.error = getString(R.string.err_field_cant_be_blank)
                error = true
            }

            if (!error) {
                albumsViewModel.isAlbumExists(albumName.text.toString())
                artistsViewModel.isArtistExists(artistName.text.toString())
            }

            albumsViewModel.isAlbumExists.combineWith(artistsViewModel.isArtistExists)
                .observe(viewLifecycleOwner) { (albumId, artistId) ->
                    albumId?.let {
                        artistId?.let {
                            albumsViewModel.isAlbumExists.postValue(null)
                            artistsViewModel.isArtistExists.postValue(null)

                            if (artistId == 0L) {
                                artistName.error = getString(R.string.err_artist_doesnt_exist)
                                error = true
                            }
                            if (albumId == 0L) {
                                albumName.error = getString(R.string.err_album_doesnt_exist)
                                error = true
                            }
                            if (!error) {
                                val track: Track
                                if (mediaType == MediaTypeEnum.MP3) {
                                    track = Track(
                                        trackName = trackName.text.toString(),
                                        numOfTrack = numOfTrack.text.toString().toLong(),
                                        mediaType = mediaType,
                                        bitDepth = null,
                                        sampleRate = null,
                                        albumId = albumId,
                                        artistId = artistId
                                    )
                                } else {
                                    track = Track(
                                        trackName = trackName.text.toString(),
                                        numOfTrack = numOfTrack.text.toString().toLong(),
                                        mediaType = mediaType,
                                        bitDepth = bitDepth.depth,
                                        sampleRate = sampleRate.rate,
                                        albumId = albumId,
                                        artistId = artistId
                                    )
                                }
                                tracksViewModel.addTrack(track)
                                findNavController().popBackStack()
                            }
                        }
                    }
                }
        }

        return view
    }
}


