package com.skysoul.accountremebercompose.activities.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.skysoul.accountremebercompose.base.BaseViewModel
import com.skysoul.accountremebercompose.data.dbroom.entities.SimpleAccount
import com.skysoul.accountremebercompose.launch
import com.skysoul.accountremebercompose.managers.UserManager
import com.skysoul.accountremebercompose.model.beans.Account
import com.skysoul.accountremebercompose.model.beans.Cate
import com.skysoul.accountremebercompose.model.repository.local.AccountResLocal
import com.skysoul.accountremebercompose.model.repository.local.CateResLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 *@author shenqichao
 *Created on 2022/9/8
 *@Description
 */
class MainViewModel : BaseViewModel() {

    var editting: Boolean by mutableStateOf(false)

    var user = UserManager.userLiveData

    val accountResLocal: AccountResLocal = AccountResLocal()

    var mapCateAccountFlows = mapOf<Int,Flow<List<SimpleAccount>>>()

//    var _mAccounts: Flow<List<SimpleAccount>> =
//        accountResLocal.getAccountAll(UserManager.getUserId(),mCateSelected.value)

    var showTipAccount :MutableStateFlow<SimpleAccount?> =  MutableStateFlow(null)

    val showTip =  showTipAccount.map { it!=null }
    val cateRepository: CateResLocal = CateResLocal()
    var cateList = mutableStateListOf<Cate>()
    var cateSelected: Cate? by mutableStateOf(null)

    fun accountsFlow(cated:Cate):Flow<List<SimpleAccount>>{
        return accountResLocal.getAccountAll(UserManager.getUserId(),cated?.id?:0)
    }

    fun initCates(){
        launch {
            cateRepository.getCateAll(UserManager.getUserId()).ifSuccess {
                hideLoading()
                it.collect{cates->
                    cateList.clear()
                    cateList.add(Cate(0,"全部",UserManager.getUserId()))
                    cateList.addAll(cates)
                    if(cateSelected == null){
                        cateSelected = cateList.firstOrNull()
                    }
                }

            }
        }
    }

    fun deleteSelect(selected: List<Int>) {
        launch {
            accountResLocal.removeAccounts(selected)?.ifSuccess {
                editting = false
            }
        }
    }

    fun getDetailAccount(id: Int, callback: (Account) -> Unit) {
        launch {
            val aWC = accountResLocal.getAccount(id)
            callback.invoke(Account.fromDmAccount(aWC))
        }

    }


    fun logout() {
        UserManager.logout()
        finish.postValue(true)
    }
}