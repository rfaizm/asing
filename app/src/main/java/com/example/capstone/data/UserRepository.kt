package com.example.capstone.data

import androidx.lifecycle.liveData
import com.example.capstone.data.api.config.ApiService
import com.example.capstone.data.api.response.LoginResponse
import com.example.capstone.data.api.response.LogoutResponse
import com.example.capstone.data.api.response.NutriotionResponse
import com.example.capstone.data.api.response.PredictResponse
import com.example.capstone.data.api.response.ProfileResponse
import com.example.capstone.data.api.response.RegisterResponse
import com.example.capstone.data.pref.UpdateModel
import com.example.capstone.data.pref.UserModel
import com.example.capstone.data.pref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

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
            val emailBody = successResponse.loginResult?.email.toString()
            val nameBody = successResponse.loginResult?.fullName.toString()
            val heightBody = successResponse.loginResult?.heightCm
            val weightBody = successResponse.loginResult?.weightKg
            val ageBody = successResponse.loginResult?.ageYears
            val handCircleBody = successResponse.loginResult?.armCircumferenceCm
            val tokenBody = successResponse.loginResult?.token.toString()
            saveSession(UserModel(userIdBody, photoUrlBody, emailBody, nameBody, heightBody!!, weightBody!!, ageBody!!, handCircleBody!!, tokenBody))
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message!!))
        }
    }

    fun logoutSession() = liveData {
        emit(ResultState.Loading)

        try {
            val token = userPreference.getAuthToken()
            val successResponse = apiService.logout(token = "Bearer $token")
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LogoutResponse::class.java)
            emit(ResultState.Error(errorResponse.message!!))
        }
    }

    fun updateProfile(name : String, height : Float, weight : Float, age : Int, circleHand : Float) = liveData {
        emit(ResultState.Loading)

        try {
            val token = userPreference.getAuthToken()
            val successResponse = apiService.profile(token = "Bearer $token", name, weight, age, circleHand, height)
            updateSession(UpdateModel(successResponse.result?.fullName!!, successResponse.result.heightCm!!,
                successResponse.result.weightKg!!, successResponse.result.ageYears!!, successResponse.result.armCircumferenceCm!!)
            )
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ProfileResponse::class.java)
            emit(ResultState.Error(errorResponse.status!!))
        }
    }

    fun uploadImage(imageFile : File) = liveData {
        emit(ResultState.Loading)

        try {
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )
            val token = userPreference.getAuthToken()
            val successResponse = apiService.uploadImage(token = "Bearer $token", multipartBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PredictResponse::class.java)
            emit(ResultState.Error(errorResponse.status!!))
        }
    }

    fun getNutrition(name : String) = liveData {
        emit(ResultState.Loading)

        try {
            val token = userPreference.getAuthToken()
            val successResponse = apiService.getNutrition(token = "Bearer $token", name = name)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, NutriotionResponse::class.java)
            emit(ResultState.Error(errorResponse.status!!))
        }
    }

    private suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    private suspend fun updateSession(user : UpdateModel) {
        userPreference.updateSession(user)
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