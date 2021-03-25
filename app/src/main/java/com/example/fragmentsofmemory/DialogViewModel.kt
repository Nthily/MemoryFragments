package com.example.fragmentsofmemory

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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


}