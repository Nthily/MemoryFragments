package com.example.fragmentsofmemory

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.example.fragmentsofmemory.Database.UserInfo
import com.example.fragmentsofmemory.fragments.*
import com.example.fragmentsofmemory.ui.theme.MyTheme

import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.delay
import java.io.File
import java.util.Timer
import kotlin.concurrent.schedule

class Welcome : AppCompatActivity() {

    private val userCardViewModel by viewModels<UserCardViewModel>()
    private val dialogViewModel:DialogViewModel by viewModels()
    private val viewModel:UiModel by viewModels()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            File(viewModel.userAvatarPath).createNewFile()
            viewModel.userAvatarUploading!!.copyTo(File(viewModel.userAvatarPath), true)
            Toast.makeText(this, "更新头像成功", Toast.LENGTH_LONG).show()
        } else if(resultCode == UCrop.RESULT_ERROR) {
            Toast.makeText(this, "更新头像失败", Toast.LENGTH_LONG).show()
        }
    }


    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            val context = LocalContext.current
            val file = File(context.getExternalFilesDir(null), "picture.jpg")
            val navController = rememberNavController()
            val user: UserInfo? by userCardViewModel.user.observeAsState()

            viewModel.userAvatarPath = file.absolutePath

               NavHost(navController, startDestination = "welcome") {
                composable("welcome") { WelcomePage( viewModel, navController,file, context) }
                composable("mainPage") {

                    MyTheme(viewModel) {
                        HomePageEntrances(viewModel, userCardViewModel, file, context, user!!)
                        AddingPage(dialogViewModel, viewModel, userCardViewModel, file , context, user!!)
                        dialogViewModel.PopUpAlertDialog(viewModel)
                        dialogViewModel.PopUpAlertDialogDrawerItems(viewModel, userCardViewModel)
                        dialogViewModel.PopUpConfirmDeleteItem(viewModel, userCardViewModel)
                        ReadingFragments(viewModel, userCardViewModel, file, context, user!!)
                        timePicker()
                    }
                }

            }
        }
    }

    override fun onBackPressed() {
        when(true){
            (viewModel.adding && viewModel.textModify != "") -> dialogViewModel.openDialog = true
            viewModel.adding -> viewModel.endAddPage()
            (viewModel.draweringPage && !viewModel.editingProfile) -> viewModel.closeDrawerContent()
            viewModel.reading -> viewModel.endReading()
            viewModel.editingProfile -> viewModel.endEditProfile()
            else -> super.onBackPressed()
        }
    }
    private fun timePicker() {
        if(viewModel.timing) {
            MaterialDialog(this).show {
                datePicker { dialog, date ->
                    viewModel.timeResult = "${date.year}.${date.month + 1}.${date.dayOfMonth}"
                }
            }
        }
        viewModel.timing = false
        if(viewModel.timeResult != "")viewModel.selectedTime = true
    }
}

@ExperimentalAnimationApi
@Composable
fun WelcomePage(viewModel:UiModel, navController: NavController, file:File, context: Context) {
    Box{
        Image(painter = painterResource(id = R.drawable.wel_bkg), contentDescription = null,modifier = Modifier
            .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            val textVisibility = remember{ mutableStateOf(false)}

            Spacer(modifier = Modifier.padding(vertical = 50.dp))

            Timer("SettingUp", false).schedule(500) {
                textVisibility.value = true
            }

            AnimatedVisibility(visible = textVisibility.value,
                enter = fadeIn( animationSpec = tween(durationMillis = 550)),
                exit = fadeOut( animationSpec = tween(durationMillis = 550))
            ) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        shape = CircleShape,
                        modifier = Modifier.size(130.dp)
                    ) {
                        viewModel.InitUserProfilePic(context)
                    }
                    Spacer(modifier = Modifier.padding(vertical = 30.dp))
                    Text(text = "欢迎回家", fontWeight = FontWeight.W900, style = MaterialTheme.typography.h6)
                }

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = "永远相信美好的事情即将发生",
                        fontWeight = FontWeight.W500,
                        style = MaterialTheme.typography.body2)
                }
            }
        }
    }


    LaunchedEffect(true){
        delay(2000)
        navController.navigate("mainPage") {
            popUpTo("welcome") { //delete
                inclusive = true //close stack
            }
        }
    }
}

