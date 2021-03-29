package com.antonioleiva.flowworkshop.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antonioleiva.flowworkshop.R
import com.antonioleiva.flowworkshop.data.db.RoomDataSource
import com.antonioleiva.flowworkshop.data.domain.MoviesRepository
import com.antonioleiva.flowworkshop.data.server.TheMovieDbDataSource
import com.antonioleiva.flowworkshop.databinding.ActivityMainBinding
import com.antonioleiva.flowworkshop.ui.common.app
import com.antonioleiva.flowworkshop.ui.common.getViewModel
import com.antonioleiva.flowworkshop.ui.common.visible
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)

            viewModel = getViewModel(::buildViewModel)

            val moviesAdapter = MoviesAdapter()

            lifecycleScope.launchWhenStarted {
                viewModel.spinner.collect { progress.visible = it }
            }

            lifecycleScope.launchWhenStarted {
                viewModel.movies.collect { moviesAdapter.submitList(it) }
            }

            recycler.adapter = moviesAdapter

            val layoutManager = recycler.layoutManager as GridLayoutManager
            recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    viewModel.lastVisible.value = layoutManager.findLastVisibleItemPosition()
                }
            })
        }

    }

    private fun buildViewModel() = MainViewModel(
        MoviesRepository(
            RoomDataSource(app.db),
            TheMovieDbDataSource(getString(R.string.api_key))
        )
    )
}