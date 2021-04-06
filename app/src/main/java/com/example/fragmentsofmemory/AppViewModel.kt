package com.example.fragmentsofmemory
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fragmentsofmemory.Database.CategoryCardCount
import com.example.fragmentsofmemory.Database.DrawerItems
import com.example.fragmentsofmemory.Database.UserCard
import com.example.fragmentsofmemory.Database.UserInfo
import kotlinx.coroutines.*
import java.util.Date

class AppViewModel(application: Application): AndroidViewModel(application){

    private val db by lazy{AppDatabase.getDatabase(application, CoroutineScope(SupervisorJob()))}

    /**
     * get all information from database
     */
    var allCards: LiveData<List<UserCard>> = db.notes().getAll() // what the user has recorded
    var cardNum: LiveData<List<CategoryCardCount>> = db.notes().searchCardNum() // total number of cards per category
    var drawer: LiveData<List<DrawerItems>> = db.getDrawer().getAll() // get all categories
    var user: LiveData<UserInfo> = db.getUserInfo().getUser() // get user info name, signature etc..

    init {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun addDataBase(userContent: String, time:String, did:Int?) {
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

    fun updateCardMsg(id: Int, content:String, time:String, did:Int?) {
        viewModelScope.launch(Dispatchers.IO){
            db.notes().update(UserCard(id, did, content, time))
        }
    }

    fun updateLastSelected(id: Int, name:String, last:Int?, signature:String) {
        viewModelScope.launch(Dispatchers.IO){
            db.getUserInfo().update(UserInfo(id, name, last, signature))
        }
    }
}