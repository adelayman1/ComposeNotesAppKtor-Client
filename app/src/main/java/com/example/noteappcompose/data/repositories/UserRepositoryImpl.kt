package com.example.noteappcompose.data.repositories

import android.util.Log
import com.example.noteappcompose.data.source.local.dataSource.UserLocalDataSource
import com.example.noteappcompose.data.source.remote.dataSource.UserRemoteDataSource
import com.example.noteappcompose.data.source.remote.requestModels.LoginRequestModel
import com.example.noteappcompose.data.source.remote.requestModels.RegisterRequestModel
import com.example.noteappcompose.data.utilities.handleError
import com.example.noteappcompose.domain.models.UserModel
import com.example.noteappcompose.domain.repositories.UserRepository
import io.ktor.client.features.ClientRequestException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import org.json.JSONObject
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    var userRemoteDataSource: UserRemoteDataSource,
    var userLocalDataSource: UserLocalDataSource,
    val externalScope: CoroutineScope
) : UserRepository {
    override suspend fun login(email: String, password: String): UserModel {
        return try {
            return externalScope.async {
                userRemoteDataSource.login(LoginRequestModel(email, password))
                    .also {
                        if (it.status && it.data != null) {
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
                        if (it.status && it.data != null) {
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