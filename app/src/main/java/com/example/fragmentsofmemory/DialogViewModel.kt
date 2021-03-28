package com.example.fragmentsofmemory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.fragmentsofmemory.fragments.userContent

class DialogViewModel: ViewModel() {
    var openDialog by  mutableStateOf(false)
    var finishDialog by  mutableStateOf(false)

    @Composable
    fun PopUpAlertDialog() {
        val viewModel: UiModel = viewModel()
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
    fun PopUpAlertDialogDrawerItems(userCardViewModel: UserCardViewModel) {
        val viewModel: UiModel = viewModel()

        if(viewModel.addNewCategory) {
            AlertDialog(

                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    viewModel.addNewCategory = false
                },
                title = {
                    Text(text = "输入喜欢的分类名字吧~")
                },
                text = {
                       Column(modifier = Modifier.padding(top = 10.dp)) {
                           TextField(value = viewModel.categoryName, onValueChange = {
                               viewModel.categoryName = it
                           },
                               colors = TextFieldDefaults.textFieldColors(
                                   backgroundColor = Color(255, 255, 255, 1)),
                               textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.W900),

                               singleLine = true, leadingIcon = {
                                   IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(20.dp)) {
                                       Icon(Icons.Rounded.Favorite, contentDescription = null)
                                   }
                               })
                       }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            userCardViewModel.addCategoryDataBase(viewModel.categoryName)
                            viewModel.addNewCategory = false
                            viewModel.categoryName = ""
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
                        }
                    ) {
                        Text("取消")
                    }
                },
            )
        }
    }
}