package edu.pwr.iotmobile.androidimcs.app.koin

import android.content.Context
import edu.pwr.iotmobile.androidimcs.app.database.AppDatabase
import edu.pwr.iotmobile.androidimcs.app.retrofit.AppRetrofit
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.event.EventImpl
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.helpers.toast.ToastImpl
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.DashboardRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.ProjectRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.TopicRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.UserRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl.DashboardRemoteDataSourceImpl
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl.ProjectRemoteDataSourceImpl
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl.TopicRemoteDataSourceImpl
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl.UserRemoteDataSourceImpl
import edu.pwr.iotmobile.androidimcs.model.repository.DashboardRepository
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository
import edu.pwr.iotmobile.androidimcs.model.repository.TopicRepository
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import edu.pwr.iotmobile.androidimcs.model.repository.impl.DashboardRepositoryImpl
import edu.pwr.iotmobile.androidimcs.model.repository.impl.ProjectRepositoryImpl
import edu.pwr.iotmobile.androidimcs.model.repository.impl.TopicRepositoryImpl
import edu.pwr.iotmobile.androidimcs.model.repository.impl.UserRepositoryImpl
import edu.pwr.iotmobile.androidimcs.ui.screens.account.AccountViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.addtopic.AddTopicViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.admin.AdminViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.changepassword.ChangePasswordViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.activate.ActivateAccountViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.forgotpassword.ForgotPasswordViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login.LoginViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register.RegisterViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.ProjectDetailsViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.projects.ProjectsViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.search.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
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
        singleOf(::UserRemoteDataSourceImpl) bind UserRemoteDataSource::class
        singleOf(::ProjectRemoteDataSourceImpl) bind ProjectRemoteDataSource::class
        singleOf(::DashboardRemoteDataSourceImpl) bind DashboardRemoteDataSource::class
        singleOf(::TopicRemoteDataSourceImpl) bind TopicRemoteDataSource::class
    }

    // Module for repositories accessing data sources
    private val repositories = module {
        singleOf(::UserRepositoryImpl) bind UserRepository::class
        singleOf(::ProjectRepositoryImpl) bind ProjectRepository::class
        singleOf(::DashboardRepositoryImpl) bind DashboardRepository::class
        singleOf(::TopicRepositoryImpl) bind TopicRepository::class
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
        viewModelOf(::DashboardViewModel)
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
