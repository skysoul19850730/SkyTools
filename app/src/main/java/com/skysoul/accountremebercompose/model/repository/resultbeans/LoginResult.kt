package com.skysoul.accountremebercompose.model.repository.resultbeans

import com.skysoul.accountremebercompose.model.beans.User

class LoginResult(
    var user: User?=null,
    //0 成功，-1 无用户  -2 超次数  -3 失败，未超次数
    var errorType:Int = 0,
    var leftTimes:Int = 5,
    var nextTryTime:String =""
)