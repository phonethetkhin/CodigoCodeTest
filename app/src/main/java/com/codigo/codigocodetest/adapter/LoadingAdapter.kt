package com.codigo.codigocodetest.adapter

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codigo.codigocodetest.databinding.ListItemLoadingBinding
import com.codigo.codigocodetest.utility.delegateutils.adapterViewBinding

class LoadingAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadingAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        val progress = holder.binding.pgbLoading
        val btnRetry = holder.binding.btnRetry
        val txtErrorMessage = holder.binding.txtErrorMessage

        btnRetry.isVisible = loadState !is LoadState.Loading
        txtErrorMessage.isVisible = loadState !is LoadState.Loading
        progress.isVisible = loadState is LoadState.Loading

        if (loadState is LoadState.Error) {
            txtErrorMessage.text = loadState.error.localizedMessage
        }

        btnRetry.setOnClickListener {
            retry.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val v =
            parent.adapterViewBinding(ListItemLoadingBinding::inflate)
        return LoadStateViewHolder(v)
    }

    class LoadStateViewHolder(val binding: ListItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)
}