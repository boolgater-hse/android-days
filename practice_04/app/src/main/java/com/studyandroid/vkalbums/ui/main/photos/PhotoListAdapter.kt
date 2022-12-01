package com.studyandroid.vkalbums.ui.main.photos

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.studyandroid.vkalbums.data.remote.model.Photo
import com.studyandroid.vkalbums.databinding.PhotoCellBinding
import java.util.*

class PhotoListAdapter : RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {

    private var photosList = mutableListOf<Photo>()

    class PhotoViewHolder(private val binding: PhotoCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            Glide.with(binding.root)
                .load(photo.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.photoImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = PhotoCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PhotoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return photosList.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photosList[position]
        holder.bind(photo)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(photos: List<Photo>) {
        photosList.addAll(photos)
        notifyDataSetChanged()
    }

    fun expandData(photos: List<Photo>) {
        val scrollPos = photosList.size - 1
        photosList.addAll(photos)
        notifyItemRemoved(scrollPos)
    }
}
