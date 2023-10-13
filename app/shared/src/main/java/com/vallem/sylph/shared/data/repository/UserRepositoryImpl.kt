package com.vallem.sylph.shared.data.repository

import com.vallem.componentlibrary.domain.model.UserInfo
import com.vallem.sylph.shared.data.dynamo.dto.User
import com.vallem.sylph.shared.data.entity.UserInfoEntity
import com.vallem.sylph.shared.data.local.UserInfoDao
import com.vallem.sylph.shared.data.mapper.toDomain
import com.vallem.sylph.shared.data.mapper.toEntity
import com.vallem.sylph.shared.data.remote.UserRemoteDataSource
import com.vallem.sylph.shared.domain.exception.UserNotFoundException
import com.vallem.sylph.shared.domain.exception.UserSavingException
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.UserDetails
import com.vallem.sylph.shared.domain.model.UserEventsMetaData
import com.vallem.sylph.shared.domain.repository.EventUserVotesRepository
import com.vallem.sylph.shared.domain.repository.EventsRepository
import com.vallem.sylph.shared.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDataSource: UserRemoteDataSource,
    private val votesRepository: EventUserVotesRepository,
    private val eventsRepository: EventsRepository,
    private val userInfoDao: UserInfoDao,
) : UserRepository {
    override suspend fun save(id: String, name: String, picture: String?) = try {
        userDataSource.save(User(name, picture, id))
            ?.let {
                userInfoDao.insert(UserInfoEntity(name, picture, id))
                Result.Success(Unit)
            } ?: Result.Failure(UserSavingException())
    } catch (e: Exception) {
        Result.Failure(e)
    }

    override suspend fun retrieveUserInfo(userId: String) = try {
        val local = userInfoDao.getUser(userId)

        if (local != null) Result.Success(local.toDomain())
        else {
            val remote = userDataSource.retrieveDetails(userId)

            if (remote == null) Result.Failure(UserNotFoundException())
            else {
                val userInfo = UserInfo(remote.name, remote.picture)
                userInfoDao.insert(userInfo.toEntity(userId))
                Result.Success(userInfo)
            }
        }
    } catch (e: Exception) {
        Result.Failure(e)
    }

    override suspend fun retrieveDetails(userId: String) = try {
        val userInfo = retrieveUserInfo(userId).getOrNull()

        if (userInfo == null) Result.Failure(UserNotFoundException())
        else {
            val userEvents = eventsRepository.retrieveUserEvents(userId).getOrNull()
            val userVotes = votesRepository.retrieveVoteCountsForUserEvents(userId).getOrNull()

            Result.Success(
                UserDetails(
                    name = userInfo.name,
                    picUrl = userInfo.picture,
                    eventsMetaData = UserEventsMetaData(
                        totalPublishedEvents = userEvents?.size ?: 0,
                        eventsUpVotes = userVotes?.upVotes ?: 0,
                        eventsDownVotes = userVotes?.downVotes ?: 0,
                    )
                )
            )
        }
    } catch (e: Exception) {
        Result.Failure(e)
    }

    override suspend fun clearUserInfo() = try {
        userInfoDao.deleteAll()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Failure(e)
    }
}