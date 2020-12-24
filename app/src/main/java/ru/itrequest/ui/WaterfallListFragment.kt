package ru.itrequest.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.itrequest.MainActivity
import ru.itrequest.R
import ru.itrequest.adapter.WaterfallAdapter
import ru.itrequest.viewmodel.MainViewModel

class WaterfallListFragment : Fragment() {

    companion object {
        const val TAG = "WaterfallListFragment"
        fun newInstance() = WaterfallListFragment()
    }

    private val viewModel: MainViewModel by activityViewModels()
    private val waterfallAdapter = WaterfallAdapter {
        Log.d(TAG, "on ${it.id} clicked")
        viewModel.setWaterfall(it)
        (activity as MainActivity).choseWaterfall()
    }
    private lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.waterfall_list, container, false)
        recycler = view.findViewById(R.id.recycleWaterfalls)
        recycler.apply {
            layoutManager = LinearLayoutManager(activity)
            itemAnimator = DefaultItemAnimator()
            adapter = waterfallAdapter
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.waterfalls.observe(viewLifecycleOwner) {
            waterfallAdapter.submitList(it)
        }
    }
}