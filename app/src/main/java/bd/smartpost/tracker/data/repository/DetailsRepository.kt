package bd.smartpost.tracker.data.repository

import bd.smartpost.tracker.data.source.local.AppDatabase
import bd.smartpost.tracker.data.source.remote.RemoteSource
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val db:AppDatabase,
    private val remoteSource: RemoteSource
) {

    private val dao = db.appDao()

    fun singleParcel(tracking_id:String) = dao.singleParcel(tracking_id)

}