package com.skysoul.accountremebercompose.model.beans

import com.skysoul.accountremebercompose.data.dbroom.entities.DMUser


class User {

    var userId = 0
    var userName: String = ""
    var nickName: String = ""
    var passwordTip: String = ""
    var passwordViewTip: String = ""
    var isSamePassword: Boolean = true
    var isViewPasswordSet = true

    constructor()

    constructor(dmUser: DMUser) {
        userId = dmUser.id
        userName = dmUser.userName
        nickName = dmUser.nickName
        passwordTip = dmUser.passwordTip
        passwordViewTip = dmUser.passwordViewTip
        isSamePassword = dmUser.isSamePassword
        isViewPasswordSet = isSamePassword || dmUser.passwordView?.hasValue() ?: false
    }

}