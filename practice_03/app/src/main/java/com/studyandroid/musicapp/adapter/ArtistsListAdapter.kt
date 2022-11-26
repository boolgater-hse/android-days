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
import com.studyandroid.musicapp.data.model.Artist
import com.studyandroid.musicapp.viewmodel.AlbumsViewModel
import com.studyandroid.musicapp.viewmodel.ArtistsViewModel
import com.studyandroid.musicapp.viewmodel.TracksViewModel

class ArtistsListAdapter(
    private val artistsViewModel: ArtistsViewModel,
    private val albumsViewMode: AlbumsViewModel,
    private val tracksViewModel: TracksViewModel,
) :
    RecyclerView.Adapter<ArtistsListAdapter.ArtistViewHolder>() {

    private var artistsList = emptyList<Artist>()
    private lateinit var navController: NavController

    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        navController = parent.findNavController()

        return ArtistViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.artist_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return artistsList.size
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artistsList[position]
        holder.itemView.findViewById<TextView>(R.id.artist_row_artist_name).text = artist.name
        holder.itemView.findViewById<ImageButton>(R.id.artist_row_remove_button)
            .setOnClickListener {
                this@ArtistsListAdapter.artistsViewModel.deleteArtist(artist)
                this@ArtistsListAdapter.albumsViewMode.deleteAlbumsByArtistId(artist.artistId)
                this@ArtistsListAdapter.tracksViewModel.deleteTracksByArtistsId(artist.artistId)
            }
        holder.itemView.findViewById<ImageButton>(R.id.artist_row_edit_button).setOnClickListener {
            navController.navigate(
                R.id.action_artists_fragment_to_update_artist_dialog,
                bundleOf(
                    "artist_id" to artist.artistId,
                    "name" to artist.name
                )
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(artists: List<Artist>) {
        this.artistsList = artists
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sortByAtoZ() {
        artistsList = artistsList.sortedWith(compareBy { it.name })
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sortByZtoA() {
        artistsList = artistsList.sortedWith(compareByDescending { it.name })
        notifyDataSetChanged()
    }
}
