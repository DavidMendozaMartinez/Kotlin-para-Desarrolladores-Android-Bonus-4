package com.antonioleiva.flowworkshop.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.antonioleiva.flowworkshop.R
import com.antonioleiva.flowworkshop.data.db.RoomDataSource
import com.antonioleiva.flowworkshop.data.domain.MoviesRepository
import com.antonioleiva.flowworkshop.data.server.TheMovieDbDataSource
import com.antonioleiva.flowworkshop.databinding.ActivityMainBinding
import com.antonioleiva.flowworkshop.ui.common.app
import com.antonioleiva.flowworkshop.ui.common.getViewModel
import com.antonioleiva.flowworkshop.ui.common.lastVisibleEvents
import com.antonioleiva.flowworkshop.ui.common.visible
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)

            viewModel = getViewModel(::buildViewModel)

            val moviesAdapter = MoviesAdapter(lifecycleScope)

            lifecycleScope.launchWhenStarted {
                viewModel.spinner.collect { progress.visible = it }
            }

            lifecycleScope.launchWhenStarted {
                viewModel.movies.collect { moviesAdapter.submitList(it) }
            }

            lifecycleScope.launchWhenStarted {
                recycler.lastVisibleEvents.collect { viewModel.notifyLastVisible(it) }
            }

            recycler.adapter = moviesAdapter
        }

    }

    private fun buildViewModel() = MainViewModel(
        MoviesRepository(
            RoomDataSource(app.db),
            TheMovieDbDataSource(getString(R.string.api_key))
        )
    )
}