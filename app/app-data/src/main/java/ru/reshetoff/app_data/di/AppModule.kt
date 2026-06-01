package ru.reshetoff.app_data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.reshetoff.app_data.api.AppApi
import ru.reshetoff.app_data.repository.AppRepositoryImpl
import ru.reshetoff.app_domain.repository.AppRepository
import ru.reshetoff.app_domain.usecase.HasAccessTokenUseCase
import ru.reshetoff.common.Constants.AUTHORIZED
import ru.reshetoff.database.SharedPref
import ru.reshetoff.database.dao.ProfileDao
import ru.reshetoff.network.NetworkUtils
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAppApi(@Named(AUTHORIZED) retrofit: Retrofit): AppApi =
        retrofit.create(AppApi::class.java)

    @Provides
    fun provideAppRepository(
        sharedPref: SharedPref,
        profileDao: ProfileDao,
        appApi: AppApi,
        networkUtils: NetworkUtils
    ): AppRepository {
        return AppRepositoryImpl(
            sharedPref = sharedPref,
            profileDao = profileDao,
            appApi = appApi,
            networkUtils = networkUtils
        )
    }

    @Provides
    fun provideHasAccessTokenUseCase(appRepository: AppRepository) =
        HasAccessTokenUseCase(appRepository = appRepository)
}