package montanez.alexander.socioinfonavit

import android.app.Application
import montanez.alexander.socioinfonavit.model.repository.User
import montanez.alexander.socioinfonavit.model.source.IUser
import montanez.alexander.socioinfonavit.model.source.IUserData
import montanez.alexander.socioinfonavit.ui.home.HomeViewModel
import montanez.alexander.socioinfonavit.ui.login.LoginViewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SocioInfonavitApplication : Application() {
    private val retrofitModule = module {
        single {
            Retrofit.Builder()
                .baseUrl("https://staging.api.socioinfonavit.io/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
    private val apiModule = module {
        fun provideUserAPI(retrofit: Retrofit) : IUser{
            return retrofit.create(IUser::class.java)
        }
        fun provideUserDataAPI(retrofit: Retrofit) : IUserData{
            return retrofit.create(IUserData::class.java)
        }
        single { provideUserAPI(get()) }
        single { provideUserDataAPI(get()) }
    }
    private val repositoryModule = module {
        single { User(get(),get()) }
    }

    private val viewModelModule = module {
        viewModel { LoginViewModel(get()) }
        viewModel { HomeViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(org.koin.core.logger.Level.ERROR) //temporal by bug in kt1.4
            androidContext(this@SocioInfonavitApplication)
            modules(
                retrofitModule,
                apiModule,
                repositoryModule,
                viewModelModule
            )
        }
    }

}