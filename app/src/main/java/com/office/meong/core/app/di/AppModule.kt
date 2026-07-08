package com.office.meong.core.app.di

import com.office.meong.core.app.AppRestarter
import com.office.meong.core.app.AppRestarterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindAppRestarter(impl: AppRestarterImpl): AppRestarter
}
