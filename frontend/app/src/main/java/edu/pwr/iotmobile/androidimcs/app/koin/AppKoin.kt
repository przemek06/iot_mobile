package edu.pwr.iotmobile.androidimcs.app.koin

import android.content.Context
import edu.pwr.iotmobile.androidimcs.app.database.AppDatabase
import edu.pwr.iotmobile.androidimcs.app.retrofit.AppRetrofit
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.event.EventImpl
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.helpers.toast.ToastImpl
import edu.pwr.iotmobile.androidimcs.ui.screens.account.AccountViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.addtopic.AddTopicViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.changepassword.ChangePasswordViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.ProjectDetailsViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.projects.ProjectsViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.activate.ActivateAccountViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login.LoginViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register.RegisterViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.forgotpassword.ForgotPasswordViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.admin.AdminViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.search.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.bind
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

    // Module for view models
    private val viewModels = module {
        viewModelOf(::ProjectsViewModel)
        viewModelOf(::ProjectDetailsViewModel)
        viewModelOf(::LoginViewModel)
        viewModelOf(::RegisterViewModel)
        viewModelOf(::ForgotPasswordViewModel)
        viewModelOf(::ActivateAccountViewModel)
        viewModelOf(::AccountViewModel)
        viewModelOf(::ChangePasswordViewModel)
        viewModelOf(::AdminViewModel)
        viewModelOf(::SearchViewModel)
        viewModelOf(::AddTopicViewModel)
    }

    // Module for other singular classes
    private val misc = module {
        single { EventImpl() } bind Event::class
        single { ToastImpl() } bind Toast::class
    }

    private val modules by lazy {
        listOf(
            environments,
            dataSources,
            repositories,
            viewModels,
            misc
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
