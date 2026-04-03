package com.skysoul.accountremebercompose.data.dbroom.daos

import androidx.room.Dao
import androidx.room.Query
import com.skysoul.accountremebercompose.data.dbroom.BaseDao
import androidx.room.Transaction
import com.skysoul.accountremebercompose.data.dbroom.entities.DMUser
import com.skysoul.accountremebercompose.data.dbroom.entities.DMWrongUser
import com.skysoul.accountremebercompose.data.dbroom.entities.NoLoginUser
import com.skysoul.accountremebercompose.data.dbroom.entities.UserWithMembers

@Dao
interface UserDao : BaseDao<DMUser> {



    @Query("SELECT * from user where id=:userId")
    suspend fun getUserById(userId: Int): DMUser?

    /**
     * 查询 User 及其关联的所有 Members
     * @Transaction 注解确保查询在同一个事务中完成
     */
    @Transaction
    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserWithMembers(userId: Int): UserWithMembers?

    @Query("SELECT * from user where userName=:userName")
    suspend fun getUserByName(userName: String): DMUser?

    /**
     * 通过用户名查询 User 及其关联的所有 Members
     */
    @Transaction
    @Query("SELECT * FROM user WHERE userName = :userName")
    suspend fun getUserWithMembersByName(userName: String): UserWithMembers?

    @Query("select * from user")
    suspend fun getUserList(): List<DMUser>?

    /**
     * 查询所有 User 及其关联的 Members
     */
    @Transaction
    @Query("SELECT * FROM user")
    suspend fun getAllUsersWithMembers(): List<UserWithMembers>

//    /**  获取程序中 使用的用户，需要修改信息时，业务层获取dm对象，并赋值给dmuser进行更新 */
//    @Query("select * from user where id=:userId")
//    fun getSimpleUserById(userId: Int):SimpleUser

    /**  获取未登陆用户列表，供用户选择用户或者切换用户使用 */
    @Query("select userName,nickName from user ")
    suspend fun getUserNameList(): List<NoLoginUser>?

    /**  获取当前尝试密码的用户的剩余次数，及上次错误的时间 */
    @Query("select userName,leftTryTimes,lastWrongTime from user where userName=:userName")
    suspend fun getDmWrongUser(userName: String): DMWrongUser

    /**  验证用户名是否存在 */
    @Query("select userName from user where userName=:userName")
    suspend fun getUserName(userName: String): String?

    /** 验证昵称是否存在  */
    @Query("select nickName from user where nickName = :nickName")
    suspend fun getNickName(nickName: String): String?

    /* 获取提示*/
    @Query("""
        select passwordTip from user where userName = :username
    """)
    fun getTipByUsername(username:String):String


}