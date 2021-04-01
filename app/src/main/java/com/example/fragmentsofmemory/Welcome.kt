package com.example.fragmentsofmemory

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.example.fragmentsofmemory.fragments.AddingPage
import com.example.fragmentsofmemory.fragments.HomePageEntrances
import com.example.fragmentsofmemory.fragments.ReadingFragments
import com.example.fragmentsofmemory.fragments.popUpDrawer
import com.example.fragmentsofmemory.ui.theme.MyTheme
import com.google.accompanist.coil.CoilImage
import java.io.File
import java.util.Timer
import kotlin.concurrent.schedule

class Welcome : AppCompatActivity() {

    private val userCardViewModel by viewModels<UserCardViewModel>()
    private val userInfoViewModel by viewModels<UserInfoViewModel>()
    private val dialogViewModel:DialogViewModel by viewModels()
    private val viewModel:UiModel by viewModels()

    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val context = LocalContext.current
            val file = File(context.getExternalFilesDir(null), "picture.jpg")
            val navController = rememberNavController()

               NavHost(navController, startDestination = "welcome") {
                composable("welcome") { WelcomPage( viewModel, navController,file, context) }
                composable("mainPage") {

                    MyTheme(viewModel) {
                        HomePageEntrances(viewModel, userCardViewModel, userInfoViewModel, file, context)
                        AddingPage(viewModel, userCardViewModel, file , context)
                        dialogViewModel.PopUpAlertDialog(viewModel)
                        dialogViewModel.PopUpAlertDialogDrawerItems(viewModel, userCardViewModel)
                        ReadingFragments(viewModel, userInfoViewModel, userCardViewModel, file, context)
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
            viewModel.draweringPage -> viewModel.closeDrawerContent()
            viewModel.reading -> viewModel.endReading()
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
fun WelcomPage(viewModel:UiModel, navController: NavController, file:File, context: Context) {

    Column() {
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

                AnimatedVisibility(visible = textVisibility.value) {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = CircleShape,
                            modifier = Modifier.size(130.dp)
                        ) {
                            viewModel.InitUserProfilePic(file = file, context = context)
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

        val delayedHandler = Handler()
        delayedHandler.postDelayed({
            navController.navigate("mainPage") {
                popUpTo("welcome") { //删除 welcome 界面
                    inclusive = true //关闭栈
                }
            }
        }, 2000)

    }
}

