package com.example.grapefruit.di

import android.content.Context
import com.example.grapefruit.model.MemoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context)= context

    @Provides
    @Singleton
    fun provideMemoryDatabase() = MemoryDatabase()

}