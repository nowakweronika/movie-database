package com.example.moviedatabase.di

import com.example.moviedatabase.BuildConfig
import com.example.moviedatabase.data.MovieDatabaseApi
import com.example.moviedatabase.data.MovieDatabaseRepositoryImpl
import com.example.moviedatabase.domain.MovieDatabaseRepository
import com.example.moviedatabase.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideLnoApi(okHttpClient: OkHttpClient): MovieDatabaseApi =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MovieDatabaseApi::class.java)

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        )
        .addInterceptor(
            Interceptor { chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer ${BuildConfig.TMDB_API_KEY}")
                    .build()
                chain.proceed(request)
            }
        ).build()

    @Provides
    @Singleton
    fun provideMovieDatabaseRepository(api: MovieDatabaseApi): MovieDatabaseRepository =
        MovieDatabaseRepositoryImpl(api)
}