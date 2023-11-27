package com.example.grapefruit.di

import android.content.Context
import hu.blueberry.cloud.google.DriveManager
import com.example.grapefruit.test.repository.DriveRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.blueberry.cloud.google.GoogleSheetsManager
import hu.blueberry.cloud.google.base.CloudBase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CloudModule {

    @Provides
    fun cloudBase(context: Context) = CloudBase(context)

    //@Provides
    //fun drive(context: Context, cloudBase: CloudBase) = DriveManager(context, cloudBase)

    @Provides
    @Singleton
    fun provideDriveManager(cloudBase: CloudBase) = DriveManager(cloudBase)

    @Provides
    @Singleton
    fun provideSheetsManager(cloudBase: CloudBase) = GoogleSheetsManager(cloudBase)

    @Provides
    @Singleton
    fun providesDriveRepository(driveManager: DriveManager): DriveRepository = DriveRepository(driveManager)
}