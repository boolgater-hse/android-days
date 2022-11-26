package com.studyandroid.musicapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.studyandroid.musicapp.R
import com.studyandroid.musicapp.data.enums.BitDepthEnum
import com.studyandroid.musicapp.data.enums.MediaTypeEnum
import com.studyandroid.musicapp.data.enums.SampleRateEnum
import com.studyandroid.musicapp.data.model.Track
import com.studyandroid.musicapp.utils.listen
import com.studyandroid.musicapp.viewmodel.TracksViewModel

class TracksListAdapter(private val tracksViewModel: TracksViewModel) :
    RecyclerView.Adapter<TracksListAdapter.TrackViewHolder>() {

    private var tracksList = emptyList<Track>()
    private lateinit var navController: NavController

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        navController = parent.findNavController()

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.track_row, parent, false)
        return TrackViewHolder(view).listen { pos, _ ->
            val track = tracksList[pos]

            view.findNavController()
                .navigate(
                    R.id.action_tracks_fragment_to_want_to_add_to_library_dialog_fragment,
                    bundleOf("track_id" to track.trackId)
                )
        }
    }

    override fun getItemCount(): Int {
        return tracksList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracksList[position]
        holder.itemView.findViewById<TextView>(R.id.track_row_track_name).text =
            track.trackName
        holder.itemView.findViewById<TextView>(R.id.track_row_track_artist).text =
            track.artistName
        if (track.mediaType == MediaTypeEnum.MP3) {
            holder.itemView.findViewById<TextView>(R.id.track_row_track_info).text =
                MediaTypeEnum.MP3.toString()
        } else {
            holder.itemView.findViewById<TextView>(R.id.track_row_track_info).text =
                String.format(
                    holder.itemView.resources.getString(R.string.track_row_media_type_bit_depth_sample_rate),
                    track.mediaType,
                    track.bitDepth,
                    track.sampleRate.toString()
                )
        }
        holder.itemView.findViewById<ImageButton>(R.id.track_row_remove_button).setOnClickListener {
            this@TracksListAdapter.tracksViewModel.deleteTrack(track)
        }
        holder.itemView.findViewById<ImageButton>(R.id.track_row_edit_button).setOnClickListener {
            navController.navigate(R.id.action_tracks_fragment_to_update_track_fragment,
                bundleOf(
                    "track_id" to track.trackId,
                    "track_name" to track.trackName,
                    "num_of_track" to track.numOfTrack,
                    "media_type" to track.mediaType.toString(),
                    "bit_depth" to track.bitDepth,
                    "sample_rate" to track.sampleRate,
                    "album_name" to track.albumName,
                    "artist_name" to track.artistName,
                )
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(tracks: List<Track>) {
        this.tracksList = tracks
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sortByQuality(
        mediaType: MediaTypeEnum,
        bitDepth: BitDepthEnum,
        sampleRate: SampleRateEnum,
    ) {
        tracksList = tracksList.filter {
            if (mediaType == MediaTypeEnum.MP3) {
                it.mediaType == mediaType
            } else {
                it.mediaType == mediaType &&
                        it.bitDepth == bitDepth.depth &&
                        it.sampleRate == sampleRate.rate
            }
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sortByAtoZ() {
        tracksList = tracksList.sortedWith(compareBy { it.trackName })
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sortByZtoA() {
        tracksList = tracksList.sortedWith(compareByDescending { it.trackName })
        notifyDataSetChanged()
    }
}
