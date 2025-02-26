package com.skysoul.accountremebercompose.model.beans

import android.os.Parcelable
import com.skysoul.accountremebercompose.data.dbroom.converts.ExtraColumn
import com.skysoul.accountremebercompose.data.dbroom.entities.AccountWithCate
import com.skysoul.accountremebercompose.data.dbroom.entities.DMAccount
import kotlinx.android.parcel.Parcelize

/**
 * Created by Administrator on 2018/4/2.
 */
@Parcelize
data class Account(
    var id: Int = 0,
    var platform: String = "",
    var password: String = "",
    var tip: String = "",
    var bindphone: String = "",
    var bindmail: String = "",
    var create_time: String = "",
    var accountName: String = "",
    var userId: Int = 0,
    var cate: Cate? = null,
    var extraColumnList: List<ExtraColumn> = arrayListOf()
) : Parcelable, Cloneable {

    public override fun clone(): Account {
        var account = Account()
        account.id = id
        account.platform = platform
        account.password = password
        account.tip = tip
        account.bindphone = bindphone
        account.bindmail = bindmail
        account.create_time = create_time
        account.accountName = accountName
        account.userId = userId

        account.cate = cate
        account.extraColumnList = extraColumnList
        return account
    }

    fun toDmAccount(): DMAccount {
        return DMAccount(
            id,
            platform,
            accountName,
            password,
            tip,
            bindphone,
            bindmail,
            create_time,
            cate?.id ?: 0,
            userId,
            extraColumnList
        )
    }

    companion object {
        fun fromDmAccount(dmAccount: AccountWithCate): Account {
            return dmAccount.account.run {
                Account(id,
                    platform,
                    password,
                    tip,
                    bindPhone,
                    bindMail,
                    createTime,
                    accountName,
                    userId,
                    Cate.fromDmCate(dmAccount.cate),
                    arrayListOf<ExtraColumn>().apply { addAll(externalColumns) })
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is Account) {
            return id == other.id
        }
        return super.equals(other)
    }

    var isChecked = false

}