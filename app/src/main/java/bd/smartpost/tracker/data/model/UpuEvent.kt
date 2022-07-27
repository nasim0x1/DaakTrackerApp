package bd.smartpost.tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "upu_activity")
data class UpuEvent (
    val tracking_id: String,
    val dateTime: String,
    val activity: String,
    val location: String,
    val remark: String? = null
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}