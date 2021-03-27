package com.example.fragmentsofmemory.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fragmentsofmemory.*
import com.example.fragmentsofmemory.Database.DrawerItems
import com.example.fragmentsofmemory.Database.UserCard
import com.example.fragmentsofmemory.Database.UserInfo
import com.example.fragmentsofmemory.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



@Composable
fun CreateMemory(userName:String,
                 userContent:String,
                 memoryOrder:Int,
                 time:String,
                 userCardViewModel: UserCardViewModel,
                 cardID:Int) {

    val viewModel: UiModel = viewModel()

    Card(elevation = 10.dp, modifier = Modifier
        .padding(20.dp)
        .clip(RoundedCornerShape(14.dp))) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .clickable {
                    if (viewModel.maining) {
                        viewModel.testTxt = userContent
                        viewModel.userName = userName
                        viewModel.reading = true
                        viewModel.cardId = cardID
                        viewModel.maining = false
                        viewModel.timeResult = time
                    }
                    viewModel.draweringPage = false
                },
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
                    Image(painter = painterResource(id = R.drawable.qq20210315211722), contentDescription = null)
                }

                Column (modifier = Modifier.padding(start = 5.dp)){
                    Text(userName, fontWeight = FontWeight.Bold)
                }
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.align(Alignment.End), verticalAlignment = Alignment.CenterVertically){
                        Text(text = "#$memoryOrder", fontWeight = FontWeight.Bold)
                        IconButton(onClick = {
                        }) {
                            Icon(painter = painterResource(id = R.drawable.more_horiz_24px), contentDescription = null)
                        }
                    }
                }
            }
            Text(userContent, maxLines = 7,overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.body2, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 10.dp),)

            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = time, modifier = Modifier.padding(8.dp), fontWeight = FontWeight.W800, style = MaterialTheme.typography.body2)
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
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.reply_24px),
                                    contentDescription = null,
                                    modifier = viewModel.iconSize
                                )
                            }
                            IconButton(onClick = {
                                if(viewModel.maining){
                                    viewModel.cardId = cardID
                                    viewModel.deleteCard(userCardViewModel)
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
fun AlertNoCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "该分类下还没有任何记忆碎片噢~ _(:з)∠)_",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ShowAllCards(items: List<UserCard>,
                 userNameitems: List<UserInfo>,
                 userCardViewModel: UserCardViewModel) {

  //  Log.d(ContentValues.TAG, "Hello ${items.size}")

    val viewModel: UiModel = viewModel()
    Column(modifier = Modifier.fillMaxHeight()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            reverseLayout = true, state = LazyListState(items.size)
        ) {
            items(items.size) {
                Column(verticalArrangement = Arrangement.SpaceEvenly) {
                    if(items[it].categoryID == viewModel.currentCategory) {
                        CreateMemory(
                            userName = userNameitems[0].userName,
                            userContent = items[it].content,
                            time = items[it].time,
                            memoryOrder = it + 1,
                            userCardViewModel = userCardViewModel,
                            cardID = items[it].id
                        )
                    }
                    // Spacer(modifier = Modifier.padding(vertical = 5.dp))
                }
            }
        }
        /*
        if (userCardViewModel.searchCardNum(viewModel.currentCategory) == 0) {
            AlertNoCard()
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                reverseLayout = true, state = LazyListState(items.size)
            ) {
                items(items.size) {
                    Column(verticalArrangement = Arrangement.SpaceEvenly) {
                        if(items[it].categoryID == viewModel.currentCategory) {
                            CreateMemory(
                                userName = userNameitems[0].userName,
                                userContent = items[it].content,
                                time = items[it].time,
                                memoryOrder = it + 1,
                                userCardViewModel = userCardViewModel,
                                cardID = items[it].id
                            )
                        }
                        // Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    }
                }
            }
        }*/
    }

}


fun popUpDrawer(scaffoldState: ScaffoldState, scope: CoroutineScope) {

    scope.launch {
        scaffoldState.drawerState.open()
        scaffoldState.drawerState.overflow
    }
}

@Composable
fun TopBar(scaffoldState: ScaffoldState, scope: CoroutineScope) {
    val viewModel: UiModel = viewModel()
    TopAppBar(
        title = {
            Text(text = "记忆碎片", style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 19.sp,
                letterSpacing = 0.15.sp
            ), fontWeight = FontWeight.W700, modifier = Modifier.padding(end = 7.dp))
        },
        actions = {
            IconButton(onClick = {
                if(viewModel.maining){

                    viewModel.lightTheme = when(viewModel.lightTheme){
                        viewModel.lightTheme -> !viewModel.lightTheme
                        else -> viewModel.lightTheme
                    }
                }
            }) {
                Icon(painter = painterResource(id = R.drawable.dark_mode_24px), contentDescription = "Localized description")
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                popUpDrawer(scaffoldState, scope)
                viewModel.draweringPage = true}
            ){
                Icon(Icons.Rounded.Menu, contentDescription = null)
            }
        }
    )
}


@Composable
fun HomePageEntrances(userCardViewModel: UserCardViewModel,
                        userInfoViewModel: UserInfoViewModel) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val viewModel: UiModel = viewModel()

    val userCardvalue: List<UserCard>? by userCardViewModel.allCards.observeAsState()
    val userInfovalue: List<UserInfo>? by userInfoViewModel.userInfo.observeAsState()

    val userCategoryNum: List<Pair<Int, Int>>? by userCardViewModel.cardNum.observeAsState()

    val drawerItems: List<DrawerItems>? by userCardViewModel.drawer.observeAsState()

    Scaffold(
        content = {
            //  HomePage(scaffoldState = scaffoldState, scope = scope, userCardViewModel)
            viewModel.SetBackground(background = R.drawable._00d79dc3b43e3fdf3dc64452b0efe35)

            userCardvalue?.let { it1 ->
                userInfovalue?.let { it2 ->
                ShowAllCards(items = it1,
                    it2, userCardViewModel)
            } }

       //    ShowAllCards(items = userCardViewModel.allCards, scaffoldState = scaffoldState, scope = scope, userInfoViewModel)
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                  if(viewModel.maining){
                      viewModel.adding  = true
                      viewModel.maining = false
                  }
             //   userCardViewModel.AddDatabase("Nthily", "Hello world", "3.15")
                //    Log.d(TAG, "Hello ${userCardViewModel.allCards.size}")
            }) {
                Icon(painter = painterResource(id = R.drawable.add_24px), contentDescription = null, modifier = Modifier.size(25.dp))
            }
        },
        //   bottomBar = { BottomApps() },
        drawerContent = {
            drawerItems?.let {
                userCategoryNum?.let { it1 ->
                    DisplayDrawerContent(
                        viewModel = viewModel,
                        scaffoldState = scaffoldState,
                        scope = scope,
                        userCardViewModel = userCardViewModel,
                        categoryNum = it1,
                        drawerItems = drawerItems!!
                    )
                }
            }
                },
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(scaffoldState, scope)
        }
    )
}