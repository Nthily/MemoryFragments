package com.example.fragmentsofmemory.fragments

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.roundToInt
import java.time.format.TextStyle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.afollestad.materialdialogs.MaterialDialog
import com.example.fragmentsofmemory.*
import com.example.fragmentsofmemory.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

var userContent = mutableStateOf("")

fun Modifier.percentOffsetX(percent: Float): Modifier =
    this.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.width, placeable.height) {
            val offset = (percent * placeable.width).roundToInt()
            placeable.placeRelative(offset, 0)
        }
    }



@Composable
fun PageContent(file: File, context: Context) {
    val viewModel: UiModel = viewModel()
    viewModel.SetSecBackground(background = R.drawable._e826ba47840c0723c356ce92e6d8b39)


    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {


        TextField(value = viewModel.textModify, onValueChange = {
            viewModel.textModify = it
        },modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(255, 255, 255, 1)),
            textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.W900)
        )

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier
                .align(Alignment.Start)

        ){
            Surface(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterVertically),
                shape = CircleShape
            ) {
                viewModel.InitUserProfilePic(file = file, context = context)
              //  Image(painter = painterResource(R.drawable.qq20210315211722), contentDescription = null)
            }
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            Text(text = "记录点美好的东西吧~", style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.W900,
                modifier = Modifier.align(Alignment.CenterVertically))
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                Row{
                    Button(onClick = {
                        viewModel.timing = true
                    }, colors = if(viewModel.timeResult != "") ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1DC792))
                    else ButtonDefaults.buttonColors(MaterialTheme.colors.primary), modifier = Modifier
                        .animateContentSize()
                        .padding(5.dp)) {
                        Text(if(viewModel.timeResult != "") "时间存储完毕！"
                        else "请选择记忆的时间吧~")
                    }
                }
            }
        }
    }
}


@Composable
fun AddingPage(userCardViewModel: UserCardViewModel,file:File, context: Context) {

    val viewModel: UiModel = viewModel()
    val dialogViewModel: DialogViewModel = viewModel()

    val percentOffsetX = animateFloatAsState(if (viewModel.adding) 0f else 1f)

    Box(
        modifier = Modifier
            .percentOffsetX(percentOffsetX.value)
            .background(Color.White)
            .fillMaxSize()
    ){

        Scaffold(
            content = {
                PageContent(file, context)
            },

            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = {
                            dialogViewModel.openDialog = true
                        }) {
                            Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                        }
                    },
                    actions = {
                        Text(text = "完成", modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .clickable(
                                onClick = {

                                    if (viewModel.editing) {
                                        userCardViewModel.UpdateCardMsg(
                                            viewModel.cardId,
                                            viewModel.textModify,
                                            viewModel.timeResult,
                                            viewModel.currentCategory
                                        )
                                    } else {
                                        userCardViewModel.addDataBase(
                                            viewModel.textModify,
                                            viewModel.timeResult,
                                            viewModel.currentCategory
                                        )
                                    }

                                    viewModel.endAddPage()
                                    //   userContent.value = ""
                                },
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ))
                    },
                )

            }
        )
    }
}