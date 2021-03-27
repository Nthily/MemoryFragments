package com.example.fragmentsofmemory

import android.content.ContentValues
import android.media.Image
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.fragmentsofmemory.ui.theme.MyTheme


class UiModel: ViewModel(){
   // var theme by mutableStateOf(MyTheme.Theme.Dark)
    var lightTheme by mutableStateOf(true)
    //  var currentPage: AddNewTreeHoles? by mutableStateOf(null)
    var maining by mutableStateOf(true)

    var adding by mutableStateOf(false)

    var draweringPage by mutableStateOf(false)
    var requestCloseDrawerPage by mutableStateOf(false)

    var selectedItems by mutableStateOf(1)

    var reading by mutableStateOf(false)

    var testTxt by mutableStateOf("")
    var userName by mutableStateOf("")
    val iconSize = Modifier.size(18.dp)

    var timing by mutableStateOf(false)

    var timeResult by mutableStateOf("")

    var selectedTime by mutableStateOf(false)

    var cardId by mutableStateOf(0)

    var textModify by mutableStateOf("")
    var editing by mutableStateOf(false)

    var currentCategory by mutableStateOf(1)


    fun endReading() {
        reading = false
        maining = true
        cardId = 0
        timeResult = ""
    }



    @Composable
    fun SetBackground(background: Int): Unit{
        return Image(painter = painterResource(id = background), contentDescription = null,
            modifier = Modifier
            .fillMaxSize(),
            contentScale = ContentScale.Crop)
    }

    @Composable
    fun SetSecBackground(background: Int): Unit{
        return Image(painter = painterResource(id = background), contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop)
    }


    fun startEdit(cardID:Int, userContent:String, time:String) {
        cardId = cardID
        editing = true
        adding = true
        maining = false
        textModify = userContent
        timeResult = time
    }

    fun endAddPage() {
        adding = false
        maining = true
        selectedTime = false
        timeResult = ""
        editing = false
        textModify = ""
    }


    fun deleteCard(userCardViewModel: UserCardViewModel) {
        userCardViewModel.removeDataBase(cardId)
        reading = false
        maining = true
        timeResult = ""
        textModify = ""
    }

}
