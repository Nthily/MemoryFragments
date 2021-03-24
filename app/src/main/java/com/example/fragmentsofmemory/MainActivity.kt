package com.example.fragmentsofmemory

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.example.fragmentsofmemory.fragments.*

import com.example.fragmentsofmemory.ui.theme.MyTheme



class MainActivity : AppCompatActivity() {

    private val userCardViewModel by viewModels<UserCardViewModel>()

    private val drawerItemsViewModel by viewModels<DrawerItemsViewModel>()

    private val userInfoViewModel by viewModels<UserInfoViewModel>()

    private val dialogViewModel:DialogViewModel by viewModels()
    private val viewModel:UiModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme(viewModel) {
                HomePageEntrances(userCardViewModel, drawerItemsViewModel, userInfoViewModel)
                AddingPage(userCardViewModel)
                dialogViewModel.PopUpAlertDialog()
                ReadingFragments(userInfoViewModel, userCardViewModel)
                Material()
            }
        }
    }


    override fun onBackPressed() {

        when(true){
            viewModel.adding -> dialogViewModel.openDialog = true
            viewModel.draweringPage -> viewModel.requestCloseDrawerPage = true
            viewModel.reading -> viewModel.endReading()
            else -> super.onBackPressed()
        }
    }

    fun Material(){
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
