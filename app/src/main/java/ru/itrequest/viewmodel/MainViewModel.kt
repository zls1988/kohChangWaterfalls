package ru.itrequest.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.itrequest.data.Waterfall
import ru.itrequest.data.WaterfallRepository
import ru.itrequest.util.Result

class MainViewModel(
    private val repository: WaterfallRepository
) : ViewModel() {

    private val _waterfalls = MutableLiveData<List<Waterfall>>(emptyList())
    val waterfalls: LiveData<List<Waterfall>>
    get() { return _waterfalls }

    private val _viewState = MutableLiveData(ViewState.IDLE)
    val viewState: LiveData<ViewState>
        get() { return _viewState }

    fun getWaterfallList() {
        viewModelScope.launch {
            _viewState.value = ViewState.PROCESSING
            when (val result = repository.getAll()) {
                is Result.Success -> {
                    _viewState.value = ViewState.IDLE
                    _waterfalls.value = result.success
                }
                is Result.Failure -> {
                    _viewState.value = ViewState.ERROR
                }
            }
        }
    }
}

enum class ViewState() {
    PROCESSING, ERROR, IDLE
}

/**
 * As [MainViewModel] needs a repository, we create an instance through a viewmodel factory
 */
class MainViewModelFactory(private val repository: WaterfallRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress( "UNCHECKED_CAST")
            return MainViewModel(repository = repository) as T
        } else {
            error("Can't construct a viewmodel")
        }
    }
}