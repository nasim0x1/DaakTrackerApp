package bd.smartpost.tracker.di

import android.app.Application
import androidx.room.Room
import bd.smartpost.tracker.data.source.remote.DaakServices
import bd.smartpost.tracker.data.source.remote.UpuServices
import bd.smartpost.tracker.data.source.local.AppDatabase
import bd.smartpost.tracker.data.source.remote.RemoteSource
import bd.smartpost.tracker.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) =
        Room.databaseBuilder(app, AppDatabase::class.java, "daak_tracker_database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    @Provides
    @Singleton
    fun provideLocalSource(db: AppDatabase) = db.appDao()

    @Provides
    @Singleton
    fun provideDaakApi(): DaakServices = Retrofit.Builder()
        .baseUrl(Constants.daak_base_url)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(DaakServices::class.java)

    @Provides
    @Singleton
    fun provideUpuApi(): UpuServices = Retrofit.Builder()
        .baseUrl(Constants.upu_base_url)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(UpuServices::class.java)

    @Provides
    @Singleton
    fun provideRemoteSource(
        upuServices: UpuServices,
        daakServices: DaakServices
    ) = RemoteSource(upuServices, daakServices)


    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope