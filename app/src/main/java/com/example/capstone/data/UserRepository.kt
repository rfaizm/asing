package com.example.capstone.data

import androidx.lifecycle.liveData
import com.example.capstone.data.api.config.ApiService
import com.example.capstone.data.api.response.RegisterResponse
import com.example.capstone.data.pref.UserModel
import com.example.capstone.data.pref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService, private val userPreference: UserPreference
) {
    fun register(email : String, password : String, name : String, height : Float, weight : Float, age : Int, circleHand : Float) = liveData {
        emit(ResultState.Loading)

        try {
            val successResponse = apiService.register(email, password, name, height, weight, age, circleHand)
            emit(ResultState.Success(successResponse))
        } catch (e : HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(ResultState.Error(errorResponse.message!!))
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)

        try {
            val successResponse = apiService.login(email, password)
            val userIdBody = successResponse.loginResult?.userId.toString()
            val photoUrlBody = successResponse.loginResult?.photoUrl.toString()
            val nameBody = successResponse.loginResult?.fullName.toString()
            val heightBody = successResponse.loginResult?.heightCm
            val weightBody = successResponse.loginResult?.weightKg
            val ageBody = successResponse.loginResult?.ageYears
            val handCircleBody = successResponse.loginResult?.armCircumferenceCm
            val tokenBody = successResponse.loginResult?.token.toString()
            saveSession(UserModel(userIdBody, photoUrlBody, nameBody, heightBody!!, weightBody!!, ageBody!!, handCircleBody!!, tokenBody))
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(ResultState.Error(errorResponse.message!!))
        }
    }

    private suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}