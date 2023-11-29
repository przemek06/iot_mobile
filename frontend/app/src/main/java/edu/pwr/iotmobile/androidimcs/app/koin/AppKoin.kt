package edu.pwr.iotmobile.androidimcs.app.koin

import android.content.Context
import edu.pwr.iotmobile.androidimcs.app.database.AppDatabase
import edu.pwr.iotmobile.androidimcs.app.retrofit.AddCookiesInterceptor
import edu.pwr.iotmobile.androidimcs.app.retrofit.AppRetrofit
import edu.pwr.iotmobile.androidimcs.app.rsocket.WebSocketClient
import edu.pwr.iotmobile.androidimcs.data.scopestates.ComponentsListState
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.event.EventImpl
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.helpers.toast.ToastImpl
import edu.pwr.iotmobile.androidimcs.model.datasource.local.UserLocalDataSource
import edu.pwr.iotmobile.androidimcs.model.datasource.local.UserSessionLocalDataSource
import edu.pwr.iotmobile.androidimcs.model.datasource.local.impl.UserLocalDataSourceImpl
import edu.pwr.iotmobile.androidimcs.model.datasource.local.impl.UserSessionLocalDataSourceImpl
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.ComponentRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.DashboardRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.IntegrationRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.MessageRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.ProjectRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.TopicRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.UserRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl.ComponentRemoteDataSourceImpl
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl.DashboardRemoteDataSourceImpl
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl.IntegrationRemoteDataSourceImpl
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl.MessageRemoteDataSourceImpl
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl.ProjectRemoteDataSourceImpl
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl.TopicRemoteDataSourceImpl
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.impl.UserRemoteDataSourceImpl
import edu.pwr.iotmobile.androidimcs.model.repository.ComponentRepository
import edu.pwr.iotmobile.androidimcs.model.repository.DashboardRepository
import edu.pwr.iotmobile.androidimcs.model.repository.IntegrationRepository
import edu.pwr.iotmobile.androidimcs.model.repository.MessageRepository
import edu.pwr.iotmobile.androidimcs.model.repository.ProjectRepository
import edu.pwr.iotmobile.androidimcs.model.repository.TopicRepository
import edu.pwr.iotmobile.androidimcs.model.repository.UserRepository
import edu.pwr.iotmobile.androidimcs.model.repository.impl.ComponentRepositoryImpl
import edu.pwr.iotmobile.androidimcs.model.repository.impl.DashboardRepositoryImpl
import edu.pwr.iotmobile.androidimcs.model.repository.impl.IntegrationRepositoryImpl
import edu.pwr.iotmobile.androidimcs.model.repository.impl.MessageRepositoryImpl
import edu.pwr.iotmobile.androidimcs.model.repository.impl.ProjectRepositoryImpl
import edu.pwr.iotmobile.androidimcs.model.repository.impl.TopicRepositoryImpl
import edu.pwr.iotmobile.androidimcs.model.repository.impl.UserRepositoryImpl
import edu.pwr.iotmobile.androidimcs.ui.screens.account.AccountViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent.AddComponentViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.addtopic.AddTopicViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.admin.AdminViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.app.MainViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.changepassword.ChangePasswordViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.dashboard.DashboardViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.invitations.InvitationsViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.activate.ActivateAccountViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.forgotpassword.ForgotPasswordViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.login.LoginViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.loginregister.register.RegisterViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.main.MainScreenViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.projectdetails.ProjectDetailsViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.projects.ProjectsViewModel
import edu.pwr.iotmobile.androidimcs.ui.screens.search.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

object AppKoin {

    object Scope {
        val COMPONENT_LIST = named("componentListScope")
    }

    // Module for local and remote environments, e.g. Retrofit.
    private val environments = module {
        single { AppDatabase.create(androidApplication()) }
        single { AppRetrofit.create(get()) }
        single { WebSocketClient.create(get()) }
    }

    // Module for local and remote data sources
    private val dataSources = module {
        // Remote
        singleOf(::UserRemoteDataSourceImpl) bind UserRemoteDataSource::class
        singleOf(::ProjectRemoteDataSourceImpl) bind ProjectRemoteDataSource::class
        singleOf(::DashboardRemoteDataSourceImpl) bind DashboardRemoteDataSource::class
        singleOf(::TopicRemoteDataSourceImpl) bind TopicRemoteDataSource::class
        singleOf(::ComponentRemoteDataSourceImpl) bind ComponentRemoteDataSource::class
        singleOf(::MessageRemoteDataSourceImpl) bind MessageRemoteDataSource::class
        singleOf(::IntegrationRemoteDataSourceImpl) bind IntegrationRemoteDataSource::class

        // Local
        singleOf(::UserSessionLocalDataSourceImpl) bind UserSessionLocalDataSource::class
        singleOf(::UserLocalDataSourceImpl) bind UserLocalDataSource::class
        single { get<AppDatabase>().dashboardDao() }
    }

    // Module for repositories accessing data sources
    private val repositories = module {
        singleOf(::UserRepositoryImpl) bind UserRepository::class
        singleOf(::ProjectRepositoryImpl) bind ProjectRepository::class
        singleOf(::DashboardRepositoryImpl) bind DashboardRepository::class
        singleOf(::TopicRepositoryImpl) bind TopicRepository::class
        singleOf(::ComponentRepositoryImpl) bind ComponentRepository::class
        singleOf(::MessageRepositoryImpl) bind MessageRepository::class
        singleOf(::IntegrationRepositoryImpl) bind IntegrationRepository::class
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
        viewModelOf(::MainViewModel)
        viewModelOf(::AddComponentViewModel)
        viewModelOf(::InvitationsViewModel)
        viewModelOf(::MainScreenViewModel)
    }

    // Module for other singular classes
    private val misc = module {
        single { EventImpl() } bind Event::class
        single { ToastImpl() } bind Toast::class
        single { AddCookiesInterceptor(get()) }
    }

    private val scopes = module {
        scope(Scope.COMPONENT_LIST) {
            scoped { params ->
                ComponentsListState(
                    componentListDto = params.get(),
                )
            }
        }
    }

    private val modules by lazy {
        listOf(
            environments,
            dataSources,
            repositories,
            viewModels,
            misc,
            scopes
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
