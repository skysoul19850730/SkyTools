package com.skysoul.accountremebercompose.activities.register2

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.skysoul.accountremebercompose.activities.main.MainActivity
import com.skysoul.accountremebercompose.base.BaseActivity

class RegisterActivity2 : BaseActivity<RegisterViewModel2>() {

    lateinit var dialogState:MutableState<Boolean>

    override fun startObserver() {
        viewModel.next.observe(this) {
            if (it) {
                //to register2
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        }
    }

    override fun initData() {
    }

    @Composable
    override fun getPage() {
        return registerPage2()
    }

}