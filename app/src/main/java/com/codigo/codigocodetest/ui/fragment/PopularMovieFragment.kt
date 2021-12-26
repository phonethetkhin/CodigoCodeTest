package com.codigo.codigocodetest.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.codigo.codigocodetest.R
import com.codigo.codigocodetest.adapter.HomePagingAdapter
import com.codigo.codigocodetest.adapter.HomePagingAdapterPopular
import com.codigo.codigocodetest.adapter.LoadingAdapter
import com.codigo.codigocodetest.databinding.FragmentPopularMovieBinding
import com.codigo.codigocodetest.roomdb.MovieDB
import com.codigo.codigocodetest.ui.activity.HomeActivity
import com.codigo.codigocodetest.utility.delegateutils.fragmentViewBinding
import com.codigo.codigocodetest.utility.kodeinViewModel
import com.codigo.codigocodetest.utility.showToast
import com.codigo.codigocodetest.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance
import java.lang.Exception

/**
 * Fetch and show the popular movies
 */
class PopularMovieFragment : Fragment(R.layout.fragment_popular_movie), DIAware {
    override val di: DI by closestDI()
    private val binding by fragmentViewBinding(FragmentPopularMovieBinding::bind)
    lateinit var homePagingAdapter: HomePagingAdapterPopular
    private val homeViewModel: HomeViewModel by kodeinViewModel()
    val movieDB: MovieDB by instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).supportActionBar!!.title = resources.getString(R.string.popular)
        homePagingAdapter = HomePagingAdapterPopular(movieDB, requireContext())
        setAdapter()
        observeUpcoming()
        loadingStateHandler()
        binding.btnRetry.setOnClickListener {
            homePagingAdapter.retry()
        }
    }

    private fun observeUpcoming() {
        lifecycleScope.launch {
            homeViewModel.getPopular().collectLatest {
                homePagingAdapter.submitData(it)
            }
        }
    }

    private fun setAdapter() {
        binding.rcvPopular.adapter =
            homePagingAdapter.withLoadStateFooter(footer = LoadingAdapter { homePagingAdapter.retry() })
    }

    fun loadingStateHandler() {
        lifecycleScope.launch {
            homePagingAdapter.loadStateFlow.collectLatest { loadState ->
                try {
                    // Show loading spinner during initial load or refresh.
                    binding.pgbLoading.isVisible = loadState.source.refresh is LoadState.Loading
                    // Show the retry state if initial load or refresh fails.
                    binding.btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                } catch (e: Exception) {
                    Log.d("error", e.localizedMessage.toString())
                }

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    requireContext().showToast("Whoops Error Occurred${it.error}")
                }
            }
        }
    }
}