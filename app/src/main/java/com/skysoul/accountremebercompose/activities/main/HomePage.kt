package com.skysoul.accountremebercompose.activities.main

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.skysoul.accountremebercompose.R
import com.skysoul.accountremebercompose.activities.main.ClickView.SEARCH
import com.skysoul.accountremebercompose.ui.HSpace16
import com.skysoul.accountremebercompose.ui.VSpace
import com.skysoul.accountremebercompose.ui.bar.Action
import com.skysoul.accountremebercompose.ui.bar.topBar
import com.skysoul.accountremebercompose.ui.dialog.TextDialog
import com.skysoul.accountremebercompose.utils.gestureClick
import com.skysoul.accountremebercompose.utils.log
import com.skysoul.utils.ToastUtil
import kotlinx.coroutines.launch

/**
 *@author shenqichao
 *Created on 2022/9/8
 *@Description
 */

enum class ClickView {
    HEADER,
    LOGOUT,
    MENU_SETTING,
    MENU_BACKUP,
    MENU_SHARE,
    MENU_RATING,
    Opt_ADD,
    Account,
    SEARCH,
}

@Composable
fun HomePage(
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    mViewModel: MainViewModel,
    clickListener: (ClickView, any: Any?) -> Unit,
) {

    val scope = rememberCoroutineScope()
//    val animationTransition = updateTransition(targetState = mViewModel.editting,"")
//    val orgBarAlpha by animateFloatAsState(targetValue = if(mViewModel.editting) 0f else 1f
//    , animationSpec = tween(400), label = "原始topbar alpha"
//    )

    var checkedAccountIds = remember {
        arrayListOf<Int>().toMutableStateList()
    }
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
            DrawerView(mViewModel = mViewModel, clickListener = {
                when (it) {
                    ClickView.MENU_BACKUP -> {}
                    ClickView.MENU_SHARE -> {}
                    ClickView.MENU_RATING -> {}
                    else -> clickListener.invoke(it, null)
                }
            })
        }

    }) {
        val scrollState = rememberLazyListState()
        val showFbt: androidx.compose.runtime.State<Boolean> = remember {
            derivedStateOf {
                !scrollState.isScrollInProgress
            }
        }
        Scaffold(
            modifier = Modifier
                .fillMaxWidth(),
//            .statusBarsPadding(),
            topBar = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(), color = Color.White
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                            .shadow(4.dp)
                    ) {
//                        Crossfade(targetState = mViewModel.editting, label = "exchange bar state") {
                        if (!mViewModel.editting) {
                            topBar(
                                actionsLeft = arrayOf(Action.IconAction(Icons.Filled.Menu) {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }),
                                Modifier.shadow(0.dp),
                                title = "账易密",
                                actions = arrayOf(
                                    Action.IconAction(Icons.Filled.Search) {
                                        clickListener.invoke(SEARCH,null)
                                    },

                                    Action.TextAction("编辑") {
                                        checkedAccountIds.clear()
                                        mViewModel.editting = true
                                    })
                            )
                        } else {
                            topBar(
                                showBack = true,
                                Modifier.shadow(0.dp),
                                title = "编辑中",
                                onBackPressed = {
                                    checkedAccountIds.clear()
                                    mViewModel.editting = false
                                },
                                actions = arrayOf(Action.TextAction("删除所选") {
                                    mViewModel.deleteSelect(
                                        checkedAccountIds
                                    )
                                })
                            )
                        }
//                        }


                    }
                }

            },
            floatingActionButton = {
                AnimatedVisibility(
                    visibleState = MutableTransitionState(!showFbt.value).apply {
                        targetState = showFbt.value
                    }, enter = scaleIn(), exit = scaleOut()
                ) {
                    FloatingActionButton(onClick = {
                        clickListener.invoke(ClickView.Opt_ADD, null)
                    }, shape = CircleShape) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                    }
                }

            }
        ) { it ->
            Box(Modifier.padding(it)) {

                var cated = mViewModel.cateSelected
                val accouts =
                    mViewModel.accountsFlow(cated!!).collectAsState(initial = arrayListOf())

                Row {
                    cardTabs(mViewModel)

                    LazyColumn(state = scrollState) {
                        accouts.run {
                            itemsIndexed(value) { index, item ->
                                if (index == 0) {
                                    VSpace(dp = 12)
                                }
                                Card(modifier = Modifier.padding(horizontal = 12.dp)) {
                                    Row(Modifier
                                        .gestureClick(key = item, onLongClick = {
                                            if (mViewModel.editting) {
                                            } else {
                                                clickListener.invoke(
                                                    ClickView.Account,
                                                    value.get(index)
                                                )
                                            }
                                        }) {
                                            Log.d(
                                                "sqc",
                                                "index is $index item name is ${item.accountName}"
                                            )
                                            if (mViewModel.editting) {
//                                        var item = value.get(index)
                                                if (checkedAccountIds.contains(item.id)) {
                                                    checkedAccountIds.remove(item.id)
                                                } else {
                                                    checkedAccountIds.add(item.id)
                                                }
                                            } else {
                                                //showtip
//                                        showTipState.value = true
                                                mViewModel.showTipAccount.value = value.get(index)
                                            }
                                        }
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AnimatedVisibility(visible = mViewModel.editting) {
                                            Checkbox(
                                                checked = checkedAccountIds.contains(item.id),
                                                onCheckedChange = {
                                                    log("cb index is $index item name is ${item.accountName}")

                                                    if (it) {
                                                        checkedAccountIds.add(item.id)
                                                    } else {
                                                        checkedAccountIds.remove(item.id)
                                                    }
                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = Color.Blue,
                                                    checkmarkColor = Color.White
                                                )
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = item.platform,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            VSpace(dp = 12)
                                            Text(text = item.accountName, fontSize = 14.sp)

                                        }
                                    }
                                }
                                VSpace(dp = 12)
                            }
                        }
                    }

                }
                ToastUtil.toast("lsjdlfjsljdflsflj")
//                AnimatedVisibility(visible = showFbt) {
//                    FloatingActionButton(onClick = {
//                        clickListener.invoke(ClickView.Opt_ADD, null)
//                    }, shape = CircleShape) {
//                        Icon(imageVector = Icons.Filled.Add, contentDescription = "")
//                    }
//                }

                var showTipState = mViewModel.showTip.collectAsState(initial = false)
                var account = mViewModel.showTipAccount.collectAsState()

                TextDialog(
                    showState = showTipState as MutableState<Boolean>,
                    title = "密码提示",
                    content = account.value?.tip ?: "",
                    okText = "查看详情",
                    callbackOk = {
                        clickListener.invoke(ClickView.Account, mViewModel.showTipAccount.value)
                        mViewModel.showTipAccount.value = null
                        showTipState.value = false
                    },
                    cancelText = "确定",
                    callbackDismiss = {
                        mViewModel.showTipAccount.value = null
                        showTipState.value = false
                    })
            }
        }
    }
}

@Composable
fun cardTabs(mViewModel: MainViewModel) {
    LazyColumn(
        Modifier
            .fillMaxHeight()
            .width(50.dp)
            .background(Color.LightGray)
            .padding(vertical = 12.dp)
    ) {
        itemsIndexed(mViewModel.cateList) { index, item ->

            Column {
                if (index == 0) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .drawBehind {
                                var r = 8.dp.toPx()
                                var spac = 6.dp.toPx()
                                if (item == mViewModel.cateSelected) {
                                    Path().apply {
                                        moveTo(size.width, size.height)
                                        relativeLineTo(-r, 0f)
                                        relativeQuadraticBezierTo(r, 0f, r, -r)
                                        lineTo(size.width, size.height)
                                        drawPath(this, Color.White)
                                    }
                                }
                            }
                    )
                }

                Box(Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        var r = 8.dp.toPx()
                        var spac = 6.dp.toPx()
                        if (item == mViewModel.cateSelected) {
                            Path().apply {
                                moveTo(size.width, 0f)
//                            lineTo(0f,0f)
//                            lineTo(0f,size.height)
//                            lineTo(size.width,size.height)
                                relativeLineTo(spac + r - size.width, 0f)
                                relativeQuadraticBezierTo(-r, 0f, -r, r)
                                lineTo(spac, size.height - r)
                                relativeQuadraticBezierTo(0f, r, r, r)
                                lineTo(size.width, size.height)
                                lineTo(size.width, 0f)
                                drawPath(this, Color.White)
                            }
                        } else if (index > 0 && mViewModel.cateList.get(index - 1) == mViewModel.cateSelected) {
                            //上一个是选中的
                            Path().apply {
                                moveTo(size.width, 0f)
                                relativeLineTo(-r, 0f)
                                relativeQuadraticBezierTo(r, 0f, r, r)
                                lineTo(size.width, 0f)
                                drawPath(this, Color.White)
                            }
                        } else if (index < mViewModel.cateList.size - 1 && mViewModel.cateList.get(
                                index + 1
                            ) == mViewModel.cateSelected
                        ) {
                            Path().apply {
                                moveTo(size.width, size.height)
                                relativeLineTo(-r, 0f)
                                relativeQuadraticBezierTo(r, 0f, r, -r)
                                lineTo(size.width, size.height)
                                drawPath(this, Color.White)
                            }
                        }

                    }
                    .padding(vertical = 8.dp)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = null,
                    ) {
                        mViewModel.cateSelected = item

                    }, contentAlignment = Alignment.Center
                ) {
                    Column {
                        item.cateName.forEach {
                            Text(text = it.toString())
                        }
                    }

                }

                if (index == mViewModel.cateList.size - 1) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .drawBehind {
                                var r = 8.dp.toPx()
                                var spac = 6.dp.toPx()
                                if (item == mViewModel.cateSelected) {
                                    Path().apply {
                                        moveTo(size.width, 0f)
                                        relativeLineTo(-r, 0f)
                                        relativeQuadraticBezierTo(r, 0f, r, r)
                                        lineTo(size.width, 0f)
                                        drawPath(this, Color.White)
                                    }
                                }
                            }
                    )
                }
            }


        }
    }
}

@Composable
fun DrawerView(mViewModel: MainViewModel, clickListener: (ClickView) -> Unit) {
    DrawerHeader(mViewModel = mViewModel, clickListener = clickListener)
    DrawerMenu(
        menu = DMenu(R.drawable.ic_menu_settings, "设置", ClickView.MENU_SETTING),
        clickListener = clickListener
    )
    DrawerMenu(
        menu = DMenu(R.drawable.ic_menu_backup, "备份", ClickView.MENU_BACKUP),
        clickListener = clickListener
    )
    DrawerMenu(
        menu = DMenu(R.drawable.ic_menu_share, "分享", ClickView.MENU_SHARE),
        clickListener = clickListener
    )
    DrawerMenu(
        menu = DMenu(R.drawable.ic_menu_rate, "评分", ClickView.MENU_RATING),
        clickListener = clickListener
    )
}

data class DMenu(val id: Int, val text: String, val clickView: ClickView)

@Composable
fun DrawerMenu(menu: DMenu, clickListener: (ClickView) -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { clickListener.invoke(menu.clickView) }) {
        HSpace16()
        Icon(painter = painterResource(id = menu.id), contentDescription = "")
        HSpace16()
        Text(text = menu.text)
    }

}

@Composable
fun DrawerHeader(mViewModel: MainViewModel, clickListener: (ClickView) -> Unit) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(192.dp)
            .background(Color.Black)
    ) {
        val (header, nick, logout) = createRefs()
        Image(painter = painterResource(id = R.mipmap.sidebar_headportrait),
            contentDescription = "",
            Modifier
                .size(60.dp)
                .constrainAs(header) {
                    start.linkTo(parent.start, 24.dp)
                    centerVerticallyTo(parent)
                }
                .clickable {
                    clickListener.invoke(ClickView.HEADER)
                })
        Text(
            text = mViewModel.user.value?.nickName ?: "",
            Modifier.constrainAs(nick) {
                top.linkTo(header.top)
                start.linkTo(header.end, 12.dp)
            },
            fontSize = 18.sp,
            color = Color.White
        )
        Button(onClick = { mViewModel.logout() }, Modifier.constrainAs(logout) {
            end.linkTo(parent.end, 24.dp)
            bottom.linkTo(parent.bottom, 24.dp)
        }) {
            Text(text = "登出", color = Color.White, fontSize = 16.sp)
        }
    }

}

@Preview
@Composable
fun test() {
//    DrawerHeader(mViewModel = viewModel(), clickListener = {})
    FloatingActionButton(
        onClick = { /*TODO*/ },
        contentColor = Color.White,
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "")
    }
}
