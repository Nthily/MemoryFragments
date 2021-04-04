package com.example.fragmentsofmemory.fragments
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.registerForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
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
                   userCardViewModel: UserCardViewModel,
                   categoryItems: List<CategoryCardCount>,
                   context: Context,
                   file:File,
                   user:UserInfo) {
    // 分类栏
    AnimatedVisibility(
        visible = !viewModel.editingProfile,
        enter = fadeIn( animationSpec = tween(durationMillis = 1550)),
        exit = fadeOut( animationSpec = tween(durationMillis = 550))
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(items.size) {
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
                    LaunchedEffect(viewModel.hasAnyExtraButtonRevealed) {
                        if(!viewModel.hasAnyExtraButtonRevealed) {
                            swipeableState.animateTo(0)
                        }
                    }

                    /*LaunchedEffect(viewModel.requestCloseDrawer) { //关闭 Drawer
                            swipeableState.animateTo(0)
                            Log.d(TAG, "wtf ++++ ")
                            viewModel.requestCloseDrawer = false
                    }*/

                    val anim = rememberCoroutineScope()

                    Box(){
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
                                        userCardViewModel.updateLastSelected(
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

                        LaunchedEffect(viewModel.editingCategory) {  // 检测编辑分类对话框是否已关闭
                            if(!viewModel.editingCategory) {
                                viewModel.hasAnyExtraButtonRevealed = false
                            }
                        }
                        androidx.compose.animation.AnimatedVisibility(visible = editable) {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(), horizontalAlignment = Alignment.End) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ){
                                    IconButton(onClick = {
                                        viewModel.editingCategory = true
                                        viewModel.editingCategoryUid = items[it].uid

                                        //   Log.d(TAG, "uid + ${viewModel.editingCategoryUid} \n name + ${viewModel.editingCategoryName}")
                                    }, modifier = Modifier
                                        .background(Color(0xFF7F849F))
                                        .padding(1.dp)) {
                                        Icon(Icons.Rounded.Edit, contentDescription = null)
                                    }
                                    IconButton(onClick = {
                                        userCardViewModel.deleteCategoryDataBase(items[it].uid)
                                        viewModel.hasAnyExtraButtonRevealed = false
                                    }, modifier = Modifier
                                        .background(Color(0xFFE65B65))
                                        .padding(1.dp)) {
                                        Icon(Icons.Rounded.Delete, contentDescription = null)
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
                        Text("暂无分类", style = MaterialTheme.typography.h6)
                    }
                }
            }
            Divider(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(1.dp)
            )

            LaunchedEffect(viewModel.addNewCategory) {  // 检测新建分类对话框是否已关闭
                if(!viewModel.addNewCategory) {
                    viewModel.hasAnyExtraButtonRevealed = false
                }
            }
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
    }
}


@Composable
fun UserInfoColumn(viewModel:UiModel,
                   items: List<DrawerItems>,
                   userCardViewModel: UserCardViewModel,
                   categoryItems: List<CategoryCardCount>,
                   context: Context,
                   file:File,
                   user:UserInfo) {

    /*
    val profileMinHeight = 160.dp   // 用户栏展开前高度
    val profileMaxHeight = 750.dp   // 展开后高度
    val profileHeight by animateDpAsState(
        targetValue = (if(viewModel.editingProfile) profileMaxHeight else profileMinHeight),
        animationSpec = tween(800)
    )

     */
    /*
    Column(modifier = Modifier
        .background(Color(90, 143, 185))
        .fillMaxWidth()
        .height(profileHeight)) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(start = 15.dp, top = 15.dp)){
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(shape = CircleShape)
                        .clickable {
                            getContent.launch("image/*")
                            /*
                            val sendIntent: Intent = Intent().apply { // 分享 Intent
                                action = Intent.ACTION_PICK
                                type = "image/*"
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "选择一张图片作为头像吧~")
                            //     startActivity(content, shareIntent, Bundle())
                            startActivityForResult(context, shareIntent, 1, Bundle())

                             */

                             */
                        },
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

            Column() {
                Text(text = user.userName,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.W900,
                    modifier = Modifier.padding(start = 15.dp, top = 15.dp),
                    color = Color.White)
                Text(text = "永远相信美好的事情即将发生",
                    style = MaterialTheme.typography.overline,
                    fontWeight = FontWeight.W900,
                    modifier = Modifier.padding(start = 15.dp, top = 8.dp),
                    color = Color.White)
            }
        }
    }

     */

     */
}

fun uploadPicture(context: Context, viewModel: UiModel) {
    try {
        viewModel.userAvatarUploading = File.createTempFile("avatar", null)
        Log.d(TAG, " wtf wtf wtf ${viewModel.userAvatarUploading!!.absolutePath}")
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
        Toast.makeText(context, "读取失败", Toast.LENGTH_LONG).show()
    }
}


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DrawerInfo(viewModel:UiModel,
               items: List<DrawerItems>,
               userCardViewModel: UserCardViewModel,
               categoryItems: List<CategoryCardCount>,
               context: Context,
               file:File,
               user:UserInfo) {

  //  val context = LocalContext.current
  //  val file = File(context.getExternalFilesDir(null), "picture.jpg")

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if(it != null) {
            viewModel.imageUriState.value = it
            uploadPicture(context, viewModel)
        }
    }

    // 编辑信息栏
    AnimatedVisibility(
        visible = viewModel.editingProfile,
        enter = fadeIn( animationSpec = tween(durationMillis = 1000)),
        exit = fadeOut( animationSpec = tween(durationMillis = 500))
    ) {

        Box{
            Image(painter = painterResource(id = R.drawable.edit_bkg), contentDescription = null,modifier = Modifier
                .fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Card(
                    backgroundColor = Color(255, 255, 255 , 235),
                    elevation = 12.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                ) {
                    // 另一种编辑的界面，已被作者本人抛弃了
                    /*
                    Row(
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center){
                            Surface(
                                shape = CircleShape,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(60.dp)
                                    .clip(shape = CircleShape)
                                    .clickable {
                                        getContent.launch("image/*")
                                    },
                                border = BorderStroke(2.dp, Color.DarkGray)
                            ) {
                                viewModel.InitUserProfilePic(context)
                            }
                            Text("编辑头像",
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                color = Color.DarkGray)
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 15.dp , end = 25.dp, top = 15.dp, bottom = 15.dp),
                            verticalArrangement = Arrangement.Center
                        ) {

                            /*
                            val focusRequester = FocusRequester()
                            LaunchedEffect(true) {
                                focusRequester.requestFocus()
                            }

                             */
                            OutlinedTextField(value = user.userName, onValueChange = {
                                userCardViewModel.updateLastSelected(user.uid, it, user.last!!, user.signature)
                            },
                                modifier = Modifier
                                    .height(60.dp),
                                //    .focusRequester(focusRequester),
                                label = {Text("你的名字")},
                                singleLine = true,
                                textStyle = MaterialTheme.typography.caption
                            )
                            Spacer(modifier = Modifier.padding(vertical = 10.dp))

                            OutlinedTextField(value = user.signature, onValueChange = {
                                userCardViewModel.updateLastSelected(user.uid, user.userName, user.last!!, it)
                            },
                                modifier = Modifier.height(150.dp),
                                label = {Text("你的签名/状态")},
                                textStyle = MaterialTheme.typography.caption
                            )
                        }
                    }*/

                     */
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){


                        val enableCancelButton = mutableStateOf(true)
                        val focusRequester = FocusRequester()

                        Surface(
                            shape = CircleShape,
                            modifier = Modifier
                                .padding(start = 13.dp, top = 13.dp, end = 13.dp)
                                .size(70.dp)
                                .clip(shape = CircleShape)
                                .clickable {
                                    getContent.launch("image/*")
                                },
                            border = BorderStroke(2.dp, Color.DarkGray)
                        ) {
                            viewModel.InitUserProfilePic(context)
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 26.dp, end = 26.dp, top = 15.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            OutlinedTextField(value = user.userName, onValueChange = {
                                userCardViewModel.updateLastSelected(user.uid, it, user.last!!, user.signature)
                            },
                                modifier = Modifier
                                    .height(60.dp)
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester)
                                    .onFocusChanged {
                                        enableCancelButton.value = !enableCancelButton.value
                                    },

                                label = {Text("你的名字")},
                                singleLine = true,
                                textStyle = MaterialTheme.typography.button,
                                trailingIcon = {
                                    AnimatedVisibility(visible = enableCancelButton.value) {
                                        IconButton(
                                            onClick = {
                                                userCardViewModel.updateLastSelected(user.uid, "", user.last!!, user.signature)
                                            },
                                        ) {
                                            Icon(Icons.Filled.Cancel, null)
                                        }
                                    }
                                },

                                )

                            Spacer(modifier = Modifier.padding(vertical = 10.dp))

                            OutlinedTextField(value = user.signature, onValueChange = {
                                userCardViewModel.updateLastSelected(user.uid, user.userName, user.last!!, it)
                            },
                                modifier = Modifier
                                    .height(150.dp)
                                    .fillMaxWidth(),
                                label = {Text("你的签名/状态")},
                                textStyle = MaterialTheme.typography.button
                            )
                        }
                    }
                }
            }
        }
    }

    // 用户栏
    AnimatedVisibility(
        visible = !viewModel.editingProfile,
        enter = fadeIn( animationSpec = tween(durationMillis = 1200)),
        exit = fadeOut( animationSpec = tween(durationMillis = 1500))
    ) {
        Column(modifier = Modifier
            .background(Color(90, 143, 185))
            .fillMaxWidth()
            .height(160.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(start = 15.dp, top = 15.dp)){
                    Surface(
                        shape = CircleShape,
                        modifier = Modifier
                            .size(70.dp)
                            .clip(shape = CircleShape)
                    ) {
                        viewModel.InitUserProfilePic(context)
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

                Column() {
                    Text(text = user.userName,
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier.padding(start = 15.dp, top = 15.dp),
                        color = Color.White)
                    Text(text = user.signature,
                        style = MaterialTheme.typography.overline,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier.padding(start = 15.dp, top = 8.dp),
                        color = Color.White)
                }
            }
        }
    }

    /*val switchMode = (
        if(viewModel.editingProfile) {
            Modifier
                .background(Color(90, 143, 185))
                .animateContentSize(tween(800))
                .fillMaxSize()
        }
        else {
            Modifier
                .background(Color(90, 143, 185))
                .animateContentSize(tween(800))
                .height(160.dp)
        }
    )*/



    // 分类栏
    CategoryColumn(viewModel, items, userCardViewModel, categoryItems, context, file, user)
}


@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DisplayDrawerContent(
    viewModel:UiModel,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    userCardViewModel: UserCardViewModel,
    categoryNum: List<CategoryCardCount>?,
    drawerItems: List<DrawerItems>,
    file: File,
    context: Context,
    user:UserInfo) {
    MyTheme(viewModel = viewModel) {

    //    val drawerItems: List<DrawerItems>? by userCardViewModel.drawer.observeAsState()

        LaunchedEffect(viewModel.requestCloseDrawer) {  // 检测关闭菜单请求
            if(viewModel.requestCloseDrawer) {
                scaffoldState.drawerState.close()
                scaffoldState.drawerState.overflow
                viewModel.requestCloseDrawer = false
            }
        }


        LaunchedEffect(scaffoldState.drawerState.isOpen) {    // 检测菜单是否已经打开
            viewModel.draweringPage = true
        }


        LaunchedEffect(scaffoldState.drawerState.isClosed) {    // 检测菜单是否已通过任何方式关闭
            if(scaffoldState.drawerState.isClosed) {
                viewModel.hasAnyExtraButtonRevealed = false
                viewModel.draweringPage = false
                viewModel.editingProfile = false
            }
        }

        if (categoryNum != null) {
            DrawerInfo(viewModel, drawerItems, userCardViewModel, categoryNum, context, file, user)
        }

    }

}
