package com.codigo.codigocodetest.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.codigo.codigocodetest.R
import com.codigo.codigocodetest.databinding.ActivityHomeBinding
import com.codigo.codigocodetest.utility.delegateutils.activityViewBinding
import com.codigo.codigocodetest.utility.kodeinViewModel
import com.codigo.codigocodetest.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayout
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI

class HomeActivity : AppCompatActivity(), DIAware {
    override val di by closestDI()
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private val homeViewModel: HomeViewModel by kodeinViewModel()
    private val binding by activityViewBinding(ActivityHomeBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcvHome) as NavHostFragment
        navController = navHostFragment.findNavController()

        homeViewModel.getTabPositionLiveData().observe(this, {
            if (it != null) {
                binding.tblHome.getTabAt(it)!!.select()
            }
        })
        setToolbar()
        tabSelectedListener()
    }

    private fun setToolbar() {
        setSupportActionBar(binding.include.tlbToolbar)
    }

    private fun tabSelectedListener() {
        binding.tblHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> {
                        homeViewModel.setTabPositionLiveData(0)
                        navController.popBackStack(R.id.upcomingMovieFragment,true)
                        navController.navigate(R.id.upcomingMovieFragment)
                    }
                    1 -> {
                        homeViewModel.setTabPositionLiveData(1)
                        navController.popBackStack(R.id.upcomingMovieFragment,false)
                        navController.navigate(R.id.popularMovieFragment)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        when (navController.currentDestination?.id) {
            R.id.upcomingMovieFragment -> {
                binding.tblHome.getTabAt(0)!!.select()
            }
        }
    }
}