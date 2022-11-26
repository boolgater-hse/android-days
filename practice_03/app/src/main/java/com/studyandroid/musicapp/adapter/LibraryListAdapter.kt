package com.studyandroid.musicapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.studyandroid.musicapp.R
import com.studyandroid.musicapp.data.enums.MediaTypeEnum
import com.studyandroid.musicapp.data.model.Track
import com.studyandroid.musicapp.viewmodel.LibraryViewModel

class LibraryListAdapter(private val libraryViewModel: LibraryViewModel) :
    RecyclerView.Adapter<LibraryListAdapter.LibraryViewHolder>() {

    private var libraryTrackList = emptyList<Track>()

    class LibraryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        return LibraryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.track_library_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return libraryTrackList.size
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val track = libraryTrackList[position]
        holder.itemView.findViewById<TextView>(R.id.track_library_row_track_name).text =
            track.trackName
        holder.itemView.findViewById<TextView>(R.id.track_library_row_track_artist).text =
            track.artistName
        if (track.mediaType == MediaTypeEnum.MP3) {
            holder.itemView.findViewById<TextView>(R.id.track_library_row_track_info).text =
                MediaTypeEnum.MP3.toString()
        } else {
            holder.itemView.findViewById<TextView>(R.id.track_library_row_track_info).text =
                String.format(
                    holder.itemView.resources.getString(R.string.track_row_media_type_bit_depth_sample_rate),
                    track.mediaType,
                    track.bitDepth,
                    track.sampleRate.toString()
                )
        }
        holder.itemView.findViewById<ImageButton>(R.id.track_library_row_remove_button)
            .setOnClickListener {
                this@LibraryListAdapter.libraryViewModel.deleteFromTrackLibrary(
                    libraryViewModel.currentUser,
                    track.trackId
                )
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(tracks: List<Track>) {
        this.libraryTrackList = tracks
        notifyDataSetChanged()
    }
}
