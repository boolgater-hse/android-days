package com.studyandroid.musicapp.ui.main.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.studyandroid.musicapp.R
import com.studyandroid.musicapp.data.enums.GenreEnum

class GenreSortFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_genre_sort, container, false)

        val genreSpinner = view.findViewById<Spinner>(R.id.main_sort_genre_genre)
        genreSpinner.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                GenreEnum.values()
            )

        view.findViewById<Button>(R.id.main_sort_genre_sort_button).setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set("result",
                genreSpinner.selectedItem as GenreEnum)
            findNavController().popBackStack()
        }

        return view
    }
}
