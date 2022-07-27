package bd.smartpost.tracker.ui.view.options

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.smartpost.tracker.data.model.Parcel
import bd.smartpost.tracker.data.repository.OptionsRepository
import bd.smartpost.tracker.data.source.local.AppDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParcelOptionsViewModel @Inject constructor(
    private val dao: AppDao
) :ViewModel(){

    private var optionsEventsChannel = Channel<ParcelOptionEvents>()
    val events get() = optionsEventsChannel.receiveAsFlow()

    fun update(parcel: Parcel){

    }

    fun rename(parcel: Parcel,newTitle:String) = viewModelScope.launch{
        val new = parcel.copy(title = newTitle)
        dao.updateParcel(new).let {
            optionsEventsChannel.send(ParcelOptionEvents.OnRenamed(parcel.tracking_id,newTitle))
        }
    }

    fun moveToArchive(parcel: Parcel) = viewModelScope.launch {
        val new = parcel.copy(isActive = false)
        dao.updateParcel(new)
        optionsEventsChannel.send(ParcelOptionEvents.OnArchived(parcel.tracking_id))
    }

    fun restoreFromArchive(parcel: Parcel) = viewModelScope.launch {
        val new = parcel.copy(isActive = true)
        dao.updateParcel(new).let {
            optionsEventsChannel.send(ParcelOptionEvents.OnRestored(parcel.tracking_id))

        }
    }
    fun delete(parcel: Parcel)= viewModelScope.launch {
        optionsEventsChannel.send(ParcelOptionEvents.DeleteConformation(parcel))
    }

    fun confirmDelete(parcel: Parcel) = viewModelScope.launch {
        dao.deleteParcel(parcel).let {
            optionsEventsChannel.send(ParcelOptionEvents.OnDeleted(parcel.tracking_id))
        }
    }

    sealed class ParcelOptionEvents(){
        data class OnRenamed(val tracking_id:String,val title:String):ParcelOptionEvents()
        data class OnDeleted(val tracking_id:String):ParcelOptionEvents()
        data class DeleteConformation(val parcel: Parcel):ParcelOptionEvents()
        data class OnArchived(val tracking_id:String):ParcelOptionEvents()
        data class OnRestored(val tracking_id:String):ParcelOptionEvents()
    }
}