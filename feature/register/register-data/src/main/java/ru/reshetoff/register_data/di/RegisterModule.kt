package ru.reshetoff.register_data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.reshetoff.common.Constants.UNAUTHORIZED
import ru.reshetoff.register_data.api.RegisterApi
import ru.reshetoff.register_data.repository.RegisterRepositoryImpl
import ru.reshetoff.register_domain.repository.RegisterRepository
import ru.reshetoff.register_domain.usecase.GetCountryListUseCase
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RegisterModule {
    @Provides
    @Singleton
    fun provideRegisterApi(@Named(UNAUTHORIZED) retrofit: Retrofit): RegisterApi =
        retrofit.create(RegisterApi::class.java)

    @Provides
    fun provideRegisterRepository(registerApi: RegisterApi): RegisterRepository {
        return RegisterRepositoryImpl(registerApi = registerApi)
    }

    @Provides
    fun provideGetCountryListUseCase(registerRepository: RegisterRepository) =
        GetCountryListUseCase(registerRepository = registerRepository)
}