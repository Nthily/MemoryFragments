package com.example.fragmentsofmemory

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fragmentsofmemory.Database.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserInfoViewModel(application: Application): AndroidViewModel(application){

    /*
    val userdb = Room.databaseBuilder(
        application.applicationContext,
        UserInfoDataBase::class.java, "database-userInfo"
    ).build()*/

    private val userdb by lazy{UserInfoDataBase.getDatabase(application)}


    var userInfo = MutableLiveData(listOf<UserInfo>())


    init {
        viewModelScope.launch(Dispatchers.IO){
            val items = userdb.getInfo().getAll()
            viewModelScope.launch(Dispatchers.Main) {
                userInfo.value = items
            }
            initUserInfo()
        }
    }

    private fun initUserInfo() {
        val cardObj = UserInfo((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), "Nthily", R.drawable.qq20210315211722)
        if(userInfo.value?.isEmpty() == true){
            viewModelScope.launch(Dispatchers.Main) {
                userInfo.value = listOf(cardObj)
            }
            viewModelScope.launch(Dispatchers.IO) {
                userdb.getInfo().insert(cardObj)
            }
        }
    }
}