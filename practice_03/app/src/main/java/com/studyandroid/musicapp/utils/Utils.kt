package com.studyandroid.musicapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.recyclerview.widget.RecyclerView

fun <T, S> LiveData<T?>.combineWith(other: LiveData<S?>): LiveData<Pair<T?, S?>> =
    MediatorLiveData<Pair<T?, S?>>().apply {
        addSource(this@combineWith) { value = Pair(it, other.value) }
        addSource(other) { value = Pair(this@combineWith.value, it) }
    }

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition, itemViewType)
    }
    return this
}
