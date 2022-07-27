package bd.smartpost.tracker.data.repository

import android.util.Log
import bd.smartpost.tracker.data.model.Parcel
import bd.smartpost.tracker.data.source.local.AppDao
import bd.smartpost.tracker.data.source.local.AppDatabase
import bd.smartpost.tracker.data.source.remote.RemoteSource
import bd.smartpost.tracker.utils.ParcelState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddParcelRepository @Inject constructor(
    private var dao: AppDao
) {
    fun checkTrackingId(tracking_id:String) = dao.checkTrackingID(tracking_id) != 0
    fun addParcel(parcel: Parcel) = dao.addParcel(parcel)

    fun fetchData(parcel: Parcel){
        CoroutineScope(Dispatchers.IO).launch {
            delay(5000L)
            val nwe = parcel.copy(
                state = ParcelState.Updated,
                lastUpdate = "Departed from outward of exchange Departed from outward of exchange Departed from outward of exchange"
            )
            Log.d("sdlkfjdkslf","updated")
            dao.updateParcel(nwe)
        }
    }

}