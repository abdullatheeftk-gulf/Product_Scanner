package com.gulftechiinovations.product_scanner.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gulftechiinovations.product_scanner.data.api.ApiService
import com.gulftechiinovations.product_scanner.data.api.ApiServiceImpl
import com.gulftechiinovations.product_scanner.data.firebase.FireStoreService
import com.gulftechiinovations.product_scanner.data.firebase.FireStoreServiceImpl
import com.gulftechiinovations.product_scanner.util.SharedMemory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideKtorClient(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    contentType = ContentType.Application.Json,
                    json = Json {
                        encodeDefaults = true
                    }
                )
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            install(HttpCache)

            engine {
                connectTimeout = 15_000
                socketTimeout = 15_000
            }
        }
    }


    @Provides
    @Singleton
    fun provideApiService(client: HttpClient,sharedMemory: SharedMemory): ApiService = ApiServiceImpl(apiClient = client, sharedMemory = sharedMemory)

    @Provides
    @Singleton
    fun provideFirebaseDb():FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideFireStoreService(fireStore: FirebaseFirestore): FireStoreService = FireStoreServiceImpl(fireStore)

}