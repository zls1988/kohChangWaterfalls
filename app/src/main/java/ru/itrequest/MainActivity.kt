package ru.itrequest

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.commit
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

    private val loadingBar: ConstraintLayout by lazy {
        findViewById(R.id.mainLoadingLayout)
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
    private fun setup() {
        showContent()
        viewModel.waterfalls.observe(this) {
            Log.d(TAG, "${it.size}")
        }
        viewModel.viewState.observe(this) {
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
            when (it) {
                ViewState.PROCESSING -> {
                    loadingBar.visibility = View.VISIBLE
                    Log.d(TAG, "PROCESSING")
                }
                ViewState.ERROR -> {
                    loadingBar.visibility = View.GONE
                    Log.d(TAG, "ERROR")
                }
                ViewState.IDLE -> {
                    loadingBar.visibility = View.GONE
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