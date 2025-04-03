package com.skysoul.accountremebercompose.activities.main

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skysoul.accountremebercompose.data.dbroom.entities.SimpleAccount
import com.skysoul.accountremebercompose.ui.VSpace
import com.skysoul.accountremebercompose.utils.gestureClick
import com.skysoul.accountremebercompose.utils.log

/**
 *@author shenqichao
 *Created on 2025/4/3
 *@Description
 */
enum class AccountItemClick{
    SINGLE_CLICK,
    LONG_CLICK,
}
@Composable
fun AccountItem(item: SimpleAccount,isEditing:Boolean = false, clickListener: (AccountItemClick, any: Any?) -> Unit){
    Card(modifier = Modifier.padding(horizontal = 12.dp)) {
        Row(Modifier
            .gestureClick(key = item, onLongClick = {
                if (isEditing) {
                } else {
                    clickListener.invoke(
                        AccountItemClick.LONG_CLICK,
                        item
                    )
                }
            }) {
//                Log.d(
//                    "sqc",
//                    "index is $index item name is ${item.accountName}"
//                )
                if (isEditing) {
//                                              var item = value.get(index)
                    item.isChecked.value = !item.isChecked.value
                } else {
                    //showtip
                    clickListener.invoke(AccountItemClick.SINGLE_CLICK,item)
                }
            }
            .fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = isEditing) {
                Checkbox(
                    checked = item.isChecked.value,
                    onCheckedChange = {
                        item.isChecked.value = it
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
}