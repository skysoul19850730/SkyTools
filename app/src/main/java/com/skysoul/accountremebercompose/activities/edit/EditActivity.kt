package com.skysoul.accountremebercompose.activities.edit

import androidx.compose.runtime.Composable
import com.skysoul.accountremebercompose.base.BaseActivity
import com.skysoul.accountremebercompose.model.beans.Account

/**
 *@author shenqichao
 *Created on 2022/9/9
 *@Description
 */
class EditActivity : BaseActivity<EditViewModel>() {
    @Composable
    override fun getPage() {
        editPage()
    }

    override fun startObserver() {
    }

    override fun initData() {
        var account = intent.getParcelableExtra<Account>("account")
        viewModel.initWithAccount(account)
    }

}