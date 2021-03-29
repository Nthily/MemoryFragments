package com.example.fragmentsofmemory
import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.fragmentsofmemory.Database.CategoryCardCount
import com.example.fragmentsofmemory.Database.DrawerItems
import com.example.fragmentsofmemory.Database.UserCard
import kotlinx.coroutines.*


class UserCardViewModel(application: Application): AndroidViewModel(application){

    /*
    val db = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java, "database-cardContent"
    ).build()*/

    private val db by lazy{AppDatabase.getDatabase(application, CoroutineScope(SupervisorJob()))}

    var allCards: LiveData<List<UserCard>> = db.notes().getAll()
    var cardNum: LiveData<List<CategoryCardCount>> = db.notes().searchCardNum()

    var drawer: LiveData<List<DrawerItems>> = db.getDrawer().getAll()
//    var allDrawerItems by mutableStateOf(listOf<DrawerCard>())
    /*
    init {
        GlobalScope.launch {
            val items = db.notes().getAll()
        //    val drawerItems = db.notes().getAllDrawerItems()
            viewModelScope.launch() {
                allCards = items
         //       allDrawerItems = drawerItems
            }
        }
    }*/

    init {
        viewModelScope.launch(Dispatchers.IO) {
         //   allCards = db.notes().getAll()
        }
    }

    fun addDataBase(userContent: String, time:String, did:Int) {
        viewModelScope.launch(Dispatchers.IO){
           val cardObj = UserCard(0, did, userContent, time)
            db.notes().insert(cardObj)
        }
    }

    fun addCategoryDataBase(name:String) {
        viewModelScope.launch(Dispatchers.IO){
            db.getDrawer().insert(DrawerItems(0, name))
        }
    }

    fun deleteCategoryDataBase(uid:Int) {
        viewModelScope.launch(Dispatchers.IO){
            db.getDrawer().delete(uid)
        }
    }

    fun updateCategoryDataBaseName(uid:Int, name:String) {
        viewModelScope.launch(Dispatchers.IO){
            db.getDrawer().update(DrawerItems(uid, name))
        }
    }

    fun removeDataBase(id:Int) {
        viewModelScope.launch(Dispatchers.IO){
            db.notes().delete(id)
        }
    }

    fun UpdateCardMsg(id: Int, content:String, time:String, did:Int) {
        viewModelScope.launch(Dispatchers.IO){
            db.notes().update(UserCard(id, did, content, time))
        }
    }

}