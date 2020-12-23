package ru.itrequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import ru.itrequest.data.WaterfallRepository
import ru.itrequest.network.WaterfallService
import ru.itrequest.viewmodel.MainViewModel
import ru.itrequest.viewmodel.MainViewModelFactory
import ru.itrequest.viewmodel.ViewState

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = this::class.java.canonicalName
    }

    private val viewModel by viewModels<MainViewModel> {
        val service = WaterfallService.create()
        val repository = WaterfallRepository(service = service)
        MainViewModelFactory(repository = repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    /**
     * We log a new state for debug purposes
     */
    private fun setup() {
        viewModel.waterfalls.observe(this) {
            Log.d(TAG,"${it.size}")
        }
        viewModel.viewState.observe(this) {
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
            when (it) {
                ViewState.PROCESSING -> Log.d(TAG, "PROCESSING")
                ViewState.ERROR -> Log.d(TAG, "ERROR")
                ViewState.IDLE -> Log.d(TAG, "IDLE")
            }
        }
        viewModel.getWaterfallList()
    }
}