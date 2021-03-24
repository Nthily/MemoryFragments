package com.example.fragmentsofmemory.fragments

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fragmentsofmemory.Database.UserCard
import com.example.fragmentsofmemory.Database.UserInfo
import com.example.fragmentsofmemory.R
import com.example.fragmentsofmemory.UiModel
import com.example.fragmentsofmemory.UserCardViewModel
import com.example.fragmentsofmemory.UserInfoViewModel
import kotlin.math.roundToInt




@Composable
fun DeteilPage(userCardViewModel: UserCardViewModel) {
    val viewModel: UiModel = viewModel()
    viewModel.SetSecBackground(background = R.drawable.pattern_2)

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Surface(modifier = Modifier
                    .size(30.dp),
                    shape = CircleShape,
                ) {
                    Image(painter = painterResource(id = R.drawable.qq20210315211722), contentDescription = null)
                }
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Text(text = viewModel.userName,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontWeight = FontWeight.W900,
                    style = MaterialTheme.typography.body1)
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                    Row(){
                        Text(text = viewModel.timeResult, fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.body1)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        LazyColumn(
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = viewModel.testTxt,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp, end = 15.dp, top = 15.dp),
                            fontWeight = FontWeight.W900,
                            textAlign = TextAlign.Center
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy((-15).dp), modifier = Modifier.align(Alignment.End)){
                            IconButton(onClick = {
                                viewModel.startEdit(viewModel.cardId, viewModel.testTxt, viewModel.timeResult)
                            }) {
                                Icon((Icons.Rounded.Create), contentDescription = null, modifier = viewModel.iconSize)
                            }
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(painter = painterResource(id = R.drawable.reply_24px), contentDescription = null, modifier = viewModel.iconSize)
                            }
                            IconButton(onClick = {
                                userCardViewModel.RemoveDataBase(viewModel.cardId)
                                viewModel.reading = false
                                viewModel.maining = true
                            }) {
                                Icon(Icons.Rounded.Delete, contentDescription = null, modifier = viewModel.iconSize)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReadingFragments(userInfoViewModel: UserInfoViewModel,
                     userCardViewModel: UserCardViewModel) {

    val viewModel: UiModel = viewModel()
    val percentOffsetX = animateFloatAsState(if (viewModel.reading) 0f else 1f)

    Box(
        modifier = Modifier
            .percentOffsetX(percentOffsetX.value)
            .background(Color.White)
            .fillMaxSize()
    ) {
        Scaffold(
            content = {
                DeteilPage(userCardViewModel)
            },

            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = {
                            viewModel.reading = false
                            viewModel.maining = true
                        }) {
                            Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                        }
                    },
                    /*
                    actions = {
                        Text(text = "完成", modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .clickable(
                                onClick = {
                                    viewModel.finishDialog = true
                                    //    userCardViewModel.AddDatabase("香辣鸡腿堡", userContent.value, "今天")
                                    //    viewModel.adding = false
                                },
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ))
                    },*/
                )

            }
        )
    }

}