package com.gulftechiinovations.product_scanner.di

import android.content.Context
import android.provider.Settings
import com.gulftechiinovations.product_scanner.data.data_store.DataStoreService
import com.gulftechiinovations.product_scanner.data.data_store.DataStoreServiceImpl
import com.gulftechiinovations.product_scanner.util.SharedMemory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataSoreService(@ApplicationContext context: Context): DataStoreService {
        return DataStoreServiceImpl(context = context)
    }

    @Provides
    @Singleton
    fun provideSharedMemory(): SharedMemory {
        return SharedMemory
    }

    @Provides
    @Singleton
    fun provideDeviceId(@ApplicationContext context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) + "CD"
    }


}