package com.codigo.codigocodetest.application

import android.app.Application
import com.codigo.codigocodetest.repository.HomeRepository
import com.codigo.codigocodetest.retrofit.RetrofitObj
import com.codigo.codigocodetest.roomdb.MovieDB
import com.codigo.codigocodetest.utility.ViewModelFactory
import com.codigo.codigocodetest.viewmodel.HomeViewModel
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule

class CodigoApplication : Application(), DIAware {

    override val di by DI.lazy {
        import(androidXModule(this@CodigoApplication))

        //vmFactory
        bindSingleton { ViewModelFactory(di.direct) }

        //apiservice
        bindSingleton { RetrofitObj.API_SERVICE }

        //database
        bindSingleton { MovieDB.getInstance(instance()) }

        //repositories
        bindSingleton { HomeRepository(instance(), instance()) }


        //viewmodels
        bind<HomeViewModel>(HomeViewModel::class.java.simpleName) with provider {
            HomeViewModel(
                instance(), instance(), instance()
            )
        }

    }
}