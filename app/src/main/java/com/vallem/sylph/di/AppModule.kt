package com.vallem.sylph.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseAuth
import com.vallem.sylph.R
import com.vallem.sylph.shared.auth.GoogleSignInClient
import com.vallem.sylph.shared.auth.GoogleSignInClientImpl
import com.vallem.sylph.shared.data.datastore.AppSettings
import com.vallem.sylph.shared.data.datastore.AppSettingsSerializer
import com.vallem.sylph.shared.data.local.SylphDatabase
import com.vallem.sylph.shared.data.remote.EventRemoteDataSource
import com.vallem.sylph.shared.data.remote.EventVotesRemoteDataSource
import com.vallem.sylph.shared.data.remote.UserRemoteDataSource
import com.vallem.sylph.shared.data.remote.impl.DynamoEventDataSource
import com.vallem.sylph.shared.data.remote.impl.DynamoEventVotesDataSource
import com.vallem.sylph.shared.data.remote.impl.DynamoUserDataSource
import com.vallem.sylph.shared.data.repository.EventUserVotesRepositoryImpl
import com.vallem.sylph.shared.data.repository.EventsRepositoryImpl
import com.vallem.sylph.shared.data.repository.FirebaseAuthRepositoryImpl
import com.vallem.sylph.shared.data.repository.UserRepositoryImpl
import com.vallem.sylph.shared.domain.repository.EventUserVotesRepository
import com.vallem.sylph.shared.domain.repository.EventsRepository
import com.vallem.sylph.shared.domain.repository.FirebaseAuthRepository
import com.vallem.sylph.shared.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
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
    fun provideSignInRequest(@ApplicationContext context: Context) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(context.getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

    @Provides
    fun provideAuthRepository(auth: FirebaseAuth): FirebaseAuthRepository =
        FirebaseAuthRepositoryImpl(auth)

    @Provides
    fun provideSignInClient(@ApplicationContext context: Context) =
        Identity.getSignInClient(context)

    @Provides
    @Named("HasGoogleFunctionality")
    fun provideHasGoogleFunctionality(@ApplicationContext context: Context) = GoogleApiAvailability
        .getInstance()
        .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS

    @Provides
    fun provideGoogleSignInClient(
        signInClient: SignInClient,
        signInRequest: BeginSignInRequest,
    ): GoogleSignInClient = GoogleSignInClientImpl(signInClient, signInRequest)

    @Provides
    fun providesEventsDataSource(): EventRemoteDataSource = DynamoEventDataSource()

    @Provides
    fun provideEventsRepository(
        dataSource: EventRemoteDataSource,
        votesRepository: EventUserVotesRepository,
    ): EventsRepository = EventsRepositoryImpl(dataSource, votesRepository)

    @Provides
    fun provideEventVotesDataSource(): EventVotesRemoteDataSource = DynamoEventVotesDataSource()

    @Provides
    fun provideEventVotesRepository(
        dataSource: EventVotesRemoteDataSource
    ): EventUserVotesRepository = EventUserVotesRepositoryImpl(dataSource)

    @Provides
    fun provideUserDataSource(): UserRemoteDataSource = DynamoUserDataSource()

    @Provides
    fun provideUserRepository(
        dataSource: UserRemoteDataSource,
        votesRepository: EventUserVotesRepository,
        eventsRepository: EventsRepository,
        database: SylphDatabase,
    ): UserRepository = UserRepositoryImpl(
        userDataSource = dataSource,
        votesRepository = votesRepository,
        eventsRepository = eventsRepository,
        userInfoDao = database.userInfoDao,
    )

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): SylphDatabase = Room.databaseBuilder(
        context,
        SylphDatabase::class.java,
        SylphDatabase.Name,
    ).build()

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