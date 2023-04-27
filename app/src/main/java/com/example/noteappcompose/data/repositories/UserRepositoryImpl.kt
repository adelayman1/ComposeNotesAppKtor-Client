package com.example.noteappcompose.data.repositories

import com.example.noteappcompose.data.source.local.dataSource.UserLocalDataSource
import com.example.noteappcompose.data.source.remote.dataSource.UserRemoteDataSource
import com.example.noteappcompose.data.source.remote.requestModels.LoginRequestModel
import com.example.noteappcompose.data.source.remote.requestModels.RegisterRequestModel
import com.example.noteappcompose.data.utilities.handleError
import com.example.noteappcompose.data.utilities.isDataHasGotSuccessfully
import com.example.noteappcompose.domain.models.UserModel
import com.example.noteappcompose.domain.repositories.UserRepository
import io.ktor.client.features.ClientRequestException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private var userRemoteDataSource: UserRemoteDataSource,
    private var userLocalDataSource: UserLocalDataSource,
    private val externalScope: CoroutineScope
) : UserRepository {
    override suspend fun login(email: String, password: String): UserModel {
        try {
            return externalScope.async {
                userRemoteDataSource.login(LoginRequestModel(email, password))
                    .also {
                        if (it.isDataHasGotSuccessfully()) {
                            userLocalDataSource.saveUserToken(it.data!!.userToken)
                        }
                    }
            }.await().data!!.toUserModel()
        } catch (e: ClientRequestException) {
            throw e.handleError()
        }
    }

    override suspend fun register(email: String, name: String, password: String): UserModel {
        return try {
            externalScope.async {
                userRemoteDataSource.register(RegisterRequestModel(name, email, password))
                    .also {
                        if (it.isDataHasGotSuccessfully()) {
                            userLocalDataSource.saveUserToken(it.data!!.userToken)
                        }
                    }
            }.await().data!!.toUserModel()
        } catch (e: ClientRequestException) {
            throw e.handleError()
        }
    }

    override suspend fun getUserToken(): String? {
        return userLocalDataSource.getUserToken()
    }
}