package nu.veberod.healthmonitor.presentation.data

import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SensorDataViewModel : ViewModel() {

    private val _data: MutableLiveData<Float> = MutableLiveData()

    val data: LiveData<Float> get() = _data


}