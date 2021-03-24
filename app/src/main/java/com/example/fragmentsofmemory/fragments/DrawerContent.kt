package com.example.fragmentsofmemory.fragments

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fragmentsofmemory.Database.DrawerItems
import com.example.fragmentsofmemory.Database.UserCard
import com.example.fragmentsofmemory.Database.UserInfo
import com.example.fragmentsofmemory.DrawerItemsViewModel
import com.example.fragmentsofmemory.R
import com.example.fragmentsofmemory.UiModel
import com.example.fragmentsofmemory.UserCardViewModel
import com.example.fragmentsofmemory.ui.theme.MyTheme
import com.example.fragmentsofmemory.ui.theme.blueee


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun DrawerInfo(items: List<DrawerItems>, drawerItemsViewModel: DrawerItemsViewModel) {

    val viewModel: UiModel = viewModel()

    Column(modifier = Modifier
        .fillMaxWidth()

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
                if(viewModel.lightTheme) Color.White else Color(0xFF1C242F)
            )
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(items.size) {
                Column(
                    modifier = Modifier
                        .clickable {
                            viewModel.selectedItems = items[it].drawerItemsId
                        }
                        .background(if (items[it].drawerItemsId != viewModel.selectedItems) MaterialTheme.colors.surface else MaterialTheme.colors.primary)
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ){
                        Icon(Icons.Rounded.Bookmark, contentDescription = null, modifier = Modifier.padding(5.dp))
                        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                        Text(text = items[it].drawerItems, style = MaterialTheme.typography.body2, fontWeight = FontWeight.W600, modifier = Modifier.padding(6.dp))
                        // Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    }
                }
            }
        }
     //   Spacer(modifier = Modifier.padding(vertical = 5.dp))
        Divider(modifier = Modifier.fillMaxWidth().padding(5.dp).height(1.dp))
        /*
        Button(onClick = {
            drawerItemsViewModel.AddDrawerItemsDatabase("记忆碎片")
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "点我创建新分类")
        }*/
        Row(
            modifier = Modifier.clickable{

            }.padding(8.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.padding(5.dp))
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            Text(text = "添加分类", style = MaterialTheme.typography.body2, fontWeight = FontWeight.W600, modifier = Modifier.padding(6.dp))
        }
    }
}


@Composable
fun DisplayDrawerContent(viewModel:UiModel, drawerItemsViewModel: DrawerItemsViewModel, scaffoldState: ScaffoldState, scope: CoroutineScope) {
    MyTheme(viewModel = viewModel) {

        val drawerItems: List<DrawerItems>? by drawerItemsViewModel.allDrawerItems.observeAsState()


        DrawerInfo(drawerItems!!, drawerItemsViewModel)

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