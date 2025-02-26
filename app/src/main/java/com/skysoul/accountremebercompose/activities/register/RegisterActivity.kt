package com.skysoul.accountremebercompose.activities.register

import android.content.Intent
import androidx.compose.runtime.Composable
import com.skysoul.accountremebercompose.activities.register2.RegisterActivity2
import com.skysoul.accountremebercompose.base.BaseActivity

class RegisterActivity : BaseActivity<RegisterViewModel>() {

    override fun startObserver() {
        viewModel.next.observe(this) {
            if (it) {
                //to register2
                startActivity(Intent(this,RegisterActivity2::class.java))
                finish()
            }
        }
    }


    override fun initData() {
    }

    @Composable
    override fun getPage() {
        return registerPage()
    }

}