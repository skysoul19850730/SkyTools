package com.skysoul.accountremebercompose.ui.bar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skysoul.accountremebercompose.ui.HSpace16

/**
 *@author shenqichao
 *Created on 2022/8/31
 *@Description
 */
sealed class Action {
    class TextAction(var s: String,val modifier: Modifier = Modifier, val click: OnClick) : Action()
    class IconAction(var id: ImageVector,val modifier: Modifier = Modifier, val click: OnClick) : Action()
}
typealias OnClick = () -> Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBar(
    actionsLeft: Array<Action>,
    modifier: Modifier = Modifier,
    title: String,
    vararg actions: Action
) {
    TopAppBar(
        title = {
            Text(
                text = title, textAlign = TextAlign.Center,
            )
        },
        modifier =Modifier.shadow(4.dp).then(modifier),
        navigationIcon = {
            if (!actionsLeft.isNullOrEmpty()) {
                actionsLeft.forEach {
                    when (it) {
                        is Action.TextAction -> {
                            Text(text = it.s, modifier = Modifier.clickable { it.click.invoke() })
                        }

                        is Action.IconAction -> {
                            IconButton(onClick = { it.click.invoke() }) {
                                Icon(
                                    imageVector = it.id,
                                    contentDescription = "",
                                )
                            }
                        }
                    }
                }
            }
        },
        actions = {
            if (actions.isNullOrEmpty()) {
                Spacer(modifier = Modifier.width(68.dp))
            } else {
                actions.forEach {
                    HSpace16()
                    when (it) {
                        is Action.TextAction -> {
                            Text(text = it.s, modifier = it.modifier.then(Modifier.clickable { it.click.invoke() }))
                        }

                        is Action.IconAction -> {
                            Icon(
                                imageVector = it.id,
                                contentDescription = "",
                                modifier = it.modifier.then(Modifier.clickable { it.click.invoke() }))
                        }
                    }
                }
            }
            HSpace16()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBar(
    showBack: Boolean,
    modifier: Modifier = Modifier,
    title: String,
    onBackPressed: () -> Unit,
    vararg actions: Action
) {
    TopAppBar(
        title = {
            Text(
                text = title, textAlign = TextAlign.Center,
            )
        },
        modifier =  Modifier.shadow(4.dp).then(modifier),
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back"
                    )
                }
            }
        },
        actions = {
            if (actions.isNullOrEmpty()) {
                Spacer(modifier = Modifier.width(68.dp))
            } else {
                actions.forEach {
                    when (it) {
                        is Action.TextAction -> {
                            Text(text = it.s, modifier = Modifier.clickable { it.click.invoke() })
                        }

                        is Action.IconAction -> {
                            Icon(
                                imageVector = it.id,
                                contentDescription = "",
                                modifier = Modifier.clickable { it.click.invoke() })
                        }
                    }
                }
            }
            HSpace16()
        },
    )
}

@Composable
fun backTopBar(title: String, onBackPressed: () -> Unit) {
    topBar(showBack = true, title = title, onBackPressed = onBackPressed)
}

@Composable
fun backOverBar(title: String, onBackPressed: () -> Unit, onOver: () -> Unit) {
    topBar(
        showBack = true,
        modifier=Modifier,
        title = title,
        onBackPressed = onBackPressed,
        Action.TextAction("完成") { onOver.invoke() })
}


@Composable
fun backPage(
    title: String,
    onBackPressed: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val snackState = remember {
        SnackbarHostState()
    }
    Scaffold(
        topBar = {
            backTopBar(title = title, onBackPressed)
        },
        content = {
            Box(Modifier.padding(0.dp, it.calculateTopPadding(), 0.dp, 0.dp)) {
                content(it)
            }

        },
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackState,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.Center)
            )
        }
    )
}

@Composable
fun topBarPage(
    topbar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val hostState = remember {
        SnackbarHostState()
    }
    Scaffold(
        topBar = topbar,
        content = {
            Box(Modifier.padding(0.dp, it.calculateTopPadding(), 0.dp, 0.dp)) {
                content(it)
            }

        },
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        snackbarHost = {
            SnackbarHost(
                hostState = hostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.Center)
            )
        }
    )
}