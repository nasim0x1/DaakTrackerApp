package bd.smartpost.tracker.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import bd.smartpost.tracker.utils.ParcelState
import bd.smartpost.tracker.utils.ParcelTypes
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "parcels")
@Parcelize
data class Parcel(
    val tracking_id: String,
    val title: String? = null,
    val type: ParcelTypes,
    val state: ParcelState = ParcelState.Loading,
    val isActive: Boolean = true,

    val hasUpdate: Boolean = false,
    val currentEvents: Int = 0,
    val lastUpdate: String? = null,
    val lastUpdateAt: String? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Long = Date().time
) : Parcelable {

    fun compare(other: Parcel): Boolean {
        if (other.title != this.title) return false
        if (other.type != this.type) return false
        if (other.state != this.state) return false
        return true
    }
}