package ru.reshetoff.app_data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.reshetoff.app_data.repository.AppRepositoryImpl
import ru.reshetoff.app_domain.repository.AppRepository
import ru.reshetoff.app_domain.usecase.HasAccessTokenUseCase
import ru.reshetoff.database.SharedPref

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideAppRepository(sharedPref: SharedPref): AppRepository {
        return AppRepositoryImpl(sharedPref = sharedPref)
    }

    @Provides
    fun provideHasAccessTokenUseCase(appRepository: AppRepository) =
        HasAccessTokenUseCase(appRepository = appRepository)
}