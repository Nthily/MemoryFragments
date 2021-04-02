package com.example.fragmentsofmemory

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.util.Log
import androidx.activity.compose.registerForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.fragmentsofmemory.ui.theme.MyTheme
import androidx.activity.compose.registerForActivityResult
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.google.accompanist.coil.CoilImage
import java.io.File

class UiModel: ViewModel(){
   // var theme by mutableStateOf(MyTheme.Theme.Dark)
    var lightTheme by mutableStateOf(true)
    //  var currentPage: AddNewTreeHoles? by mutableStateOf(null)
    var maining by mutableStateOf(true)     // 检测是否为主界面,否则在其他界面回触发主界面的控件

    var adding by mutableStateOf(false)     // 启动添加界面

    var draweringPage by mutableStateOf(false) // 判断是否打开了汉堡菜单
    var requestCloseDrawerPage by mutableStateOf(false) // 请求关闭汉堡菜单

    var hasAnyExtraButtonRevealed by mutableStateOf(false)

    var reading by mutableStateOf(false) // 是否在阅读卡片详细内容

    var testTxt by mutableStateOf("") // 利用 testText 来传递卡片的内容

    val iconSize = Modifier.size(18.dp)
    var timing by mutableStateOf(false) // 是否打开时间选择器
    var timeResult by mutableStateOf("") // 时间结果

    var selectedTime by mutableStateOf(false)

    var cardId by mutableStateOf(0)

    var textModify by mutableStateOf("")
    var editing by mutableStateOf(false)

    var addNewCategory by mutableStateOf(false)
    var categoryName by mutableStateOf("")

    var requestCloseDrawer by mutableStateOf(false)


    var editingCategory by mutableStateOf(false) //启用编辑分类状态
    var editingCategoryUid by mutableStateOf(0)


    val imageUriState = mutableStateOf<Uri?>(null)

    var userAvatar by mutableStateOf(Uri.EMPTY)


    fun endReading() {
        reading = false
        maining = true
        cardId = 0
        timeResult = ""
    }



    @Composable
    fun SetBackground(background: Int): Unit{
        return Image(painter = painterResource(id = background), contentDescription = null,
            modifier = Modifier
            .fillMaxSize(),
            contentScale = ContentScale.Crop)
    }

    @Composable
    fun SetSecBackground(background: Int): Unit{
        return Image(painter = painterResource(id = background), contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.7f),
            contentScale = ContentScale.Crop)
    }


    fun startEdit(cardID:Int, userContent:String, time:String) {
        cardId = cardID
        editing = true
        adding = true
        maining = false
        textModify = userContent
        timeResult = time
    }

    fun endAddPage() {
        adding = false
        maining = true
        selectedTime = false
        timeResult = ""
        editing = false
        textModify = ""
    }


    fun deleteCard(userCardViewModel: UserCardViewModel) {
        userCardViewModel.removeDataBase(cardId)
        reading = false
        maining = true
        timeResult = ""
        textModify = ""
    }


    fun closeDrawerContent() {
        requestCloseDrawerPage = true // 关闭 Drawer
        requestCloseDrawer = !requestCloseDrawer  //收起 DrawerItems 编辑和删除按钮
    }

    @Composable
    fun InitUserProfilePic(file:File, context: Context) {
        /*if(file.exists()){
            CoilImage(Uri.fromFile(file), null, fadeIn = true)
        } else {
            Image(painter = painterResource(id = R.drawable.qq20210315211722), contentDescription = null)
        }*/
        if(userAvatar.equals(Uri.EMPTY) && file.exists()) {
            userAvatar = Uri.fromFile(file)
        }

        if(!userAvatar.equals(Uri.EMPTY)) {
            CoilImage(userAvatar, null)
        } else {
            Image(painter = painterResource(id = R.drawable.qq20210315211722), contentDescription = null)
        }
    }
}
