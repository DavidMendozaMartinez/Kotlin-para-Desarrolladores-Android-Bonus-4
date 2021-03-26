package com.antonioleiva.flowworkshop.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.antonioleiva.flowworkshop.data.domain.Movie
import com.antonioleiva.flowworkshop.data.domain.MoviesRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MoviesRepository) : ViewModel() {
    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean> get() = _spinner

    val movies: LiveData<List<Movie>> get() = repository.getMovies().asLiveData()

    init {
        _spinner.value = true
    }

    fun notifyLastVisible(lastVisible: Int) {
        viewModelScope.launch {
            repository.checkRequireNewPage(lastVisible)
            _spinner.value = false
        }
    }
}