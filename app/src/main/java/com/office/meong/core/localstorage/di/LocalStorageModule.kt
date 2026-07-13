package com.office.meong.core.localstorage.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.office.meong.core.localstorage.qualifier.AuthDataStore
import com.office.meong.core.localstorage.qualifier.UserDataStore
import com.office.meong.core.localstorage.token.TokenManager
import com.office.meong.core.localstorage.token.TokenManagerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalStorageModule {

    @Binds
    @Singleton
    abstract fun bindTokenManager(impl: TokenManagerImpl): TokenManager

    companion object {
        private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "meong_auth_datastore")
        private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "meong_user_datastore")

        @AuthDataStore
        @Provides
        @Singleton
        fun provideAuthDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.authDataStore
        }

        @UserDataStore
        @Provides
        @Singleton
        fun provideUserDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.userDataStore
        }
    }
}