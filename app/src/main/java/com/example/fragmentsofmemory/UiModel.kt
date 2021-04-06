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

    var lightTheme by mutableStateOf(true)

    var maining by mutableStateOf(true)

    // 检测是否为主界面,否则在其他界面会触发主界面的控件
    // Check whether the main interface is the main interface, otherwise the controls of the main interface will be triggered in other interfaces

    var adding by mutableStateOf(false)
    // 启动添加界面
    // Launch the add screen

    var draweringPage by mutableStateOf(false)

    // 启动 Drawer 并且判断是否处于 Drawer 的页面中
    // Start the Drawer and determine if you are in the Drawer's page

    var requestCloseDrawerPage by mutableStateOf(false)
    // 请求关闭汉堡菜单
    // request close drawer page

    var hasAnyExtraButtonRevealed by mutableStateOf(false)
    // 是否有任何分类被滑动并出现附加按钮
    // Are there any categories that are slid and additional buttons appear

    var reading by mutableStateOf(false)
    // 是否在阅读卡片详细内容
    // whether you are reading the card details

    var testTxt by mutableStateOf("")
    // 利用 testText 来传递卡片的内容最终显示在卡片详情里面显示而无需使用数据库代码
    // Use testText to pass the content of the card to be displayed in the card details without using database code

    val iconSize = Modifier.size(18.dp)

    var timing by mutableStateOf(false)
    // 是否打开时间选择器
    // whether to turn on the time picker

    var timeResult by mutableStateOf("")
    // 时间结果
    // time result

    var selectedTime by mutableStateOf(false)
    // 是否已经选择了时间
    // Has the time been selected

     var cardId by mutableStateOf(0)
    // 获取卡片的主键
    // Get the primary key of the card

    var textModify by mutableStateOf("")
    // 用户添加事件的文本
    // Text of user-added events

    var editing by mutableStateOf(false)
    // 编辑用户事件的详情状态
    // Edit the status of user event details

    var addNewCategory by mutableStateOf(false)
    // 当前是否正在添加新分类
    // Is a new category currently being added

    var categoryName by mutableStateOf("")

    var requestCloseDrawer by mutableStateOf(false)
    // 请求关闭抽屉栏
    // Request to close the Drawer

    var editingCategory by mutableStateOf(false)
    // 当前是否正在编辑分类名称
    // Is the category name currently being edited

    var editingCategoryUid by mutableStateOf(0)
    // 正在编辑分类的主键
    // The primary key of the category being edited

    val imageUriState = mutableStateOf<Uri?>(null)


    var userAvatarUploading by mutableStateOf<File?>(null)
    // 上传头像的临时文件
    // Temporary files for uploading avatars

    var userAvatarUploadedPath by mutableStateOf("")
    // 成功上传并完成裁剪的头像文件路径
    // Path to the avatar file that has been successfully uploaded and cropped

    var userAvatarPath by mutableStateOf("")
    // 用户头像所在的路径
    // The path where the user's avatar is located

    var editingProfile by mutableStateOf(false)
    // 当前是否正在编辑用户信息
    // whether user information is currently being edited

    var requestDeleteCard by mutableStateOf(false)
    // 请求删除卡片，true 将会触发 AlertDialog
    // Request to delete a card,true will trigger AlertDialog

    var currentTitle by mutableStateOf("")
    // 当前所在分类的名字
    // Name of the category you are currently in


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
