package com.example.fragmentsofmemory

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fragmentsofmemory.Database.DrawerItems
import com.example.fragmentsofmemory.fragments.userContent

class DialogViewModel: ViewModel() {
    var openDialog by  mutableStateOf(false)
    var finishDialog by  mutableStateOf(false)

    @Composable
    fun PopUpAlertDialog(viewModel: UiModel) {

        if (openDialog) {

            if(viewModel.textModify == ""){
                viewModel.adding = false
                viewModel.maining = true
            }

            else {
                AlertDialog(
                    onDismissRequest = {
                        // Dismiss the dialog when the user clicks outside the dialog or on the back
                        // button. If you want to disable that functionality, simply use an empty
                        // onCloseRequest.
                        openDialog = false
                    },
                    title = {
                        Text(text = "还有没写完的东西呐,你确定要退出🐎")
                    },
                    text = {
                        Text(text = "埃拉我i耨爱三到四阿斯顿阿斯顿阿斯顿阿斯顿阿斯顿阿斯顿阿斯顿阿斯顿阿斯顿u暗送不低啊建瓯市第")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                // TODO 检测是否还有文字
                                openDialog = false
                                viewModel.timeResult = ""
                                viewModel.textModify = ""
                                viewModel.adding = false
                                viewModel.maining = true
                            }
                        ) {
                            Text("确定")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openDialog = false
                            }
                        ) {
                            Text("留着继续写")
                        }
                    }
                )
            }
        }
    }

/*
    @Composable
    fun ConfirmAlertDialog(userCardViewModel: UserCardViewModel) {
        val viewModel: UiModel = viewModel()
        if (finishDialog) {
            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    finishDialog = false
                },
                title = {
                    Text(text = "确定将添加到碎片中吗")
                },
                text = {
                    Text(text = "好的不好好的不要,好的,添加吧")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            userCardViewModel.AddDatabase("nmsl", userContent.value)
                            finishDialog = false
                            viewModel.adding = false
                        }
                    ) {
                        Text("确定")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            finishDialog = false
                        }
                    ) {
                        Text("还是算了吧")
                    }
                }
            )
        }
    }*/


    @Composable
    fun PopUpAlertDialogDrawerItems(viewModel: UiModel, userCardViewModel: UserCardViewModel) {
        if(viewModel.addNewCategory || viewModel.editingCategory) {
            AlertDialog(

                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    viewModel.addNewCategory = false
                },
                title = {
                    Text(if(viewModel.editingCategory) "修改名字啦~" else "添加新的分类~")
                },
                text = {
                       Column(modifier = Modifier.padding(top = 10.dp)) {
                           Row(){
                               Surface(
                                   shape = CircleShape,
                                   color = (Color(208, 207, 209)),
                                   modifier = Modifier.size(20.dp).clickable {
                                   }.align(Alignment.CenterVertically)
                               ) {

                               }
                               TextField(value = viewModel.categoryName, onValueChange = {
                                   viewModel.categoryName = it
                               },
                                   colors = TextFieldDefaults.textFieldColors(
                                       backgroundColor = Color(255, 255, 255, 1)),
                                   textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.W900),

                                   singleLine = true)
                           }
                       }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if(viewModel.addNewCategory) {
                                userCardViewModel.addCategoryDataBase(viewModel.categoryName)
                                viewModel.addNewCategory = false
                                viewModel.categoryName = ""
                            }

                            if(viewModel.editingCategory){

                                userCardViewModel.updateCategoryDataBaseName(viewModel.editingCategoryUid, viewModel.categoryName)
                                viewModel.editingCategory = false
                                viewModel.categoryName = ""
                            }
                        }
                    ) {
                        Text("确定")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            viewModel.addNewCategory = false
                            viewModel.categoryName = ""
                            viewModel.editingCategory = false
                        }
                    ) {
                        Text("取消")
                    }
                },
            )
        }
    }
}