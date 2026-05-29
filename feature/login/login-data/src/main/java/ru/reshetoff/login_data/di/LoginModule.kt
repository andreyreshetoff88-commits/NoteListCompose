package ru.reshetoff.login_data.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.reshetoff.common.Constants.UNAUTHORIZED
import ru.reshetoff.database.SharedPref
import ru.reshetoff.login_data.api.LoginApi
import ru.reshetoff.login_data.repository.LoginRepositoryImpl
import ru.reshetoff.login_domain.repository.LoginRepository
import ru.reshetoff.login_domain.usecase.LoginUseCase
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LoginModule {
    @Provides
    @Singleton
    fun provideLoginApi(@Named(UNAUTHORIZED) retrofit: Retrofit): LoginApi =
        retrofit.create(LoginApi::class.java)

    @Provides
    fun provideLoginRepository(
        loginApi: LoginApi,
        sharedPref: SharedPref,
        gson: Gson
    ): LoginRepository {
        return LoginRepositoryImpl(loginApi = loginApi, sharedPref = sharedPref, gson = gson)
    }

    @Provides
    fun provideLoginUseCase(loginRepository: LoginRepository) =
        LoginUseCase(loginRepository = loginRepository)
}