package bd.smartpost.tracker.data.repository

import androidx.room.Insert
import bd.smartpost.tracker.data.source.local.AppDao
import javax.inject.Inject

class OptionsRepository @Inject constructor(
    private val dao: AppDao
) {
}