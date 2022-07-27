package bd.smartpost.tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "domestic")
data class DomesticEvent(
    val tracking_id: String,
    val date: String,
    val time: String,
    val bag_id: String,
    val branch_office: String,
    val status: String,
    val event: String,
    val type: String,
    val remark: String? = null
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
