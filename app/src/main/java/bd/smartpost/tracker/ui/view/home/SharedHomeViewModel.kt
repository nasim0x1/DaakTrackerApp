package bd.smartpost.tracker.ui.view.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import bd.smartpost.tracker.data.source.local.AppDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedHomeViewModel @Inject constructor(val dao: AppDao):ViewModel() {

    var lastParcelsSize = 0

    private val homeEventsChannel = Channel<HomeEvents>()
    val events get() = homeEventsChannel.receiveAsFlow()

    val getActiveParcels = dao.getActiveParcels().asLiveData()
    val getArchiveParcels = dao.getArchiveParcels().asLiveData()

    fun updateAll() = viewModelScope.launch {
        getActiveParcels.value?.forEach {
            delay(1000L)
            Log.d("testing",it.toString())
        }
        homeEventsChannel.send(HomeEvents.OnUpdateCompleted(""))
    }

    sealed class HomeEvents(){
        data class OnUpdateCompleted(val temp:String): HomeEvents()
    }

}