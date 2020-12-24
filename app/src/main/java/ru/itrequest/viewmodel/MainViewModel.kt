package ru.itrequest.viewmodel

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.itrequest.data.Waterfall
import ru.itrequest.data.WaterfallRepository
import ru.itrequest.util.Result

class MainViewModel(
    private val repository: WaterfallRepository
) : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.canonicalName
    }

    // TODO: We could keep all data in the separate class (e.g. StateManager)
    private val _chosenWaterfall = MutableLiveData<Waterfall?>(null)
    val chosenWaterfall: LiveData<Waterfall?>
        get() {
            return _chosenWaterfall
        }

    private val _waterfalls = MutableLiveData<List<Waterfall>>(emptyList())

    val waterfalls: LiveData<List<Waterfall>>
        get() {
            return _waterfalls
        }

    private val _viewState = MutableLiveData(ViewState.IDLE)
    val viewState: LiveData<ViewState>
        get() {
            return _viewState
        }

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _viewState.value = ViewState.PROCESSING
            when (val result = repository.getAll()) {
                is Result.Success -> {
                    _viewState.value = ViewState.IDLE
                    _waterfalls.value = result.success
                }
                is Result.Failure -> {
                    val cause = result.error
                    cause.printStackTrace()
                    Log.e(TAG, cause.localizedMessage ?: "localizedMessage is null")
                    _viewState.value = ViewState.ERROR
                }
            }
        }
    }

    fun setWaterfall(waterfall: Waterfall) {
        _chosenWaterfall.value = waterfall
    }
}

enum class ViewState {
    PROCESSING, ERROR, IDLE
}

/**
 * As [MainViewModel] needs a repository, we create an instance through a viewmodel factory
 */
class MainViewModelFactory(private val repository: WaterfallRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository = repository) as T
        } else {
            error("Can't construct a viewmodel")
        }
    }
}