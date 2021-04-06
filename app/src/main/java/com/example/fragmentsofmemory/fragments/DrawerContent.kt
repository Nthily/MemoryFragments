package com.example.fragmentsofmemory.fragments
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.registerForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fragmentsofmemory.Database.CategoryCardCount
import com.example.fragmentsofmemory.Database.DrawerItems
import com.example.fragmentsofmemory.UiModel
import com.example.fragmentsofmemory.AppViewModel
import com.example.fragmentsofmemory.ui.theme.MyTheme
import kotlinx.coroutines.CoroutineScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import com.example.fragmentsofmemory.Database.UserInfo
import com.yalantis.ucrop.UCrop
import java.io.*
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun CategoryColumn(viewModel:UiModel,
                   items: List<DrawerItems>,
                   appViewModel: AppViewModel,
                   categoryItems: List<CategoryCardCount>,
                   context: Context,
                   file:File,
                   user:UserInfo) {
    // 分类栏
    /*AnimatedVisibility(
        visible = !viewModel.editingProfile,
        enter = fadeIn( animationSpec = tween(durationMillis = 1550)),
        exit = fadeOut( animationSpec = tween(durationMillis = 550))
    ) {*/

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            // 所有分类
            Box(){
                Column(
                    modifier = Modifier
                        .background(if (user.last != null) MaterialTheme.colors.surface else MaterialTheme.colors.primary)
                        .clickable {
                            if(!viewModel.editingProfile) {
                                viewModel.requestCloseDrawer = true
                                appViewModel.updateLastSelected(
                                    user.uid,
                                    user.userName,
                                    null,
                                    user.signature
                                )
                            }
                        }
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
                            text = "所有分类",
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
                                    text = "${appViewModel.allCards.value?.size ?: 0}",
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
            }

            // 自定义分类
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(items.size + 1) {     // +1 以避免无分类后 LazyColumn 不刷新
                    if(it < items.size) {

                        val squareSize = 96.dp

                        val swipeableState = rememberSwipeableState(0)


                        /*confirmStateChange = { wtf -> //是否启用应用动画,也就是最终会不会停留
                    when(true){
                        viewModel.requestOpenDrawer -> true
                        !viewModel.requestOpenDrawer -> false
                        else -> true
                    }*/

                        val sizePx = with(LocalDensity.current) { squareSize.toPx() }
                        val anchors = mapOf(0f to 0, sizePx to -1)

                        var editable by remember { mutableStateOf(false) }

                        editable = swipeableState.targetValue == -1
                        LaunchedEffect(editable) {   // 检测是否有任何地方被滑动
                            viewModel.hasAnyExtraButtonRevealed = editable
                        }

                        LaunchedEffect(viewModel.hasAnyExtraButtonRevealed) {   // 关闭分类滑动后的附加按钮
                            if (!viewModel.hasAnyExtraButtonRevealed) {
                                swipeableState.animateTo(0)
                            }
                        }

                        Box() {
                            Column(
                                modifier = Modifier
                                    .swipeable(
                                        enabled = (editable || !viewModel.hasAnyExtraButtonRevealed) && !viewModel.editingProfile,
                                        state = swipeableState,
                                        anchors = anchors,
                                        thresholds = { from, to ->
                                            FractionalThreshold(0.3f)
                                        },
                                        orientation = Orientation.Horizontal,
                                        reverseDirection = true,
                                    )
                                    .offset {
                                        IntOffset(-swipeableState.offset.value.roundToInt(), 0)
                                    }
                                    .clickable {
                                        if (!viewModel.hasAnyExtraButtonRevealed && !viewModel.editingProfile) {
                                            viewModel.requestCloseDrawer = true
                                           // viewModel.currentTitle = items[it].drawerItems
                                            appViewModel.updateLastSelected(
                                                user.uid,
                                                user.userName,
                                                items[it].uid,
                                                user.signature
                                            )
                                        } else {
                                            viewModel.hasAnyExtraButtonRevealed = false
                                        }
                                        //      viewModel.requestSelectPicture = true
                                    }

                                    .background(if (items[it].uid != user.last) MaterialTheme.colors.surface else MaterialTheme.colors.primary)
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

                            LaunchedEffect(viewModel.addNewCategory) {  // 检测添加分类对话框是否已关闭
                                if (!viewModel.addNewCategory) {
                                    viewModel.hasAnyExtraButtonRevealed = false
                                }
                            }
                            LaunchedEffect(viewModel.editingCategory) {  // 检测编辑分类名称对话框是否已关闭
                                if (!viewModel.editingCategory) {
                                    viewModel.hasAnyExtraButtonRevealed = false
                                }
                            }

                            androidx.compose.animation.AnimatedVisibility(visible = editable) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(), horizontalAlignment = Alignment.End
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        IconButton(
                                            onClick = { //
                                                viewModel.categoryName = items[it].drawerItems
                                                viewModel.editingCategory = true
                                                viewModel.editingCategoryUid = items[it].uid

                                            }, modifier = Modifier
                                                .background(Color(0xFF7F849F))
                                                .padding(1.dp)
                                        ) {
                                            Icon(Icons.Rounded.Edit, contentDescription = null)
                                        }
                                        IconButton(
                                            onClick = {
                                                editable = false
                                                viewModel.hasAnyExtraButtonRevealed = false
                                                appViewModel.deleteCategoryDataBase(items[it].uid)
                                            }, modifier = Modifier
                                                .background(Color(0xFFE65B65))
                                                .padding(1.dp)
                                        ) {
                                            Icon(Icons.Rounded.Delete, contentDescription = null)
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
            //   Spacer(modifier = Modifier.padding(vertical = 5.dp))
            if(items.isEmpty()) {
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .fillMaxHeight()
                ) {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("暂无自定义分类", style = MaterialTheme.typography.h6)
                    }
                }
            }
            Divider(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(1.dp)
            )

            Row(
                modifier = Modifier
                    .clickable {
                        if (!viewModel.editingProfile) viewModel.addNewCategory = true
                    }
                    .padding(8.dp)
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.padding(5.dp))
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Text(text = "添加分类", style = MaterialTheme.typography.body2, fontWeight = FontWeight.W600, modifier = Modifier.padding(6.dp))
            }
        }
    //}
}

/**
 * 菜单用户栏
 */
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun UserInfoColumn(viewModel:UiModel,
                   items: List<DrawerItems>,
                   appViewModel: AppViewModel,
                   categoryItems: List<CategoryCardCount>,
                   context: Context,
                   file:File,
                   user:UserInfo) {

    val focus = LocalFocusManager.current
    val koptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
    val kactions = KeyboardActions(onDone = { focus.clearFocus() })
    val keyboard = LocalSoftwareKeyboardController.current

    /**
     * 下拉动画
     */
    /*val profileMinHeight = 160.dp   // 用户栏展开前高度
    val profileMaxHeight = 750.dp   // 展开后高度
    val profileHeight by animateDpAsState(
        targetValue = (if(viewModel.editingProfile) profileMaxHeight else profileMinHeight),
        animationSpec = tween(800)
    )*/

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if(it != null) {
            viewModel.imageUriState.value = it
            uploadPicture(context, viewModel)
        }
    }

    val editModifier = Modifier
        .size(70.dp)
        .clip(shape = CircleShape)
        .clickable {
            getContent.launch("image/*")
        }

    val normalModifier = Modifier
        .size(70.dp)
        .clip(shape = CircleShape)



    Column(modifier = Modifier
        .background(Color(90, 143, 185))
        .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(start = 15.dp, top = 15.dp)){
                Surface(
                    shape = CircleShape,
                    modifier = if(viewModel.editingProfile) editModifier else normalModifier,
                    border = if(viewModel.editingProfile) BorderStroke(2.dp, Color.DarkGray) else BorderStroke(0.dp, Color.Transparent)
                ) {
                    viewModel.InitUserProfilePic()
                }
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {

                    CompositionLocalProvider(LocalContentColor provides Color.White) {
                        IconButton(onClick = {
                            viewModel.editingProfile = !viewModel.editingProfile
                        }) {
                            if(viewModel.editingProfile)Icon(Icons.Rounded.ArrowBack, contentDescription = "")
                            else Icon(Icons.Rounded.Edit, contentDescription = "")
                        }
                    }

                }
            }

            Column {
                /*Text(
                    text = user.userName,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.W900,
                    modifier = Modifier.padding(start = 15.dp, top = 15.dp, end = 15.dp),
                    color = Color.White
                )
                Text(
                    text = user.signature,
                    style = MaterialTheme.typography.overline,
                    fontWeight = FontWeight.W900,
                    modifier = Modifier.padding(
                        start = 15.dp,
                        top = 8.dp,
                        end = 15.dp,
                        bottom = 15.dp
                    ),
                    color = Color.White
                )*/
                var userName by remember { mutableStateOf(user.userName) }
                BasicTextField(
                    enabled = viewModel.editingProfile,
                    value = userName,
                    onValueChange = {
                        userName = it.replace("\n", "")
                        if (userName.isNotBlank()) {
                            appViewModel.updateLastSelected(
                                user.uid,
                                userName.trimEnd(),
                                user.last,
                                user.signature
                            )
                        }
                    },
                    modifier = Modifier.height(41.dp).padding(start = 15.dp, top = 10.dp, end = 15.dp),
                    textStyle = MaterialTheme.typography.body2.copy(
                        color = Color.White, fontWeight = FontWeight.W900
                    ),
                    singleLine = true,
                    maxLines = 1,
                    decorationBox = { textField ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {

                            Column() {
                                textField()
                                Spacer(Modifier.height(2.dp))
                                AnimatedVisibility(
                                    visible = viewModel.editingProfile,
                                    enter = fadeIn(animationSpec = tween(300)),
                                    exit = fadeOut(animationSpec = tween(300))
                                ) {
                                    Divider(
                                        color = if (userName.isNotBlank()) Color.White else Color(
                                            0xFFF74853
                                        ), thickness = 2.dp
                                    )
                                }
                            }
                        }
                    },
                    cursorBrush = SolidColor(Color.White),
                    keyboardOptions = koptions,
                    keyboardActions = kactions
                )

                var userSignature by remember { mutableStateOf(user.signature) }
                BasicTextField(
                    enabled = viewModel.editingProfile,
                    value = userSignature,
                    onValueChange = {
                        userSignature = it
                        appViewModel.updateLastSelected(
                            user.uid,
                            user.userName,
                            user.last,
                            userSignature.trimEnd()
                        )
                    },
                    modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 10.dp),
                    textStyle = MaterialTheme.typography.overline.copy(
                        color = Color.White, fontWeight = FontWeight.W900
                    ),
                    decorationBox = { textField ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {

                            Column() {
                                textField()
                                Spacer(Modifier.height(2.dp))
                                AnimatedVisibility(
                                    visible = viewModel.editingProfile,
                                    enter = fadeIn(animationSpec = tween(300)),
                                    exit = fadeOut(animationSpec = tween(300))
                                ) {
                                    Divider(color = Color.White, thickness = 2.dp)
                                }
                            }
                        }
                    },
                    cursorBrush = SolidColor(Color.White),
                    maxLines = 5,
                    keyboardOptions = koptions,
                    keyboardActions = kactions
                )
            }
        }
    }

}


fun uploadPicture(context: Context, viewModel: UiModel) {
    try {
        viewModel.userAvatarUploading = File.createTempFile("avatar", null)
        val options = UCrop.Options()
        options.setCircleDimmedLayer(true) // 使用圆形裁剪
        options.setShowCropGrid(false)  // 不显示头像选取的网格
        options.setShowCropFrame(false) // 不显示头像选取的边框

        UCrop.of(viewModel.imageUriState.value!!, Uri.fromFile(viewModel.userAvatarUploading))
            .withAspectRatio(1.0F, 1.0F).withOptions(options)
            .start(context as Activity)

        /*val getIS = context.contentResolver.openInputStream(viewModel.imageUriState.value!!) //基本上传文件代码
        val os = FileOutputStream(file)
        val data = ByteArray(getIS!!.available())
        getIS.read(data)
        os.write(data)
        getIS.close()
        os.close()*/

    } catch (e:IOException) {
        Toast.makeText(context, "头像读取失败", Toast.LENGTH_LONG).show()
    }
}


@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DrawerInfo(viewModel:UiModel,
               items: List<DrawerItems>,
               appViewModel: AppViewModel,
               categoryItems: List<CategoryCardCount>,
               context: Context,
               file:File,
               user:UserInfo) {
    // 用户栏
    // UserInfoColumn
    UserInfoColumn(viewModel, items, appViewModel, categoryItems, context, file, user)

    // 分类栏
    // CategoryColumn
    CategoryColumn(viewModel, items, appViewModel, categoryItems, context, file, user)

}


@ExperimentalComposeUiApi
@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DisplayDrawerContent(
    viewModel:UiModel,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    appViewModel: AppViewModel,
    categoryNum: List<CategoryCardCount>?,
    drawerItems: List<DrawerItems>,
    file: File,
    context: Context,
    user:UserInfo) {
    MyTheme(viewModel = viewModel) {

        val titler = {
            viewModel.currentTitle = if(user.last == null) "所有记忆碎片" else drawerItems.find { it.uid == user.last }?.drawerItems ?: "Error"
        }
        LaunchedEffect(user.last) {
            titler()
        }
        LaunchedEffect(viewModel.editingCategory) {
            if(!viewModel.editingCategory) {
                titler()
            }
        }

    //    val drawerItems: List<DrawerItems>? by userCardViewModel.drawer.observeAsState()

        LaunchedEffect(viewModel.requestCloseDrawer) {  // 检测关闭菜单请求
            if(viewModel.requestCloseDrawer) {
                scaffoldState.drawerState.close()
                scaffoldState.drawerState.overflow
                viewModel.requestCloseDrawer = false
            }
        }


        LaunchedEffect(scaffoldState.drawerState.isOpen) {    // 检测菜单是否已经打开
            if(scaffoldState.drawerState.isOpen) {
                viewModel.draweringPage = true
            }

        }


        LaunchedEffect(scaffoldState.drawerState.isClosed) {    // 检测菜单是否已通过任何方式关闭
            if(scaffoldState.drawerState.isClosed) {
                viewModel.hasAnyExtraButtonRevealed = false
                viewModel.draweringPage = false
                viewModel.editingProfile = false
            }
        }

        if (categoryNum != null) {
            DrawerInfo(viewModel, drawerItems, appViewModel, categoryNum, context, file, user)
        }


    }

}
