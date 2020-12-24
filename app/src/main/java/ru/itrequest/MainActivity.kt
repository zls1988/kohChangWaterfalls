package ru.itrequest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.commit
import com.google.android.material.textview.MaterialTextView
import ru.itrequest.data.WaterfallRepository
import ru.itrequest.network.WaterfallService
import ru.itrequest.ui.WaterfallDetailFragment
import ru.itrequest.ui.WaterfallListFragment
import ru.itrequest.viewmodel.MainViewModel
import ru.itrequest.viewmodel.MainViewModelFactory
import ru.itrequest.viewmodel.ViewState

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        private val TAG = this::class.java.canonicalName
    }

    private val contentWindow: FrameLayout by lazy {
        findViewById(R.id.contentWindow)
    }

    private val loadingBar: ProgressBar by lazy {
        findViewById(R.id.mainProgressBar)
    }

    private val errorGroup: Group by lazy {
        findViewById(R.id.mainErrorGroup)
    }

    private val errorMessage: MaterialTextView by lazy {
        findViewById(R.id.mainErrorLabel)
    }

    private val reloadButton: Button by lazy {
        findViewById(R.id.mainReloadButton)
    }

    private val viewModel by viewModels<MainViewModel> {
        val service = WaterfallService.create()
        val repository = WaterfallRepository(service = service)
        MainViewModelFactory(repository = repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    /**
     * We log a new state for debug purposes
     */
    @SuppressLint("SetTextI18n")
    private fun setup() {
        showContent()
        reloadButton.setOnClickListener {
            viewModel.loadData()
        }
        viewModel.waterfalls.observe(this) {
            Log.d(TAG, "${it.size}")
        }
        viewModel.viewState.observe(this) {
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
            when (it) {
                ViewState.PROCESSING -> {
                    loadingBar.visibility = View.VISIBLE
                    errorGroup.visibility = View.GONE
                    Log.d(TAG, "PROCESSING")
                }
                ViewState.ERROR -> {
                    loadingBar.visibility = View.GONE
                    errorGroup.visibility = View.VISIBLE
                    contentWindow.visibility = View.INVISIBLE
                    errorMessage.text = "An error occurred, try to reload data."
                    Log.d(TAG, "ERROR")
                }
                ViewState.IDLE -> {
                    loadingBar.visibility = View.GONE
                    errorGroup.visibility = View.GONE
                    contentWindow.visibility = View.VISIBLE
                    Log.d(TAG, "IDLE")
                }
            }
        }
    }

    /**
     * We use navigation from code
     */
    private fun showContent() {
        val fragment = supportFragmentManager.findFragmentByTag(WaterfallListFragment.TAG)
        supportFragmentManager.commit {
            if (fragment == null)
                add(
                    contentWindow.id,
                    WaterfallListFragment.newInstance(),
                    WaterfallListFragment.TAG
                )
            else {
                replace(contentWindow.id, fragment, WaterfallListFragment.TAG)
            }
        }
    }

    fun choseWaterfall() {
        val fragment = supportFragmentManager.findFragmentByTag(WaterfallDetailFragment.TAG)
        supportFragmentManager.commit {
            if (fragment == null)
                replace(
                    contentWindow.id,
                    WaterfallDetailFragment.newInstance(),
                    WaterfallDetailFragment.TAG
                )
            else {
                replace(contentWindow.id, fragment, WaterfallDetailFragment.TAG)
            }
            addToBackStack(null)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}