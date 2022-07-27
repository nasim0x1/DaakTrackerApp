package bd.smartpost.tracker.ui.view.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import bd.smartpost.tracker.data.model.Parcel
import bd.smartpost.tracker.data.repository.DetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: DetailsRepository
): ViewModel() {

    fun getCurrentParcelUpdate(tracking_id:String) = repository.singleParcel(tracking_id)?.asLiveData()

    fun update(parcel: Parcel) {}

}