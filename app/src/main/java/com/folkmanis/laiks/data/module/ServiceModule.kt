package com.folkmanis.laiks.data.module

import com.folkmanis.laiks.data.*
import com.folkmanis.laiks.data.implementations.AccountServiceFirebase
import com.folkmanis.laiks.data.implementations.AppliancesServiceFirebase
import com.folkmanis.laiks.data.implementations.LaiksUserServiceFirebase
import com.folkmanis.laiks.data.implementations.LocalUserPreferencesRepository
import com.folkmanis.laiks.data.implementations.LocalesServiceFirebase
import com.folkmanis.laiks.data.implementations.MarketZonesServiceFirebase
import com.folkmanis.laiks.data.implementations.PermissionsFirebase
import com.folkmanis.laiks.data.implementations.PricesServiceFirebase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    abstract fun provideAccountService(impl: AccountServiceFirebase): AccountService

    @Binds
    abstract  fun providePricesService(impl: PricesServiceFirebase): PricesService

    @Binds
    abstract  fun providePreferencesService(impl: LocalUserPreferencesRepository): UserPreferencesRepository

    @Binds
    abstract fun provideAppliancesService(impl: AppliancesServiceFirebase): AppliancesService

    @Binds
    abstract  fun providePermissionsService(impl: PermissionsFirebase): PermissionsService

    @Binds
    abstract  fun provideLaiksUserService(impl: LaiksUserServiceFirebase): LaiksUserService

    @Binds
    abstract fun provideMarketZonesService(impl: MarketZonesServiceFirebase): MarketZonesService

    @Binds
    abstract fun provideLocalesService(impl: LocalesServiceFirebase): LocalesService

}