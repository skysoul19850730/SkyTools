package com.skysoul.accountremebercompose.activities.main

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.skysoul.accountremebercompose.activities.edit.EditActivity
import com.skysoul.accountremebercompose.activities.login.LoginActivity
import com.skysoul.accountremebercompose.activities.main.ClickView.SEARCH
import com.skysoul.accountremebercompose.base.BaseActivity
import com.skysoul.accountremebercompose.data.dbroom.entities.SimpleAccount
import com.skysoul.accountremebercompose.managers.UserManager
import com.skysoul.accountremebercompose.model.beans.Account
import com.skysoul.accountremebercompose.ui.HSpace16
import com.skysoul.utils.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<MainViewModel>() {

    lateinit var scostate: DrawerState
    lateinit var scope: CoroutineScope

    @Composable
    override fun getPage() {
        scostate = rememberDrawerState(DrawerValue.Closed)
        scope = rememberCoroutineScope()
        return Box(Modifier.fillMaxSize()) {
            var showSearch by remember { mutableStateOf(false) }
            HomePage(scostate, viewModel) { type, obj ->
                when (type) {
                    ClickView.Opt_ADD -> {
                        gotoEditor(null)
                    }

                    ClickView.Account -> {
                        val account: SimpleAccount = obj as SimpleAccount
                        viewModel.getDetailAccount(account.id) {
                            gotoEditor(it)
                        }

                    }

                    SEARCH -> {
                        showSearch = true
                    }

                    else -> {

                    }
                }
            }
            if(showSearch){
                SearchPage(viewModel) { }
            }


        }
    }

    private fun gotoSearch() {
    }

    override fun initData() {
        viewModel.initCates()
    }

    fun gotoEditor(account: Account?) {
        startActivity(Intent(this, EditActivity::class.java).apply {
            if (account != null)
                putExtra("account", account)
        })
    }

    override fun onBackPressed() {

        if (scostate.isOpen) {
            scope.launch {
                scostate.close()
            }
            return
        }

        if (viewModel.editting) {
            viewModel.editting = false
            return
        }
        super.onBackPressed()
    }

    override fun startObserver() {
        UserManager.userLiveData.observe(this) {
            if (it == null) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

}
