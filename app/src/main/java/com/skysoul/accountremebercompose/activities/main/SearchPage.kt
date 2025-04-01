package com.skysoul.accountremebercompose.activities.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import com.skysoul.utils.ToastUtil

/**
 *@author shenqichao
 *Created on 2025/4/1
 *@Description
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(
    mViewModel: MainViewModel,
    clickListener: (ClickView) -> Unit,
) {
    val searchText = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    var expand by remember { mutableStateOf(false) }

   Box(modifier = Modifier.fillMaxSize().semantics { isTraversalGroup = true }){
       SearchBar(modifier = Modifier.align(Alignment.TopCenter).semantics { traversalIndex = 0f },
           inputField = {
               SearchBarDefaults.InputField(
                   query = searchText.value,
                   onQueryChange = {},
                   onSearch = {
                       ToastUtil.showToast("search $searchText")
                   },
                   expanded = expand,
                   onExpandedChange = {
                       expand = it
                   },

               )
           },
           expanded = expand,
           onExpandedChange = {
               expand = it
           }
       ) { }
   }


}