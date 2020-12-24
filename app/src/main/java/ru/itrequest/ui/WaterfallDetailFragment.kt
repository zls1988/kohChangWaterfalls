package ru.itrequest.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textview.MaterialTextView
import ru.itrequest.R
import ru.itrequest.data.Waterfall
import ru.itrequest.viewmodel.MainViewModel

class WaterfallDetailFragment : Fragment(R.layout.waterfall_detail) {

    companion object {
        const val TAG = "WaterfallDetailFragment"
        fun newInstance() = WaterfallDetailFragment()
    }

    private val viewModel: MainViewModel by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.chosenWaterfall.observe(viewLifecycleOwner) {
            renderWaterfallDetailInfo(it)
        }
    }

    /**
     * We use the hardcoded strings for test purposes (should use string.xml resource)
     */
    @SuppressLint("SetTextI18n")
    private fun renderWaterfallDetailInfo(it: Waterfall?) {
        val v = view ?: error("Can't render the waterfall details info cause the view is null!")
        if (it == null) {
            val content = v.findViewById<Group>(R.id.detailContentGroup)
            content.apply {
                visibility = View.GONE
            }
            v.findViewById<MaterialTextView>(R.id.detailNameLabel)?.text =
                getString(R.string.nothing_to_show)
            Log.d(TAG, "Chosen waterfall is null")
            return
        }
        v.findViewById<Group>(R.id.detailContentGroup).apply {
            visibility = View.VISIBLE
        }
        v.findViewById<MaterialTextView>(R.id.detailNameLabel)?.text = it.name
        v.findViewById<MaterialTextView>(R.id.detailAddress)?.text = "Address: ${it.address}"
        v.findViewById<MaterialTextView>(R.id.detailOpenHours)?.text = "Open hours: ${it.openHours}"
        v.findViewById<MaterialTextView>(R.id.detailDescription)?.text =
            "Description:\n${it.description}"
        v.findViewById<MaterialTextView>(R.id.detailNote)?.text = "Notes: ${it.notes}"

    }
}