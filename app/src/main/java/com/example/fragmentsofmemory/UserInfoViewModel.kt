package com.example.fragmentsofmemory

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fragmentsofmemory.Database.DrawerItems
import com.example.fragmentsofmemory.Database.UserInfo
import kotlinx.coroutines.*

class UserInfoViewModel(application: Application): AndroidViewModel(application){

    /*
    val userdb = Room.databaseBuilder(
        application.applicationContext,
        UserInfoDataBase::class.java, "database-userInfo"
    ).build()*/

    private val userdb by lazy{UserInfoDataBase.getDatabase(application, CoroutineScope(SupervisorJob()))}


    var userInfo: LiveData<List<UserInfo>> = userdb.getInfo().getAll()


    init {
        viewModelScope.launch(Dispatchers.IO){

        }
    }
}