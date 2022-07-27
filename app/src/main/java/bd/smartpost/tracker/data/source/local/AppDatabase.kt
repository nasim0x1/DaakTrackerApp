package bd.smartpost.tracker.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import bd.smartpost.tracker.data.model.*


@Database(
    entities = [Parcel::class, DomesticEvent::class, InternationalEvent::class, UpuEvent::class, UpuSummary::class],
    exportSchema = false,
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}