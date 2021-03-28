package com.example.fragmentsofmemory.fragments

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fragmentsofmemory.Database.CategoryCardCount
import com.example.fragmentsofmemory.Database.DrawerItems
import com.example.fragmentsofmemory.R
import com.example.fragmentsofmemory.UiModel
import com.example.fragmentsofmemory.UserCardViewModel
import com.example.fragmentsofmemory.ui.theme.MyTheme

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.padding

import androidx.compose.material.Icon

import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.*
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DrawerInfo(items: List<DrawerItems>,
               scaffoldState: ScaffoldState,
               scope: CoroutineScope,
               userCardViewModel: UserCardViewModel,
               categoryItems: List<CategoryCardCount>) {

    val viewModel: UiModel = viewModel()

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(if (viewModel.lightTheme) Color(90, 143, 185) else Color(35, 48, 64))
    ) {
        Column(modifier = Modifier.height(150.dp)
        ) {
            Row(modifier = Modifier.padding(start = 15.dp, top = 15.dp)){
                Surface(
                    modifier = Modifier
                        .size(70.dp),
                    shape = CircleShape
                ) {
                    Image(painter = painterResource(id = R.drawable.qq20210315211722), contentDescription = null)
                }
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                    CompositionLocalProvider(LocalContentColor provides Color.White) {
                        IconButton(onClick = {
                            viewModel.lightTheme = when(viewModel.lightTheme){
                                viewModel.lightTheme -> !viewModel.lightTheme
                                else -> viewModel.lightTheme
                            }
                        }) {
                            Icon(painter = painterResource(id = R.drawable.dark_mode_24px), contentDescription = "")
                        }
                    }

                }
            }
            Text(text = "Nthily", style = MaterialTheme.typography.body2, fontWeight = FontWeight.W900, modifier = Modifier.padding(start = 15.dp, top = 15.dp), color = Color.White)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (viewModel.lightTheme) Color.White else Color(0xFF1C242F)
            )
    ) {


        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(items.size) {
                val squareSize = 96.dp

                var swipeableState = rememberSwipeableState(0) //控制是否弹出, 1 为弹出
                val sizePx = with(LocalDensity.current) { squareSize.toPx() }
                val anchors = mapOf(0f to 0, sizePx to -1)

                var editable by remember { mutableStateOf(false) }

                editable = swipeableState.targetValue == -1

                Box{
                    Column(
                        modifier = Modifier
                            .swipeable(
                                state = swipeableState,
                                anchors = anchors,
                                thresholds = { from, to -> FractionalThreshold(0.3f) },
                                orientation = Orientation.Horizontal,
                                reverseDirection = true,
                            )
                            .offset { IntOffset(-swipeableState.offset.value.roundToInt(), 0)}

                            .clickable {
                                viewModel.currentCategory = items[it].uid
                                viewModel.selectedItems = items[it].uid
                             //   if(swipeableState.targetValue == -1) swipeableState.targetValue = 0
                                /*
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                    scaffoldState.drawerState.overflow
                                }*/
                            }
                            .background(if (items[it].uid != viewModel.selectedItems) MaterialTheme.colors.surface else MaterialTheme.colors.primary)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {

                            Icon(
                                Icons.Rounded.Bookmark,
                                contentDescription = null,
                                modifier = Modifier.padding(5.dp)
                            )
                            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                            Text(
                                text = items[it].drawerItems,
                                style = MaterialTheme.typography.body2,
                                fontWeight = FontWeight.W600,
                                modifier = Modifier.padding(6.dp)
                            )
                            // Spacer(modifier = Modifier.padding(vertical = 5.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 5.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = (Color(208, 207, 209)),
                                ) {

                                    Text(
                                        text = "${categoryItems[it].count}",
                                        modifier = Modifier.padding(
                                            start = 6.dp,
                                            end = 6.dp,
                                            top = 2.dp,
                                            bottom = 2.dp
                                        ),
                                        style = androidx.compose.ui.text.TextStyle(
                                            fontWeight = FontWeight.W900,
                                            fontSize = 12.sp,
                                            letterSpacing = 0.15.sp,
                                            color = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }

                    androidx.compose.animation.AnimatedVisibility(visible = editable) {
                        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), horizontalAlignment = Alignment.End) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ){
                                IconButton(onClick = {
                                }, modifier = Modifier.background(Color(0xFF7F849F)).padding(1.dp)) {
                                    Icon(Icons.Rounded.Edit, contentDescription = null)
                                }
                                IconButton(onClick = {
                                    userCardViewModel.deleteCategoryDataBase(items[it].uid)
                                }, modifier = Modifier.background(Color(0xFFE65B65)).padding(1.dp)) {
                                    Icon(Icons.Rounded.Delete, contentDescription = null)
                                }
                            }
                        }
                    }
                }


            }
        }
     //   Spacer(modifier = Modifier.padding(vertical = 5.dp))
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .height(1.dp))

        Row(
            modifier = Modifier
                .clickable {
                    //   drawerItemsViewModel.addDrawerItemsDatabase("我asdasd是憨批")
                    //   userCardViewModel.addCategoryDataBase("你好呀")
                    viewModel.addNewCategory = true
                }
                .padding(8.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.padding(5.dp))
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            Text(text = "添加分类", style = MaterialTheme.typography.body2, fontWeight = FontWeight.W600, modifier = Modifier.padding(6.dp))
        }
    }
}


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DisplayDrawerContent(
    viewModel:UiModel,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    userCardViewModel: UserCardViewModel,
    categoryNum: List<CategoryCardCount>?,
    drawerItems: List<DrawerItems>) {
    MyTheme(viewModel = viewModel) {

    //    val drawerItems: List<DrawerItems>? by userCardViewModel.drawer.observeAsState()


        if (categoryNum != null) {
            DrawerInfo(drawerItems, scaffoldState, scope, userCardViewModel, categoryNum)
        }

        if(viewModel.requestCloseDrawerPage) {
            scope.launch {
                viewModel.draweringPage = false
                scaffoldState.drawerState.close()
                scaffoldState.drawerState.overflow
            }
            viewModel.requestCloseDrawerPage = false
        }
    }

}