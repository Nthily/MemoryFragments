package com.example.fragmentsofmemory

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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

    var allDrawerItems:LiveData<List<DrawerItems>> = db.getItems().getAll()

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

        }
    }


    fun AddDrawerItemsDatabase(itemsName: String) {
        viewModelScope.launch(Dispatchers.IO){
            val itemsObj = DrawerItems(0, itemsName)
            db.getItems().insert(itemsObj)
        }
    }
}