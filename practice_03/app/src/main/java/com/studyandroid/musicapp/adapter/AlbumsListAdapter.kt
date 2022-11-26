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
import com.studyandroid.musicapp.data.converter.Converters.Companion.calendar
import com.studyandroid.musicapp.data.enums.GenreEnum
import com.studyandroid.musicapp.data.model.Album
import com.studyandroid.musicapp.viewmodel.AlbumsViewModel
import com.studyandroid.musicapp.viewmodel.TracksViewModel
import java.util.*

class AlbumsListAdapter(
    private val albumsViewModel: AlbumsViewModel,
    private val tracksViewModel: TracksViewModel,
) :
    RecyclerView.Adapter<AlbumsListAdapter.AlbumViewHolder>() {

    private var albumsList = emptyList<Album>()
    private lateinit var navController: NavController

    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        navController = parent.findNavController()

        return AlbumViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.album_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return albumsList.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albumsList[position]
        holder.itemView.findViewById<TextView>(R.id.album_row_album_name).text = album.title
        holder.itemView.findViewById<TextView>(R.id.album_row_album_artist).text =
            album.artistName
        val year = if (album.releaseDate != null) {
            calendar.time = album.releaseDate
            calendar.get(Calendar.YEAR)
        } else {
            "Year undefined"
        }
        holder.itemView.findViewById<TextView>(R.id.album_row_album_release_year_num_of_tracks_genre).text =
            String.format(
                holder.itemView.resources.getString(R.string.album_row_release_year_num_of_tracks_genre),
                year,
                album.numOfTracks,
                album.genre
            )
        holder.itemView.findViewById<ImageButton>(R.id.album_row_remove_button).setOnClickListener {
            this@AlbumsListAdapter.albumsViewModel.deleteAlbum(album)
            tracksViewModel.deleteTracksByAlbumsId(album.albumId)
        }
        holder.itemView.findViewById<ImageButton>(R.id.album_row_edit_button).setOnClickListener {
            navController.navigate(R.id.action_albums_fragment_to_update_album_fragment,
                bundleOf(
                    "album_id" to album.albumId,
                    "title" to album.title,
                    "genre" to album.genre.toString(),
                    "num_of_tracks" to album.numOfTracks,
                    "release_date" to album.releaseDate?.time,
                    "artist_name" to album.artistName,
                )
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(albums: List<Album>) {
        this.albumsList = albums
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sortByGenre(genre: GenreEnum) {
        albumsList = albumsList.filter {
            it.genre == genre
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sortByAtoZ() {
        albumsList = albumsList.sortedWith(compareBy { it.title })
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sortByZtoA() {
        albumsList = albumsList.sortedWith(compareByDescending { it.title })
        notifyDataSetChanged()
    }
}
