package com.example.fragmentsofmemory

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap

import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.example.fragmentsofmemory.fragments.*

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


class MainActivity : AppCompatActivity() {

 //   private val drawerItemsViewModel by viewModels<DrawerItemsViewModel>()
    private val userCardViewModel by viewModels<UserCardViewModel>()

    private val userInfoViewModel by viewModels<UserInfoViewModel>()

    private val dialogViewModel:DialogViewModel by viewModels()
    private val viewModel:UiModel by viewModels()



    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MyTheme(viewModel) {

                HomePageEntrances(userCardViewModel, userInfoViewModel)
                AddingPage(userCardViewModel)
                dialogViewModel.PopUpAlertDialog()
                dialogViewModel.PopUpAlertDialogDrawerItems(userCardViewModel)
                ReadingFragments(userInfoViewModel, userCardViewModel)
                timePicker()

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

    private fun timePicker(){
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
