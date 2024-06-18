package com.example.capstone.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.capstone.data.api.config.ApiService
import com.example.capstone.data.api.response.Data
import com.example.capstone.data.api.response.LoginResponse
import com.example.capstone.data.api.response.LogoutResponse
import com.example.capstone.data.api.response.NutriotionResponse
import com.example.capstone.data.api.response.PredictResponse
import com.example.capstone.data.api.response.ProfileResponse
import com.example.capstone.data.api.response.RegisterResponse
import com.example.capstone.data.api.response.TipsResponse
import com.example.capstone.data.local.entity.AnalyzeHistory
import com.example.capstone.data.local.room.AnalyzeHistoryDao
import com.example.capstone.data.pref.UpdateModel
import com.example.capstone.data.pref.UserModel
import com.example.capstone.data.pref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val apiService: ApiService, private val userPreference: UserPreference, private val analyzeHistoryDao: AnalyzeHistoryDao
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

    suspend fun getAllTips() = liveData(Dispatchers.IO) {
        emit(ResultState.Loading)
        try {
            val token = userPreference.getAuthToken()
            val response = apiService.getAllTips(token = "Bearer $token").execute()
            if (response.isSuccessful) {
                val data = response.body()?.data
                if (data != null) {
                    emit(ResultState.Success(data))
                } else {
                    emit(ResultState.Error("Error: Data is null"))
                }
            } else {
                emit(ResultState.Error("Error: Response unsuccessful"))
                Log.e("API_ERROR", "Code: ${response.code()}, Message: ${response.message()}")
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, TipsResponse::class.java)
            emit(ResultState.Error(errorResponse.status ?: "Unknown Error"))
        }
    }

    suspend fun getDetailTips(tipId: String): LiveData<ResultState<Data>> = liveData {
        emit(ResultState.Loading)
        try {
            val token = userPreference.getAuthToken()
            Log.d("UserRepository", "Token: $token, Tip ID: $tipId")

            val response = withContext(Dispatchers.IO) {
                apiService.getDetailTips("Bearer $token", tipId).execute()
            }

            if (response.isSuccessful) {
                val detailTipsResponse = response.body()
                val data = detailTipsResponse?.data
                Log.d("UserRepository", "Response Data: $data")
                if (data != null) {
                    emit(ResultState.Success(data))
                } else {
                    emit(ResultState.Error("Error: Data is null"))
                }
            } else {
                emit(ResultState.Error("Error: Response unsuccessful"))
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception: ${e.message}")
            emit(ResultState.Error(e.message ?: "Unknown Error"))
        }
    }

    fun insertAnalyzeHistory(history: AnalyzeHistory) = liveData {
        emit(ResultState.Loading)
        try {
            analyzeHistoryDao.insert(history)
            emit(ResultState.Success(Unit))
        } catch (e: Exception) {
            emit(ResultState.Error(e.localizedMessage ?: "Unknown Error"))
        }
    }

    fun getAllAnalyses() = liveData {
        emit(ResultState.Loading)
        try {
            val data = analyzeHistoryDao.getAllAnalyses().value
            if (data != null) {
                emit(ResultState.Success(data))
            } else {
                emit(ResultState.Error("No data found"))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.localizedMessage ?: "Unknown Error"))
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
        fun getInstance(apiService: ApiService, userPreference: UserPreference, analyzeHistoryDao: AnalyzeHistoryDao) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference, analyzeHistoryDao)
            }.also { instance = it }
    }
}