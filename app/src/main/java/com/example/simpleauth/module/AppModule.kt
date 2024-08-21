package com.example.simpleauth.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.simpleauth.auth.data.repository.AuthRepository
import com.example.simpleauth.auth.data.repository.AuthRepositoryImpl
import com.example.simpleauth.auth.data.repository.AuthPreferencesRepository
import com.example.simpleauth.service.AuthService
import com.example.simpleauth.service.TodoService
import com.example.simpleauth.todo.data.repository.TodoRepository
import com.example.simpleauth.todo.data.repository.TodoRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val AUTH_PREFERENCE_NAME = "auth_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = AUTH_PREFERENCE_NAME
)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    fun provideBaseUrl():  String = "http://192.168.1.18/app-crud-mobile/public/api/"

    @Provides
    fun providesClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, client: OkHttpClient) : Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun providesTodoApiService(retrofit: Retrofit) : TodoService = retrofit.create(TodoService::class.java)

    @Provides
    @Singleton
    fun providesAuthPreferences(@ApplicationContext applicationContext: Context) : AuthPreferencesRepository = AuthPreferencesRepository(dataStore = applicationContext.dataStore)

    @Provides
    @Singleton
    fun provideAuthRepositoryImpl(authService: AuthService,authPreferencesRepository: AuthPreferencesRepository) : AuthRepository = AuthRepositoryImpl(authService,authPreferencesRepository)

    @Provides
    @Singleton
    fun provideTodoRepositoryImpl(todoService: TodoService,authPreferencesRepository: AuthPreferencesRepository) : TodoRepository = TodoRepositoryImpl(todoService,authPreferencesRepository)
}