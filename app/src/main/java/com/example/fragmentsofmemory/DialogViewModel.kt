package com.example.fragmentsofmemory

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay


/**
 * All classes about AlertDialog interface
 */

class DialogViewModel: ViewModel() {
    var openDialog by  mutableStateOf(false)

    @Composable
    fun PopUpAlertDialog(viewModel: UiModel) {

        if (openDialog) {

            if(viewModel.textModify.isBlank()){     // Content is empty or contains only non-visible characters (spaces, line breaks, etc.)
                openDialog = false
                viewModel.adding = false
                viewModel.maining = true
            }

            else {
                AlertDialog(
                    onDismissRequest = {
                        openDialog = false
                    },
                    title = {
                        Text(text = "还有没写完的东西呐,你确定要退出🐎")
                    },
                    text = {
                        Text(text = "埃拉我i耨爱三到四阿斯顿阿斯顿阿斯顿阿斯顿阿斯顿阿斯顿阿斯顿阿斯顿阿斯顿u暗送不低啊建瓯市第???")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
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

    @ExperimentalComposeUiApi
    @Composable
    fun PopUpAlertDialogDrawerItems(viewModel: UiModel, appViewModel: AppViewModel) {
        val focus = FocusRequester()
        val keyboard = LocalSoftwareKeyboardController.current

        if(viewModel.addNewCategory || viewModel.editingCategory) {

            val categoryName0 by remember { mutableStateOf(viewModel.categoryName) }    // The original name of the category being edited
            var categoryName by remember { mutableStateOf(viewModel.categoryName) }

            val error1 = categoryName.isBlank()    // Category name is null error
            val error2 = appViewModel.drawer.value?.any {    // Category name error already exists
                val con = it.drawerItems.trimEnd() == categoryName.trimEnd()
                (viewModel.addNewCategory && con)
                        || (viewModel.editingCategory && con && it.uid != viewModel.editingCategoryUid)
            } ?: true

            AlertDialog(

                onDismissRequest = {
                    viewModel.addNewCategory = false
                    viewModel.editingCategory = false
                },
                title = {
                    Text(if(viewModel.editingCategory) "修改分类 \"${categoryName0}\" 的名字啦~" else "添加新的分类~")
                },
                text = {

                   Column(modifier = Modifier.padding(top = 10.dp)) {



                       Row(){
                           // TODO: 下个版本再添加选择分类图标功能
                           /*Surface(
                               shape = CircleShape,
                               color = (Color(208, 207, 209)),
                               modifier = Modifier
                                   .size(20.dp)
                                   .clickable {
                                   }
                                   .align(Alignment.CenterVertically)
                           ) {

                           }*/

                           TextField(value = categoryName, onValueChange = {
                               categoryName = it.replace("\n", "")
                           },
                               isError = error1 || error2,
                               modifier = Modifier.focusRequester(focus),
                               colors = TextFieldDefaults.textFieldColors(
                                   backgroundColor = Color(255, 255, 255, 1)),
                               textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.W900),
                               singleLine = true,
                               maxLines = 1
                           )
                           LaunchedEffect(viewModel.addNewCategory) {
                               if(viewModel.addNewCategory) {
                                   delay(300)
                                   focus.requestFocus()
                                   keyboard?.showSoftwareKeyboard()
                               }
                           }
                           LaunchedEffect(viewModel.editingCategory) {
                               if(viewModel.editingCategory) {
                                   delay(300)
                                   focus.requestFocus()
                                   keyboard?.showSoftwareKeyboard()
                               }
                           }

                       }
                       if(error2) {
                           Text("* 该分类已存在", modifier = Modifier.padding(5.dp), style = MaterialTheme.typography.body2, color = Color(0xFFD53030))
                       }
                   }

                },
                confirmButton = {
                    TextButton(
                        enabled = !(error1 || error2),
                        onClick = {
                            if(viewModel.addNewCategory) {
                                appViewModel.addCategoryDataBase(categoryName)
                                viewModel.addNewCategory = false
                                viewModel.categoryName = ""
                            }

                            if(viewModel.editingCategory){
                                appViewModel.updateCategoryDataBaseName(viewModel.editingCategoryUid, categoryName)
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


    @Composable
    fun PopUpConfirmDeleteItem(viewModel: UiModel, appViewModel: AppViewModel) {

        if (viewModel.requestDeleteCard) {

            AlertDialog(
                onDismissRequest = {
                    viewModel.requestDeleteCard = false
                },
                title = {
                    Text(text = "真的要删除这片记忆嘛")
                },
                text = {
                    Text(text = "阿巴阿巴阿巴阿巴再考虑考虑啦")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteCard(appViewModel)
                            viewModel.requestDeleteCard = false
                        }
                    ) {
                        Text("确定")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            viewModel.requestDeleteCard = false
                        }
                    ) {
                        Text("取消")
                    }
                }
            )
        }
    }
}