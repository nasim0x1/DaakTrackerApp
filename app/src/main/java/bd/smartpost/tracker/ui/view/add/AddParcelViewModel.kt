package bd.smartpost.tracker.ui.view.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.smartpost.tracker.data.model.Parcel
import bd.smartpost.tracker.data.repository.AddParcelRepository
import bd.smartpost.tracker.utils.ParcelTypes
import bd.smartpost.tracker.utils.getCurrentDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class AddParcelViewModel @Inject constructor(
    private var repository: AddParcelRepository
) : ViewModel() {

    private var addParcelEventChannel = Channel<AddParcelEvents>()
    val events get() = addParcelEventChannel.receiveAsFlow()

    fun addNewParcel(title: String, tracking_id: String, type: ParcelTypes) = viewModelScope.launch{
        if (trackingIdValidator(tracking_id)) {
            val parcel = Parcel(
                tracking_id = tracking_id,
                title = title,
                type = type,
                lastUpdateAt = getCurrentDateTime()
            )
            repository.addParcel(parcel)
            repository.fetchData(parcel)
            addParcelEventChannel.send(AddParcelEvents.OnParcelAddSuccessfully(tracking_id))
        }
    }

    private suspend fun trackingIdValidator(tracking_id: String): Boolean {
        if (tracking_id.isEmpty()){
            addParcelEventChannel.send(AddParcelEvents.OnInvalidTrackingId(TrackingIdError.Empty))
            return false
        }
        if (tracking_id.length != 13){
            addParcelEventChannel.send(AddParcelEvents.OnInvalidTrackingId(TrackingIdError.InvalidLength))
            return false
        }
        if (!tracking_id.matches("[A-Z]{2}[\\w]{3,9}[A-Z]{2}".toRegex())){
            addParcelEventChannel.send(AddParcelEvents.OnInvalidTrackingId(TrackingIdError.InvalidFormat))
            return false
        }
        if (repository.checkTrackingId(tracking_id)){
            addParcelEventChannel.send(AddParcelEvents.OnInvalidTrackingId(TrackingIdError.Exist))
            return false
        }
        return true
    }


    enum class TrackingIdError {
        Empty, InvalidLength, InvalidFormat, Exist
    }

    sealed class AddParcelEvents {
        data class OnInvalidTrackingId(val error: TrackingIdError) : AddParcelEvents()
        data class OnParcelAddSuccessfully(val tracking_id: String) : AddParcelEvents()
    }

}