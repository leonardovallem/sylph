package com.vallem.sylph.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.google.firebase.auth.FirebaseAuth
import com.vallem.sylph.shared.data.datastore.AppSettings
import com.vallem.sylph.shared.data.datastore.AppSettingsSerializer
import com.vallem.sylph.shared.data.remote.EventRemoteDataSource
import com.vallem.sylph.shared.data.remote.impl.DynamoEventDataSource
import com.vallem.sylph.shared.data.repository.AuthRepositoryImpl
import com.vallem.sylph.shared.data.repository.EventsRepositoryImpl
import com.vallem.sylph.shared.domain.repository.AuthRepository
import com.vallem.sylph.shared.domain.repository.EventsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    fun providesEventsDataSource(): EventRemoteDataSource = DynamoEventDataSource()

    @Provides
    fun provideEventsRepository(
        dataSource: EventRemoteDataSource
    ): EventsRepository = EventsRepositoryImpl(dataSource)

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<AppSettings> =
        DataStoreFactory.create(
            serializer = AppSettingsSerializer(),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        ) {
            context.dataStoreFile("user_data.pb")
        }
}