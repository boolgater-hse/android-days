package com.studyandroid.vkalbums.ui.main.albums

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory
import com.studyandroid.vkalbums.R
import com.studyandroid.vkalbums.data.remote.model.Album
import com.studyandroid.vkalbums.databinding.AlbumRowBinding

class AlbumsListAdapter :
    RecyclerView.Adapter<AlbumsListAdapter.AlbumViewHolder>() {

    private var albumsList = emptyList<Album>()

    class AlbumViewHolder(private val binding: AlbumRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.albumsRowAlbumName.text = album.title
            binding.albumsRowNumOfPhotos.text = when (album.size) {
                0L -> itemView.context.getString(R.string.no_photos)
                1L -> String.format(itemView.context.getString(R.string.one_photo), album.size)
                else -> String.format(itemView.context.getString(R.string.many_photos), album.size)
            }

            Glide
                .get(binding.root.context)
                .setMemoryCategory(MemoryCategory.LOW)
            Glide.with(binding.root)
                .load(album.thumbUrl)
                .into(binding.albumsRowBackImage)

            binding.albumRowCardView.setOnClickListener {
                if (album.size == 0L) {
                    Toast.makeText(
                        itemView.context,
                        itemView.context.getString(R.string.album_no_photos),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val currentAlbum = when (album.title) {
                    "Фотографии с моей страницы" -> "profile"
                    "Сохранённые фотографии" -> "saved"
                    else -> album.id.toString()
                }
                itemView.findNavController()
                    .navigate(
                        R.id.action_albums_fragment_to_photos_fragment, bundleOf(
                            "album_id" to currentAlbum,
                            "user_id" to album.ownerId
                        )
                    )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = AlbumRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AlbumViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return albumsList.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albumsList[position]
        holder.bind(album)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(albums: List<Album>) {
        this.albumsList = albums
        notifyDataSetChanged()
    }
}
