package com.example.fragmentsofmemory.fragments

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.registerForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.icons.twotone.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fragmentsofmemory.*
import com.example.fragmentsofmemory.Database.CategoryCardCount
import com.example.fragmentsofmemory.Database.DrawerItems
import com.example.fragmentsofmemory.Database.UserCard
import com.example.fragmentsofmemory.Database.UserInfo
import com.example.fragmentsofmemory.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun CreateMemory(viewModel: UiModel,
                 userName:String,
                 userContent:String,
                 memoryOrder:Int,
                 time: String,
                 userCardViewModel: UserCardViewModel,
                 cardID:Int,
                 file: File,
                 categoryID: Int?,
                 context: Context) {

    Card(elevation = 10.dp,
        modifier = Modifier
        .padding(20.dp)
        .clip(shape = RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    if (viewModel.maining) {
                        viewModel.testTxt = userContent
                        viewModel.reading = true
                        viewModel.cardId = cardID
                        viewModel.maining = false
                        viewModel.timeResult = ""
                    }
                    viewModel.draweringPage = false
                }
                .clip(shape = RoundedCornerShape(14.dp)),
            horizontalAlignment = Alignment.Start
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                ) {
                    viewModel.InitUserProfilePic()
                  //  Image(painter = painterResource(id = R.drawable.qq20210315211722), contentDescription = null)
                }

                Column (modifier = Modifier.padding(start = 5.dp)){
                    Text(userName, fontWeight = FontWeight.Bold)
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.align(Alignment.End), verticalAlignment = Alignment.CenterVertically){
                        // Text(text = "#$memoryOrder", fontWeight = FontWeight.Bold)
                        IconButton(onClick = {
                        }) {
                            Icon(painter = painterResource(id = R.drawable.more_horiz_24px), contentDescription = null)
                        }
                    }
                }
            }

            // 正文
            Text(userContent, maxLines = 7,overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.body2, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 10.dp))


            val categories by userCardViewModel.drawer.observeAsState()
            val categoryName = categories?.find { it.uid == categoryID }?.drawerItems


            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(time.isNotBlank()) {
                        Text(
                            text = time,
                            modifier = Modifier.padding(8.dp),
                            fontWeight = FontWeight.W800,
                            style = MaterialTheme.typography.body2,
                            color = Color.Gray
                        )
                    }
                    if(categoryName != null) {
                        NormalTab(categoryName)
                    }
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy((-15).dp),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            IconButton(onClick = {
                                if(viewModel.maining)viewModel.startEdit(cardID, userContent, time)
                            }) {
                                Icon(
                                    (Icons.Rounded.Create),
                                    contentDescription = null,
                                    modifier = viewModel.iconSize
                                )
                            }
                            IconButton(onClick = {
                                if(viewModel.maining){
                                    viewModel.startShare(context, userContent)
                                }
                            }) {
                                Icon(
                                    Icons.Rounded.Share,
                                    contentDescription = null,
                                    modifier = viewModel.iconSize
                                )
                            }
                            IconButton(onClick = {
                                if(viewModel.maining){
                                    viewModel.cardId = cardID
                                    viewModel.requestDeleteCard = true
                                } }) {
                                Icon(
                                    Icons.Rounded.Delete,
                                    contentDescription = null,
                                    modifier = viewModel.iconSize
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowAllCards(viewModel: UiModel,
                 items: List<UserCard>,
                 user:UserInfo,
                 userCardViewModel: UserCardViewModel,
                 file: File,
                 context: Context) {

    Column(modifier = Modifier.fillMaxHeight()) {

        val cardColumnState = rememberLazyListState(0)

        /*LaunchedEffect(user.last) {
            cardColumnState.animateScrollToItem(0)
        }*/

        LazyColumn(
            modifier = Modifier
                .weight(1f),
            reverseLayout = true, state = cardColumnState
        ) {
            items(items.size + 1) {
                if(it < items.size) {
                    Column(verticalArrangement = Arrangement.SpaceEvenly) {
                        // null 表示所有分类
                        if (user.last == null || items[it].categoryID == user.last) {
                            CreateMemory(
                                viewModel = viewModel,
                                userName = user.userName,
                                userContent = items[it].content,
                                time = items[it].time,
                                memoryOrder = it + 1,
                                userCardViewModel = userCardViewModel,
                                cardID = items[it].id,
                                file = file,
                                categoryID = items[it].categoryID,
                                context = context
                            )
                        }
                        // Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    }
                } else {
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }

}


fun popUpDrawer(scaffoldState: ScaffoldState, scope: CoroutineScope) {

    scope.launch {
        scaffoldState.drawerState.open()
        scaffoldState.drawerState.overflow
    }
}

@Composable
fun TopBar(viewModel: UiModel, scaffoldState: ScaffoldState, scope: CoroutineScope) {
    TopAppBar(
        title = {
            Text(text = viewModel.currentTitle, style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 19.sp,
                letterSpacing = 0.15.sp
            ), fontWeight = FontWeight.W700, modifier = Modifier.padding(end = 7.dp))
        },

        actions = {
            IconButton(onClick = {
                // TODO: 下个版本添加搜索 1.0 完工
            }) {
                Icon(Icons.TwoTone.Search, contentDescription = "Localized description", tint = Color.White)
            }
        },


        navigationIcon = {
            IconButton(onClick = {
                popUpDrawer(scaffoldState, scope)
                viewModel.draweringPage = true
            }
            ){
                Icon(Icons.Rounded.Menu, contentDescription = null)
            }
        }
    )
}



@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun HomePageEntrances(viewModel: UiModel, userCardViewModel: UserCardViewModel, file: File, context: Context, user:UserInfo) {

    val scaffoldState = rememberScaffoldState(drawerState = DrawerState(DrawerValue.Closed) {
        (it == DrawerValue.Closed) || viewModel.maining
    })
    val scope = rememberCoroutineScope()

    val userCardValue: List<UserCard>? by userCardViewModel.allCards.observeAsState()
    val userCategoryNum: List<CategoryCardCount>? by userCardViewModel.cardNum.observeAsState()
    val drawerItems: List<DrawerItems>? by userCardViewModel.drawer.observeAsState()

    Scaffold(
        content = {
            //  HomePage(scaffoldState = scaffoldState, scope = scope, userCardViewModel)
            viewModel.SetBackground(background = R.drawable._55)

            userCardValue?.let { it1 ->
                user.let { it2 ->
                    ShowAllCards(viewModel,
                        items = it1,
                        it2, userCardViewModel,
                        file,
                        context)
                }
            }

       //    ShowAllCards(items = userCardViewModel.allCards, scaffoldState = scaffoldState, scope = scope, userInfoViewModel)
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (viewModel.maining) {
                viewModel.adding = true
                viewModel.maining = false
            }
                //   userCardViewModel.AddDatabase("Nthily", "Hello world", "3.15")
                //    Log.d(TAG, "Hello ${userCardViewModel.allCards.size}")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.add_24px),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
            }
        },
        //   bottomBar = { BottomApps() },
        drawerContent = {
            drawerItems?.let {
                user.let { it1 ->
                    DisplayDrawerContent(
                        viewModel = viewModel,
                        scaffoldState = scaffoldState,
                        scope = scope,
                        userCardViewModel = userCardViewModel,
                        categoryNum = userCategoryNum,
                        drawerItems = it,
                        file = file,
                        context = context,
                        user = it1
                    )
                }
            }
        },
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(viewModel, scaffoldState, scope)
        },
        drawerBackgroundColor = Color(255, 255 ,255 ,215),
    )

}