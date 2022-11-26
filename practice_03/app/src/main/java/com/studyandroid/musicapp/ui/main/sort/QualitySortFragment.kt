package com.studyandroid.musicapp.ui.main.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.studyandroid.musicapp.R
import com.studyandroid.musicapp.data.enums.BitDepthEnum
import com.studyandroid.musicapp.data.enums.MediaTypeEnum
import com.studyandroid.musicapp.data.enums.SampleRateEnum

class QualitySortFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quality_sort, container, false)

        val mediaTypeSpinner = view.findViewById<Spinner>(R.id.main_sort_quality_media_type)
        mediaTypeSpinner.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                MediaTypeEnum.values()
            )
        mediaTypeSpinner.setSelection(1)

        val bitDepthSpinner = view.findViewById<Spinner>(R.id.main_sort_quality_bit_depth)
        bitDepthSpinner.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                BitDepthEnum.values()
            )

        val sampleRateSpinner = view.findViewById<Spinner>(R.id.main_sort_quality_sample_rate)
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

        view.findViewById<Button>(R.id.main_sort_quality_sort_button).setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set("result",
                Triple(
                    mediaTypeSpinner.selectedItem,
                    bitDepthSpinner.selectedItem,
                    SampleRateEnum.fromDouble(sampleRateSpinner.selectedItem as Double)!!))
            findNavController().popBackStack()
        }

        return view
    }
}
