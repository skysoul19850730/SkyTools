package com.skysoul.accountremebercompose.activities.login

import android.content.Intent
import androidx.compose.runtime.Composable
import com.skysoul.accountremebercompose.activities.main.MainActivity
import com.skysoul.accountremebercompose.activities.register.RegisterActivity
import com.skysoul.accountremebercompose.activities.register2.RegisterActivity2
import com.skysoul.accountremebercompose.base.BaseActivity
import com.skysoul.accountremebercompose.managers.UserManager

class LoginActivity : BaseActivity<LoginViewModel>() {

    @Composable
    override fun getPage() {
        loginPage()
    }

    override fun initData() {
        viewModel.getLastUser()
    }

    override fun startObserver() {
        UserManager.userLiveData.observe(this) {
            if (it != null) {
                finish()
            }
        }
        viewModel.registerType.observe(this) {
            if (it >= 0)
                startActivity(
                    Intent(
                        this, when (it) {
                            0 -> MainActivity::class.java
                            1 -> RegisterActivity::class.java
                            else -> RegisterActivity2::class.java
                        }
                    )
                )
        }
    }
}