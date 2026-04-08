package com.skysoul.accountremebercompose.model.beans

import com.skysoul.accountremebercompose.data.dbroom.entities.DMMember
import com.skysoul.accountremebercompose.data.dbroom.entities.DMUser
import com.skysoul.accountremebercompose.data.dbroom.entities.UserWithMembers


class User {

    var userId = 0
    var userName: String = ""
    var nickName: String = ""
    var passwordTip: String = ""
    var passwordViewTip: String = ""
    var isSamePassword: Boolean = true
    var isViewPasswordSet = true

    var currentMember: DMMember?=null
    var members : MutableList<DMMember> = mutableListOf()

    var lastLoginTime : Long = 0

    constructor()

    constructor(dmUser: DMUser) {
        userId = dmUser.id
        userName = dmUser.userName
        nickName = dmUser.nickName
        passwordTip = dmUser.passwordTip
        passwordViewTip = dmUser.passwordViewTip
        isSamePassword = dmUser.isSamePassword
        lastLoginTime = dmUser.lastLoginTime
        isViewPasswordSet = isSamePassword || dmUser.passwordView?.hasValue() ?: false
    }

    constructor(userWithMembers: UserWithMembers):this(userWithMembers.user){
        currentMember = userWithMembers.currentMember
        members = userWithMembers.members as MutableList<DMMember>
    }

}