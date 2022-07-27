package bd.smartpost.tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "upu_summary")
data class UpuSummary(
    val tracking_id: String,
    val origin: String,
    val destination: String,
    val status: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}