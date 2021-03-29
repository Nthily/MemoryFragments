package com.example.fragmentsofmemory

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable

import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.example.fragmentsofmemory.fragments.*

import com.example.fragmentsofmemory.ui.theme.MyTheme
import com.google.android.material.snackbar.Snackbar


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
