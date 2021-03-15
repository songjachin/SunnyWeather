package com.songjachin.weather.ui.place

import android.app.DownloadManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.songjachin.weather.logic.Repository
import com.songjachin.weather.logic.model.Place

/**
 * Created by matthew
 *
 *
 */
class PlaceViewModel : ViewModel() {

    /**
     * when liveData change ,it will notify to its observer
     */
    //设置这个数据，switchmap将一个livedata转换成另一个可观察的数据
    private val searchLiveData = MutableLiveData<String>()

    //这个是设置给Rv的adapter,设置的places在activity/fragment通过观察所得
    val placeList = ArrayList<Place>()

    //外部观察的数据只有这一个
    val placeLiveData :LiveData<Result<List<Place>>> = Transformations.switchMap(searchLiveData){
            //switchMap去观察searchLiveData，然后去Repository获取更多的数据
        //String--->Result<List<Place>>
                query -> Repository.searchPlaces(query)
        }

    //这个方法不可取，如果在viewModel里观察，它观察的是返回值，但searchPlaces方法得到的是新的值，而上面得到的placeLiveData是通过转换得到的新值

    //fun searchPlace(query: String): LiveData<Result<List<Place>>> {
      //  return Repository.searchPlaces(query)
    //}

    //外面调用这个方法，将改变searchLiveData的值，设置这一个数据，通过transformation即可得到一串数据
    fun searchPlaces(query: String){
        //外面调用，对searchLiveData改变，然后本层的placeLiveData改变，外面再去观察placeLiveData
        searchLiveData.value = query
    }


    fun savePlace(place: Place) = Repository.savePlace(place)

    fun getSavePlace() = Repository.getSavePlace()//Place

    fun isPlaceSaved() = Repository.isPlaceSaved()//Boolean
}