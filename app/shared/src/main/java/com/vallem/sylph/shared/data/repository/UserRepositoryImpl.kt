package com.vallem.sylph.shared.data.repository

import com.vallem.sylph.shared.data.dynamo.DynamoDbInstantiationException
import com.vallem.sylph.shared.data.dynamo.dto.User
import com.vallem.sylph.shared.data.remote.UserRemoteDataSource
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.UserDetails
import com.vallem.sylph.shared.domain.model.UserEventsMetaData
import com.vallem.sylph.shared.domain.repository.EventUserVotesRepository
import com.vallem.sylph.shared.domain.repository.EventsRepository
import com.vallem.sylph.shared.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDataSource: UserRemoteDataSource,
    private val votesRepository: EventUserVotesRepository,
    private val eventsRepository: EventsRepository
) : UserRepository {
    override suspend fun save(id: String, name: String, picUrl: String?) = try {
        userDataSource.save(User(name, picUrl, id))
            ?.let { Result.Success(Unit) }
            ?: Result.Failure(DynamoDbInstantiationException())
    } catch (e: Exception) {
        Result.Failure(e)
    }

    override suspend fun retrieveDetails(userId: String) = try {
        val userInfo = userDataSource.retrieveDetails(userId)
        val userVotes = votesRepository.retrieveVoteCountsForUserEvents(userId).getOrNull()
        val userEvents = eventsRepository.retrieveUserEvents(userId).getOrNull()

        if (userInfo == null || userEvents == null) Result.Failure(DynamoDbInstantiationException())
        else Result.Success(
            UserDetails(
                name = userInfo.name,
                picUrl = userInfo.picUrl,
                eventsMetaData = UserEventsMetaData(
                    totalPublishedEvents = userEvents.size,
                    eventsUpVotes = userVotes?.upVotes ?: 0,
                    eventsDownVotes = userVotes?.downVotes ?: 0,
                )
            )
        )
    } catch (e: Exception) {
        Result.Failure(e)
    }
}