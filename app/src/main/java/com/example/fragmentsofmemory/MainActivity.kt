package com.example.fragmentsofmemory

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi

import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker


class MainActivity : AppCompatActivity() {

 //   private val drawerItemsViewModel by viewModels<DrawerItemsViewModel>()
    private val userCardViewModel by viewModels<AppViewModel>()

    private val dialogViewModel:DialogViewModel by viewModels()
    private val viewModel:UiModel by viewModels()


    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        setContent {
            val context = LocalContext.current
            val file = File(context.getExternalFilesDir(null), "picture.jpg")

            MyTheme(viewModel) {
                HomePageEntrances(userCardViewModel, userInfoViewModel, file, context)
                AddingPage(userCardViewModel, file , context)
                dialogViewModel.PopUpAlertDialog()
                dialogViewModel.PopUpAlertDialogDrawerItems(userCardViewModel)
                ReadingFragments(userInfoViewModel, userCardViewModel, file, context)
                timePicker()
            }

        }

         */
    }


    override fun onBackPressed() {

        when {
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
