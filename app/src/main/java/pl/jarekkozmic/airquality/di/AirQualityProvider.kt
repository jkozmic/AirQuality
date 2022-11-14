package pl.jarekkozmic.airquality.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import pl.jarekkozmic.airquality.data.AirlyStationDataSource
import pl.jarekkozmic.airquality.logic.FakeRemoteStationsRepository
import pl.jarekkozmic.airquality.logic.RemoteStationsRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// tak jak klasy configurationBean w javie, służy do tworzenia klas przy wstrzykiwaniu zależności
@Module
@InstallIn(SingletonComponent::class)
class AirQualityProvider {

    @Provides
    @Singleton
    fun provideRemoteStationsRepository(airlyService: AirlyStationDataSource.AirlyService) : RemoteStationsRepository{
        return AirlyStationDataSource(airlyService)
    }

    @Provides
    @Singleton
    fun provideAirlyAuthOkHttpClient(): OkHttpClient{
        return OkHttpClient
            .Builder()
            .addInterceptor(AirlyAuthInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit{
        return Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(AirlyStationDataSource.HOST)
            .build()
    }

    @Provides
    @Singleton
    fun provideAirlyService() : AirlyStationDataSource.AirlyService{
        return Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(AirlyStationDataSource.HOST)
            .build()
            .create(AirlyStationDataSource.AirlyService::class.java)
    }
}

class AirlyAuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("apikey", "EcidBF6LdhzqVc37PmDt6TQyS0qRl0Vt")
        return chain.proceed(requestBuilder.build())
    }
}