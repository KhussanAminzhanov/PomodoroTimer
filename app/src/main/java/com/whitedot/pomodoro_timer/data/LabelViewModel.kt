package com.whitedot.pomodoro_timer.data

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LabelViewModel(private val repository: LabelRepository) : ViewModel() {

    val allLabels: LiveData<List<Label>> = repository.allLabels.asLiveData()

    fun insert(label: Label) = viewModelScope.launch {
        repository.insert(label)
    }
}

class LabelViewModelFactory(private val repository: LabelRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LabelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LabelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}