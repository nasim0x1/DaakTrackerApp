package bd.smartpost.tracker.ui.adapter

import androidx.recyclerview.widget.ListAdapter

data class DomesticAdapter(
    val tracking_id: String,
    val date: String,
    val time: String,
    val bag_id: String,
    val branch_office: String,
    val status: String,
    val event: String,
    val type: String,
    val remark: String? = null
)