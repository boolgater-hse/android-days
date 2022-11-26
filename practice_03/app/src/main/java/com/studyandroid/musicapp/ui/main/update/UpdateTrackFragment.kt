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
import com.studyandroid.musicapp.data.enums.BitDepthEnum
import com.studyandroid.musicapp.data.enums.MediaTypeEnum
import com.studyandroid.musicapp.data.enums.SampleRateEnum
import com.studyandroid.musicapp.data.model.Track
import com.studyandroid.musicapp.utils.combineWith
import com.studyandroid.musicapp.viewmodel.AlbumsViewModel
import com.studyandroid.musicapp.viewmodel.ArtistsViewModel
import com.studyandroid.musicapp.viewmodel.TracksViewModel

class UpdateTrackFragment : BottomSheetDialogFragment() {

    private lateinit var albumsViewModel: AlbumsViewModel
    private lateinit var artistsViewModel: ArtistsViewModel
    private lateinit var tracksViewModel: TracksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_track, container, false)

        albumsViewModel = ViewModelProvider(this)[AlbumsViewModel::class.java]
        artistsViewModel = ViewModelProvider(this)[ArtistsViewModel::class.java]
        tracksViewModel = ViewModelProvider(this)[TracksViewModel::class.java]

        val trackId = arguments?.getLong("track_id")!!
        val trackName = arguments?.getString("track_name")!!
        val numOfTrack = arguments?.getLong("num_of_track")!!
        val mediaType = arguments?.getString("media_type")!!
        val bitDepth = arguments?.getInt("bit_depth")
        val sampleRate = arguments?.getDouble("sample_rate")
        val artistName = arguments?.getString("artist_name")!!
        val albumName = arguments?.getString("album_name")

        val trackNameEditText = view.findViewById<EditText>(R.id.update_track_name_edittext)
        trackNameEditText.setText(trackName, TextView.BufferType.EDITABLE)

        val numOfTrackEditText =
            view.findViewById<EditText>(R.id.update_track_num_of_track_edittext)
        numOfTrackEditText.setText(numOfTrack.toString(), TextView.BufferType.EDITABLE)

        val albumNameEditText = view.findViewById<EditText>(R.id.update_track_album_name_edittext)
        albumNameEditText.setText(albumName, TextView.BufferType.EDITABLE)

        val artistNameEditText = view.findViewById<EditText>(R.id.update_track_artist_name_edittext)
        artistNameEditText.setText(artistName, TextView.BufferType.EDITABLE)

        val mediaTypeSpinner = view.findViewById<Spinner>(R.id.update_track_media_type_spinner)
        mediaTypeSpinner.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                MediaTypeEnum.values()
            )
        mediaTypeSpinner.setSelection(MediaTypeEnum.values().map { it.toString() }.toMutableList()
            .indexOf(mediaType))


        val bitDepthSpinner = view.findViewById<Spinner>(R.id.update_track_bit_depth_spinner)
        bitDepthSpinner.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                BitDepthEnum.values()
            )
        bitDepth?.let {
            bitDepthSpinner.setSelection(BitDepthEnum.values().map { i -> i.depth }.toMutableList()
                .indexOf(it))
        }

        val sampleRateSpinner = view.findViewById<Spinner>(R.id.update_track_sample_rate_spinner)
        sampleRateSpinner.adapter =
            ArrayAdapter(requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                SampleRateEnum.values().map {
                    String.format("%.2f", it.rate).toDouble()
                })
        sampleRate?.let {
            sampleRateSpinner.setSelection(SampleRateEnum.values().map { i -> i.rate }
                .toMutableList().indexOf(it))
        }

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

        view.findViewById<Button>(R.id.update_track_add_button).setOnClickListener {
            var error = false

            if (trackNameEditText.text.toString().isBlank()) {
                trackNameEditText.error = getString(R.string.err_field_cant_be_blank)
                error = true
            }
            if (numOfTrackEditText.text.toString().isBlank()) {
                numOfTrackEditText.error = getString(R.string.err_field_cant_be_blank)
                error = true
            } else if (!numOfTrackEditText.text.toString().isDigitsOnly()) {
                numOfTrackEditText.error = getString(R.string.err_field_only_numbers)
                error = true
            }
            if (albumNameEditText.text.toString().isBlank()) {
                albumNameEditText.error = getString(R.string.err_field_cant_be_blank)
                error = true
            }
            if (artistNameEditText.text.toString().isBlank()) {
                artistNameEditText.error = getString(R.string.err_field_cant_be_blank)
                error = true
            }

            if (!error) {
                albumsViewModel.isAlbumExists(albumNameEditText.text.toString())
                artistsViewModel.isArtistExists(artistNameEditText.text.toString())
            }

            albumsViewModel.isAlbumExists.combineWith(artistsViewModel.isArtistExists)
                .observe(viewLifecycleOwner) { (alId, arId) ->
                    alId?.let {
                        arId?.let {
                            albumsViewModel.isAlbumExists.postValue(null)
                            artistsViewModel.isArtistExists.postValue(null)

                            if (arId == 0L) {
                                artistNameEditText.error =
                                    getString(R.string.err_artist_doesnt_exist)
                                error = true
                            }
                            if (alId == 0L) {
                                albumNameEditText.error = getString(R.string.err_album_doesnt_exist)
                                error = true
                            }
                            if (!error) {
                                albumsViewModel.getAlbumNameById.combineWith(artistsViewModel.getArtistsNameById)
                                    .observe(viewLifecycleOwner) { (alName, arName) ->
                                        alName?.let {
                                            arName?.let {
                                                albumsViewModel.getAlbumNameById.postValue(null)
                                                artistsViewModel.getArtistsNameById.postValue(null)

                                                val track: Track
                                                if ((mediaTypeSpinner.selectedItem as MediaTypeEnum) == MediaTypeEnum.MP3) {
                                                    track = Track(
                                                        trackId = trackId,
                                                        trackName = trackNameEditText.text.toString(),
                                                        numOfTrack = numOfTrackEditText.text.toString()
                                                            .toLong(),
                                                        mediaType = mediaTypeSpinner.selectedItem as MediaTypeEnum,
                                                        bitDepth = null,
                                                        sampleRate = null,
                                                        albumId = alId,
                                                        artistId = arId
                                                    )
                                                    track.albumName = alName
                                                    track.artistName = arName
                                                } else {
                                                    track = Track(
                                                        trackId = trackId,
                                                        trackName = trackNameEditText.text.toString(),
                                                        numOfTrack = numOfTrackEditText.text.toString()
                                                            .toLong(),
                                                        mediaType = mediaTypeSpinner.selectedItem as MediaTypeEnum,
                                                        bitDepth = (bitDepthSpinner.selectedItem as BitDepthEnum).depth,
                                                        sampleRate = SampleRateEnum.fromDouble(
                                                            sampleRateSpinner.selectedItem.toString()
                                                                .toDouble())?.rate,
                                                        albumId = alId,
                                                        artistId = arId
                                                    )
                                                    track.albumName = alName
                                                    track.artistName = arName
                                                }
                                                tracksViewModel.updateTrack(track)
                                                findNavController().popBackStack()
                                            }
                                        }
                                    }

                                artistsViewModel.getArtistsNameById(arId)
                                albumsViewModel.getAlbumNameById(alId)
                            }
                        }
                    }
                }
        }

        return view
    }
}
