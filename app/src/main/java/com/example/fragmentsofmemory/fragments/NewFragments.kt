package com.example.fragmentsofmemory.fragments

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Text
import com.example.fragmentsofmemory.*
import com.example.fragmentsofmemory.Database.UserInfo
import com.example.fragmentsofmemory.R
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
fun PageContent(viewModel: UiModel, file: File, context: Context) {
    viewModel.SetSecBackground(background = R.drawable.add_bkg)
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {


        TextField(value = viewModel.textModify, onValueChange = {
            viewModel.textModify = it
        },modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(255, 255, 255, 1),
                cursorColor = Color.White
            ),
            maxLines = Int.MAX_VALUE,
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.W900
            )
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
                viewModel.InitUserProfilePic()
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
                    }, colors = if(viewModel.timeResult != "") ButtonDefaults.buttonColors(backgroundColor = Color(0xFF48C785))
                    else ButtonDefaults.buttonColors(MaterialTheme.colors.primary), modifier = Modifier
                        .animateContentSize()
                        .padding(5.dp)) {
                        Text(if(viewModel.timeResult != "") viewModel.timeResult
                        else "请选择记忆的时间吧~")
                    }
                }
            }
        }

    }
}


@Composable
fun AddingPage(dialogViewModel: DialogViewModel,
               viewModel: UiModel,
               appViewModel: AppViewModel,
               file:File,
               context: Context,
               user: UserInfo
) {

    val percentOffsetX = animateFloatAsState(if (viewModel.adding) 0f else 1f)

    Box(
        modifier = Modifier
            .percentOffsetX(percentOffsetX.value)
            .background(Color.White)
            .fillMaxSize()
    ){

        Scaffold(
            content = {
                PageContent(viewModel, file, context)
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
                        Text(text = "完成", style = MaterialTheme.typography.body1,modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .clickable(
                                onClick = {

                                    if (viewModel.editing) {
                                        //user.last?.let {
                                            appViewModel.updateCardMsg(
                                                viewModel.cardId,
                                                viewModel.textModify,
                                                viewModel.timeResult,
                                                user.last
                                            )
                                        //}
                                    } else {
                                        //user.last?.let {
                                            appViewModel.addDataBase(
                                                viewModel.textModify,
                                                viewModel.timeResult,
                                                user.last
                                            )
                                        //}
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