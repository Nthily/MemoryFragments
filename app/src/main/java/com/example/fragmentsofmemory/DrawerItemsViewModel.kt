package com.example.fragmentsofmemory

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.fragmentsofmemory.Database.DrawerItems
import com.example.fragmentsofmemory.Database.UserCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DrawerItemsViewModel(application: Application): AndroidViewModel(application){

    /*
    val dbs = Room.databaseBuilder(
        application.applicationContext,
        DrawerItemsDataBase::class.java, "database-DrawerItems"
    ).build()*/

    private val db by lazy{DrawerItemsDataBase.getDatabase(application)}

    var allDrawerItems = MutableLiveData(listOf<DrawerItems>())

    /*
    init {
        GlobalScope.launch {
            val items = dbs.getItems().getAll()

            viewModelScope.launch {
                allDrawerItems = items
            }
        }
    }*/

    init {
        viewModelScope.launch(Dispatchers.IO){
            val items = db.getItems().getAll()
            viewModelScope.launch(Dispatchers.Main){
                allDrawerItems.value = items
            }
        }
    }

    fun AddDrawerItemsDatabase(itemsName: String) {
        viewModelScope.launch(Dispatchers.IO){
            val itemsObj = DrawerItems((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), itemsName, (allDrawerItems.value?.size
                ?: 0) + 1)
            viewModelScope.launch(Dispatchers.Main) {
                allDrawerItems.value = allDrawerItems.value?.plus(listOf(itemsObj))
            }
            db.getItems().insert(itemsObj)
        }
    }
}