package com.codigo.codigocodetest.ui.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.codigo.codigocodetest.R
import com.codigo.codigocodetest.databinding.ActivityMovieDetailBinding
import com.codigo.codigocodetest.roomdb.MovieDB
import com.codigo.codigocodetest.utility.delegateutils.activityViewBinding
import com.codigo.codigocodetest.utility.kodeinViewModel
import com.codigo.codigocodetest.utility.showToast
import com.codigo.codigocodetest.viewmodel.HomeViewModel
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

class MovieDetailActivity : AppCompatActivity(), DIAware {
    override val di by closestDI()
    private val binding by activityViewBinding(ActivityMovieDetailBinding::inflate)
    private val homeViewModel: HomeViewModel by kodeinViewModel()
    var movieId = 0
    var status = 0
    var movieName: String? = ""
    val genresList = ArrayList<String>()
    val movieDB: MovieDB by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        movieId = intent.getIntExtra("movieId", 0)
        status = intent.getIntExtra("status", 0)
        movieName = intent.getStringExtra("movieName")
        setImageColor()
        binding.imgFav.setOnClickListener {
            if (status == 1) {
                if ((movieDB.movieDao().getFavStatusUpComing(movieId)) == 0) {
                    movieDB.movieDao().setFavoriteUpComing(movieId)
                    setImageColor()
                } else {
                    movieDB.movieDao().removeFavoriteUpComing(movieId)
                    setImageColor()
                }
            }
            if (status == 2) {
                if ((movieDB.movieDao().getFavStatusPopular(movieId)) == 0) {
                    movieDB.movieDao().setFavoritePopular(movieId)
                    setImageColor()
                } else {
                    movieDB.movieDao().removeFavoritePopular(movieId)
                    setImageColor()
                }
            }
        }
        observeMovieDetail()
        setToolbar()
    }

    fun setImageColor() {
        if (status == 1) {
            if ((movieDB.movieDao().getFavStatusUpComing(movieId)) == 0) {
                binding.imgFav.background.setColorFilter(
                    resources.getColor(R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
                binding.imgFav.setColorFilter(
                    ContextCompat.getColor(this, R.color.black),
                    PorterDuff.Mode.SRC_IN
                )
            } else {
                binding.imgFav.background.setColorFilter(
                    resources.getColor(R.color.purple_200),
                    PorterDuff.Mode.SRC_ATOP
                )
                binding.imgFav.setColorFilter(
                    ContextCompat.getColor(this, R.color.white),
                    PorterDuff.Mode.SRC_IN
                )
            }
        } else if (status == 2) {
            if ((movieDB.movieDao().getFavStatusPopular(movieId)) == 0) {
                binding.imgFav.background.setColorFilter(
                    resources.getColor(R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
                binding.imgFav.setColorFilter(
                    ContextCompat.getColor(this, R.color.black),
                    PorterDuff.Mode.SRC_IN
                )
            } else {
                binding.imgFav.background.setColorFilter(
                    resources.getColor(R.color.purple_200),
                    PorterDuff.Mode.SRC_ATOP
                )
                binding.imgFav.setColorFilter(
                    ContextCompat.getColor(this, R.color.white),
                    PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    fun observeMovieDetail() {
        homeViewModel.getMovieDetailLiveData(movieId).observe(this, {
            it?.let {
                Glide.with(this).load("https://image.tmdb.org/t/p/original/${it.backdrop_path}")
                    .placeholder(R.drawable.placeholder).into(binding.imgMovieCover)
                Glide.with(this).load("https://image.tmdb.org/t/p/original/${it.poster_path}")
                    .placeholder(R.drawable.placeholder).into(binding.imgMoviePhoto)
                binding.txtMoviesTitle.text = it.title
                it.genres.forEach { genre -> genresList.add(genre.name) }
                val genre = genresList.joinToString()
                binding.txtGenre.text = genre
                binding.txtProductionYear.text = it.release_date
                binding.txtDuration.text = "${it.runtime} minutes"
                binding.txtLanguage.text = it.original_language
                binding.txtStatus.text = it.status
                binding.txtTagLine.text = it.tagline
                binding.txtDescription.text = it.overview
                binding.txtRating.text = "${it.vote_average}"
            }
        })
    }

    private fun setToolbar() {
        setSupportActionBar(binding.includeMovieDetail.tlbToolbar)
        supportActionBar!!.title = if (movieName != null) movieName else "Codigo"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.includeMovieDetail.tlbToolbar.navigationIcon!!.setColorFilter(
            resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}