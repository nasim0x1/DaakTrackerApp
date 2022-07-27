package bd.smartpost.tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "international")
data class InternationalEvent(
    val tracking_id:String,
    var dateTime: String,
    val origin_country: String,
    val destination_country: String,
    val location: String,
    val status: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}