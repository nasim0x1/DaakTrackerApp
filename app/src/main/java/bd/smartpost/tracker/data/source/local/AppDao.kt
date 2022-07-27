package bd.smartpost.tracker.data.source.local

import androidx.room.*
import bd.smartpost.tracker.data.model.Parcel
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addParcel(parcel: Parcel)

    @Update
    fun updateParcel(parcel: Parcel)

    @Delete
    fun deleteParcel(parcel: Parcel)

    @Query("SELECT * FROM parcels WHERE tracking_id = :tracking_id")
    fun singleParcel(tracking_id: String):Flow<Parcel>?

    @Query("SELECT COUNT(*) FROM parcels WHERE tracking_id = :tracking_id")
    fun checkTrackingID(tracking_id: String):Int

    @Query("SELECT * FROM parcels WHERE isActive = 1 ORDER BY id DESC")
    fun getActiveParcels(): Flow<List<Parcel>>

    @Query("SELECT * FROM parcels WHERE isActive = 0 ORDER BY id DESC")
    fun getArchiveParcels(): Flow<List<Parcel>>
}