package com.skysoul.accountremebercompose.activities.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.skysoul.accountremebercompose.base.BaseViewModel
import com.skysoul.accountremebercompose.data.dbroom.converts.ExtraColumn
import com.skysoul.accountremebercompose.launch
import com.skysoul.accountremebercompose.managers.UserManager
import com.skysoul.accountremebercompose.model.beans.Account
import com.skysoul.accountremebercompose.model.beans.Cate
import com.skysoul.accountremebercompose.model.repository.local.AccountResLocal
import com.skysoul.accountremebercompose.model.repository.local.CateResLocal

/**
 *@author shenqichao
 *Created on 2022/9/9
 *@Description
 */
class EditViewModel : BaseViewModel() {

    val accountRepository: AccountResLocal  = AccountResLocal()
    val cateRepository: CateResLocal = CateResLocal()

    var account: Account? = null

    val editBeans = arrayListOf<EditInputBean>()

    val platEditBean = EditInputBean("平台", true, "输入平台名称").apply { editBeans.add(this) }
    val accountEditBean = EditInputBean("账号", true, "输入平台账号").apply { editBeans.add(this) }
    val passEditBean = EditInputBean("密码", true, "输入平台密码").apply { editBeans.add(this) }
    val passTipEditBean = EditInputBean("密码提示", true, "输入平台密码提示").apply { editBeans.add(this) }
    val mailEditBean = EditInputBean("绑定邮箱", false, "输入平台账号绑定的邮箱").apply { editBeans.add(this) }
    val phoneEditBean = EditInputBean("绑定手机", false, "输入平台账号绑定的手机号码").apply { editBeans.add(this) }


    var cateList : List<Cate> = arrayListOf()
    var cateSelected: Cate? by mutableStateOf(null)
    var externalColumn: SnapshotStateList<ExtraColumn> = mutableStateListOf<ExtraColumn>()


    fun addEmptyColumn(){
        externalColumn.add(ExtraColumn("",""))
    }
    fun removeColumn(index:Int){
        externalColumn.removeAt(index = index)
    }

    fun initWithAccount(accout: Account?) {
        accout?.also {
            this.account = it
            platEditBean.input = it.platform
            accountEditBean.input = it.accountName
            passEditBean.input = it.password
            passTipEditBean.input = it.tip
            mailEditBean.input = it.bindmail
            phoneEditBean.input = it.bindphone
            cateSelected = it.cate
            externalColumn.addAll(it.extraColumnList)
        }

        launch {
            showLoading()
            cateRepository.getCateAll(UserManager.getUserId()).ifSuccess {
                hideLoading()
                it.collect{cates->
                    cateList = cates
                    if(cateSelected == null){
                        cateSelected = cates.firstOrNull()
                    }
                }

            }
        }


    }

    fun save() {
        var check = true
        editBeans.forEach {
            if (it.need && it.input.isEmpty()) {
                check = false
            }
        }

        if (!check) {
            showToast("请检查必填项")
            return
        }

        var accoutNew = account ?: Account()
        accoutNew.platform = platEditBean.input
        accoutNew.accountName = accountEditBean.input
        accoutNew.password = passEditBean.input
        accoutNew.tip = passTipEditBean.input
        accoutNew.bindmail = mailEditBean.input
        accoutNew.bindphone = phoneEditBean.input

        accoutNew.create_time = System.currentTimeMillis().toString()
        accoutNew.userId = UserManager.getUser()?.userId ?: 0
        accoutNew.cate = cateSelected
        accoutNew.extraColumnList = externalColumn

        launch {
            accountRepository.addAccount(accoutNew)
            finish.postValue(true)
        }


    }

//    fun addCateAndChoose(cateName: String) {
//
//        launch {
//            var cate = Cate(0, cateName = cateName, UserManager.getUserId())
//            cateRepository.addCate(cate).ifSuccess {
//                cate.id = it.toInt()
//                cateSelected = cate
//            }
//        }
//    }

    suspend fun addCateAndChoose(cateName: String):Boolean{
        var cate = Cate(0, cateName = cateName, UserManager.getUserId())
        cateRepository.addCate(cate).ifSuccess {
            cate.id = it.toInt()
            cateSelected = cate
            return true
        }
        return false
    }
}

