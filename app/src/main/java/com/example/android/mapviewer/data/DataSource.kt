package com.example.android.mapviewer.data

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.esri.arcgisruntime.portal.PortalItem
import com.example.android.mapviewer.PortalSearch

class DataSource(resources: Resources) {
    private val mapList = PortalSearch.getPortalItems()//mapList(resources)
    private val mapItemsLiveData = MutableLiveData(mapList)

   /* fun updateShoe(mapItem:MapItem){
        val currentList = mapItemsLiveData.value
        if(currentList != null) {
            val updatedList = currentList.toMutableList()
            //First, find the position of the shoe in the list
            val shoePosition = updatedList.indexOfFirst {
                it.id == mapItem.id

            }


            //Now get your shoe by position and make changes
            val updatedShoe = updatedList[shoePosition].apply {
                //Make all changes you need here
                name = shoe.name
                companyName = shoe.companyName
                size = shoe.size
                description = shoe.description

                //...
            }

            //Finally, replace updated video into your list.
            updatedList[shoePosition] = updatedShoe
            shoesLiveData.postValue(updatedList)
        }
    }
*/
    fun addPortalItem(mapItem: PortalItem) {
        val currentList = mapItemsLiveData.value
        if (currentList == null) {
            mapItemsLiveData.postValue(listOf(mapItem))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, mapItem)
            mapItemsLiveData.postValue(updatedList)
        }
    }
    /* Returns flower given an ID. */
   /* fun getShoeForId(id: Long): Shoe? {
        shoesLiveData.value?.let { shoeList ->
            return shoeList.firstOrNull{ it.id == id}
        }
        return null
    }*/

    /* Returns a random flower asset for flowers that are added. */
  /*  fun getRandomShoeImageAsset(): Int? {
        val randomNumber = (shoeList.indices).random()
        return shoeList[randomNumber].image
    }
*/

    fun getPortalUrl():String{
        return PortalSearch.getPortalUrl()
    }
    fun getMapList(): MutableLiveData<List<PortalItem>?> {
        return mapItemsLiveData
    }
    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(resources: Resources): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}