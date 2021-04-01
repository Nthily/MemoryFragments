package com.example.fragmentsofmemory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.fragmentsofmemory.fragments.popUpDrawer

class Welcome : AppCompatActivity() {

    private val userCardViewModel by viewModels<UserCardViewModel>()

    private val userInfoViewModel by viewModels<UserInfoViewModel>()

    private val dialogViewModel:DialogViewModel by viewModels()
    private val viewModel:UiModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val navController = rememberNavController()
               NavHost(navController, startDestination = "welcome") {
                composable("welcome") { Test(navController) }
                composable("mainpage") {
                    Text("hello")
                }
            }
        }
    }
}

@Composable
fun Test(navController: NavController) {
    Button(onClick = {
        navController.navigate("mainpage")
    }) {
        Text(text = "跳转")
    }
}