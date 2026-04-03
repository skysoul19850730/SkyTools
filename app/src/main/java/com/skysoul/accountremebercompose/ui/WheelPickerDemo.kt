package com.skysoul.accountremebercompose.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * WheelPicker 使用示例
 */
@Composable
fun WheelPickerDemo() {
    var selectedItem by remember { mutableStateOf("") }
    val items = remember {
        mutableStateListOf("选项 1", "选项 2", "选项 3", "选项 4", "选项 5", "选项 6", "选项 7", "选项 8", "选项 9", "选项 10")
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "选择的项目: $selectedItem",
            modifier = Modifier.padding(16.dp)
        )
        
        WheelPicker(
            items = items,
            onItemSelected = { index, item ->
                selectedItem = item
            },
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(onClick = { /*TODO*/ }) {
            Text("确认选择")
        }
    }
}
