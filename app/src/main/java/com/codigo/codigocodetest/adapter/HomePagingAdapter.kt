package com.codigo.codigocodetest.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codigo.codigocodetest.R
import com.codigo.codigocodetest.databinding.ListItemMovieBinding
import com.codigo.codigocodetest.roomdb.MovieDB
import com.codigo.codigocodetest.roomdb.entity.UpComingMovieEntity
import com.codigo.codigocodetest.ui.activity.MovieDetailActivity
import com.codigo.codigocodetest.utility.delegateutils.adapterViewBinding


class HomePagingAdapter(val movieDB: MovieDB, val context: Context) :
    PagingDataAdapter<UpComingMovieEntity, HomePagingAdapter.HomePagingViewHolder>(DataDifferntiator) {

    class HomePagingViewHolder(val binding: ListItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: HomePagingViewHolder, position: Int) {
        val movie = getItem(position)!!

        if ((movieDB.movieDao().getFavStatusUpComing(movie.id)) == 0) {
            holder.binding.imgFav.background.setColorFilter(
                context.resources.getColor(R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            holder.binding.imgFav.setColorFilter(
                ContextCompat.getColor(context, R.color.black),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            holder.binding.imgFav.background.setColorFilter(
                context.resources.getColor(R.color.purple_200),
                PorterDuff.Mode.SRC_ATOP
            )
            holder.binding.imgFav.setColorFilter(
                ContextCompat.getColor(context, R.color.white),
                PorterDuff.Mode.SRC_IN
            )

        }
        setData(movie, holder)
        holder.itemView.setOnClickListener {
            val i = Intent(context, MovieDetailActivity::class.java)
            i.putExtra("movieId", movie.id)
            i.putExtra("status", 1)
            i.putExtra("movieName", movie.title)
            context.startActivity(i)
        }
        holder.binding.imgFav.setOnClickListener {
            if ((movieDB.movieDao().getFavStatusUpComing(movie.id)) == 0)
                movieDB.movieDao().setFavoriteUpComing(movie.id)
            else
                movieDB.movieDao().removeFavoriteUpComing(movie.id)
        }
    }

    private fun setData(upComingMovie: UpComingMovieEntity, holder: HomePagingViewHolder) {
        with(holder) {

            upComingMovie.poster_path?.let {
                Glide.with(context)
                    .load("https://image.tmdb.org/t/p/original/${upComingMovie.poster_path}")
                    .placeholder(
                        R.drawable.placeholder
                    )
                    .into(binding.imgMoviesImage)
            }
            upComingMovie.title?.let { binding.txtMoviesTitle.text = upComingMovie.title }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePagingViewHolder {
        val v = parent.adapterViewBinding(ListItemMovieBinding::inflate)
        return HomePagingViewHolder(v)
    }

    object DataDifferntiator : DiffUtil.ItemCallback<UpComingMovieEntity>() {

        override fun areItemsTheSame(
            oldItem: UpComingMovieEntity,
            newItem: UpComingMovieEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UpComingMovieEntity,
            newItem: UpComingMovieEntity
        ): Boolean {
            return oldItem == newItem
        }
    }

}