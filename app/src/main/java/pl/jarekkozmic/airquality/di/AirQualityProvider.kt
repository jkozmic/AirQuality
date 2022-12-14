package pl.jarekkozmic.airquality.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import pl.jarekkozmic.airquality.data.AirlyStationDataSource
import pl.jarekkozmic.airquality.data.airly.AirlyEndpoint
import pl.jarekkozmic.airquality.data.airly.AirlyService
import pl.jarekkozmic.airquality.data.local.InMemoryStationsRepository
import pl.jarekkozmic.airquality.data.local.db.AppDatabase
import pl.jarekkozmic.airquality.data.local.db.DatabaseStationsRepository
import pl.jarekkozmic.airquality.logic.repository.LocalStationsRepository
import pl.jarekkozmic.airquality.logic.repository.RemoteStationsRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// tak jak klasy configurationBean w javie, służy do tworzenia klas przy wstrzykiwaniu zależności
@Module
@InstallIn(SingletonComponent::class)
class AirQualityProvider {

    @Provides
    @Singleton
    fun provideRemoteStationsRepository(airlyService: AirlyService) : RemoteStationsRepository {
        return AirlyStationDataSource(airlyService)
    }

    @Provides
    @Singleton
    fun provideLocalStationsRepository(@ApplicationContext appContext: Context) : LocalStationsRepository{
        val dataBase = Room.databaseBuilder(appContext, AppDatabase::class.java, "AirQualityDb").build()
        return DatabaseStationsRepository(dataBase)
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
    fun provideRetrofit(okHttpClient: OkHttpClient)
    : Retrofit {
        return Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(AirlyEndpoint.HOST)
            .build()
    }

    @Provides
    @Singleton
    fun provideAirlyService(retrofit: Retrofit) : AirlyService{
        return retrofit.create(AirlyService::class.java)
    }
}

class AirlyAuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("apikey", "YOUR_KEY")
        return chain.proceed(requestBuilder.build())
    }
}