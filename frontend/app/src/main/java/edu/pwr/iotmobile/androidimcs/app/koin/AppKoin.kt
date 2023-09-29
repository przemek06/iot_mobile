package edu.pwr.iotmobile.androidimcs.app.koin

import android.content.Context
import edu.pwr.iotmobile.androidimcs.app.database.AppDatabase
import edu.pwr.iotmobile.androidimcs.app.retrofit.AppRetrofit
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.ProjectDetailsViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login.LoginViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register.RegisterViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module

object AppKoin {

    // Module for local and remote environments, e.g. Retrofit.
    private val environments = module {
        single { AppDatabase.create(androidApplication()) }
        single { AppRetrofit.create() }
    }

    // Module for local and remote data sources
    private val dataSources = module {
    }

    // Module for repositories accessing data sources
    private val repositories = module {
    }

    private val viewModels = module {
        viewModelOf(::ProjectDetailsViewModel)
        viewModelOf(::LoginViewModel)
        viewModelOf(::RegisterViewModel)
    }

    private val modules by lazy {
        listOf(
            environments,
            dataSources,
            repositories,
            viewModels
        )
    }

    fun init(context: Context) {
        startKoin {
            androidLogger()
            androidContext(context)
            modules(modules)
        }
    }
}
