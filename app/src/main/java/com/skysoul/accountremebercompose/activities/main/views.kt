package com.skysoul.accountremebercompose.activities.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skysoul.accountremebercompose.activities.edit.EditActivity
import com.skysoul.accountremebercompose.data.dbroom.entities.SimpleAccount
import com.skysoul.accountremebercompose.ui.VSpace
import com.skysoul.accountremebercompose.ui.dialog.TextDialog
import com.skysoul.accountremebercompose.utils.gestureClick
import com.skysoul.appassistant.startKtxActivity

/**
 *@author shenqichao
 *Created on 2025/4/3
 *@Description
 */
enum class AccountItemClick {
    SINGLE_CLICK,
    LONG_CLICK,
    CHECKED_CHANGE
}

@Composable
fun AccountItem(
    item: SimpleAccount,
    allCheckedItems: SnapshotStateList<Int>? = null,
//    clickListener: (AccountItemClick, any: Any?) -> Unit
) {

    var showDialog = remember { mutableStateOf(false) }

    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current

    val gotoEdit :()->Unit ={
        viewModel.getDetailAccount(item.id) { acc ->
            context.startKtxActivity<EditActivity>("account" to acc)
        }
    }

    Card(modifier = Modifier.padding(horizontal = 12.dp)) {
        Row(Modifier
            .gestureClick(key = "${allCheckedItems}_${item.id}", onLongClick = {
                gotoEdit()
            }) {
                if (allCheckedItems==null) {
                    showDialog.value = true
                }else {
                    if(allCheckedItems.contains(item.id)){
                        allCheckedItems.remove(item.id)
                    }else{
                        allCheckedItems.add(item.id)
                    }
                }
            }
            .fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = allCheckedItems!=null) {
                Checkbox(
                    checked = allCheckedItems?.contains(item.id)?:false,
                    onCheckedChange = {
                        allCheckedItems?.let {
                            if(it.contains(item.id)){
                                it.remove(item.id)
                            }else{
                                it.add(item.id)
                            }
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


    TextDialog(
        showState = showDialog,
        title = "密码提示",
        content = item.tip,
        okText = "查看详情",
        callbackOk = {
            gotoEdit()
            showDialog.value = false
        },
        cancelText = "确定",
        callbackDismiss = {
            showDialog.value = false
        })


}