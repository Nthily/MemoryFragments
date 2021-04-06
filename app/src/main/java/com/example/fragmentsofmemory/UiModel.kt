package com.example.fragmentsofmemory

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat.startActivity
import com.google.accompanist.coil.CoilImage
import java.io.File


/**
 * Handles all classes regarding interface state
 */

class UiModel: ViewModel(){
   // var theme by mutableStateOf(MyTheme.Theme.Dark)
    var lightTheme by mutableStateOf(true)
    //  var currentPage: AddNewTreeHoles? by mutableStateOf(null)
    var maining by mutableStateOf(true)     // 检测是否为主界面,否则在其他界面回触发主界面的控件

    var adding by mutableStateOf(false)     // 启动添加界面

    var draweringPage by mutableStateOf(false) // 判断是否打开了汉堡菜单
    var requestCloseDrawerPage by mutableStateOf(false) // 请求关闭汉堡菜单

    var hasAnyExtraButtonRevealed by mutableStateOf(false)  // 是否有任何分类被滑动并出现附加按钮

    var reading by mutableStateOf(false) // 是否在阅读卡片详细内容

    var testTxt by mutableStateOf("") // 利用 testText 来传递卡片的内容

    val iconSize = Modifier.size(18.dp)
    var timing by mutableStateOf(false) // 是否打开时间选择器
    var timeResult by mutableStateOf("") // 时间结果

    var selectedTime by mutableStateOf(false)

    var cardId by mutableStateOf(0)

    var textModify by mutableStateOf("")
    var editing by mutableStateOf(false)

    var addNewCategory by mutableStateOf(false) // 当前是否正在添加新分类
    var categoryName by mutableStateOf("")

    var requestCloseDrawer by mutableStateOf(false)


    var editingCategory by mutableStateOf(false) // 当前是否正在编辑分类名称
    var editingCategoryUid by mutableStateOf(0)


    val imageUriState = mutableStateOf<Uri?>(null)
    var userAvatarUploading by mutableStateOf<File?>(null) //上传头像的临时文件
    var userAvatarUploadedPath by mutableStateOf("")    // 成功上传并完成裁剪的头像文件路径
    var userAvatarPath by mutableStateOf("") // 用户头像所在的路径


    var editingProfile by mutableStateOf(false) // 当前是否正在编辑用户信息

    var requestDeleteCard by mutableStateOf(false)


    var currentTitle by mutableStateOf("")

    fun endReading() {
        reading = false
        maining = true
        cardId = 0
        timeResult = ""
    }

    fun endEditProfile(){
        editingProfile = false
    }


    fun startShare(context: Context, message:String) {
        val sendIntent: Intent = Intent().apply { // 分享 Intent
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "选择分享到哪里吧 ~")
        startActivity(context, shareIntent, Bundle())
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
                .alpha(0.5f),
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


    fun deleteCard(appViewModel: AppViewModel) {
        appViewModel.removeDataBase(cardId)
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
    fun InitUserProfilePic() {

        // 处理不自动更新的 Bug
        val avatar = File(if(userAvatarUploadedPath.isEmpty()) userAvatarPath else userAvatarUploadedPath)

        if(avatar.exists()) {
            CoilImage(Uri.fromFile(avatar), null)
        } else {
            Image(painter = painterResource(id = R.drawable.qq20210315211722), contentDescription = null)
        }
    }

}
