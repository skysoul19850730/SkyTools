package com.skysoul.accountremebercompose.activities.dialogs

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pwrd.dls.marble.common.permission.doWithSystemPermission
import com.pwrd.dls.marble.common.permission.readImagePermission
import com.skysoul.accountremebercompose.R
import com.skysoul.accountremebercompose.base.BaseActivity
import com.skysoul.accountremebercompose.data.dbroom.entities.DMMember
import com.skysoul.accountremebercompose.managers.UserManager
import com.skysoul.accountremebercompose.model.api.NilException
import com.skysoul.accountremebercompose.model.repository.local.UserRepositoryLocal
import com.skysoul.accountremebercompose.ui.HSpace
import com.skysoul.accountremebercompose.ui.VSpace
import com.skysoul.accountremebercompose.ui.VSpace16
import com.skysoul.accountremebercompose.ui.button
import com.skysoul.accountremebercompose.ui.edittext.EditText
import com.skysoul.accountremebercompose.ui.edittext.TextFieldState
import com.skysoul.accountremebercompose.utils.toBlob
import com.skysoul.accountremebercompose.utils.toast
import com.skysoul.album.AlbumManager
import com.skysoul.album.OnSelectedCallback
import com.skysoul.album.bean.MediaInfo
import com.skysoul.album.bean.MediaType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 *@author shenqichao
 *Created on 2026/4/3
 *@Description
 */
@Composable
fun UserSelectDialog(visible: MutableState<Boolean>,showEdit: Boolean = true,onMemberSelect:(DMMember)->Unit) {

    val activity = LocalActivity.current as BaseActivity<*>

    var editing by remember(System.currentTimeMillis()) { mutableStateOf(false) }

    CustomDialog(visible) {

        Column(
            modifier = Modifier
                .width(300.dp)
                .height(400.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(if (!editing) "切换角色" else "创建角色")

            VSpace16()

            if(!editing) {

                LazyColumn {
                    items(UserManager.userState.value?.members ?: arrayListOf()) { item ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 16.dp)
                                .clickable {

                                    onMemberSelect.invoke(item)
                                    visible.value = false
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Image(
                                BitmapPainter(
                                    BitmapFactory.decodeByteArray(
                                        item.avatar!!,
                                        0,
                                        item.avatar!!.size
                                    ).asImageBitmap()
                                ), contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                            HSpace(12)
                            Text(item.nickName)
                        }

                    }
                    if(showEdit) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .padding(horizontal = 16.dp)
                                    .clickable {
                                        editing = true
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(imageVector = Icons.Filled.Edit, contentDescription = "")
                            }
                        }
                    }
                }
            }else{
                var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
                var nickName by remember { mutableStateOf(TextFieldState()) }

                val painter = if (imageBitmap != null) BitmapPainter(imageBitmap!!.asImageBitmap())
                else painterResource(R.mipmap.add_icon_red)
                Image(
                    painter = painter, contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            activity.doWithSystemPermission(readImagePermission()) {
                                AlbumManager.doSelected()
                                    .setMediaType(MediaType.IMAGE)
                                    .setOpenCropDirectly(true)
                                    .setCropRatio(1f)
                                    .setImageLimitSize(100 * 1024)
                                    .setMaxSelectImageCount(1)
                                    .setOnSelectedCallback(object : OnSelectedCallback {
                                        override fun onSuccess(p0: List<MediaInfo>) {
                                            val imagePath = p0.getOrNull(0)?.compressPath ?: ""
                                            if (imagePath.isNotEmpty()) {
                                                imageBitmap = BitmapFactory.decodeFile(imagePath)
                                            }
                                        }

                                        override fun onFail(p0: Int, p1: String?) {
                                        }
                                    }).start(activity)
                            }
                        })

                VSpace16()
                EditText(state = nickName, label = "昵称")
                VSpace16()
                button("保存", modifier = Modifier.fillMaxWidth()) {

                    nickName.checkNull("昵称不能为空") ?: return@button
                    if(imageBitmap==null)return@button
                    val member = DMMember(0, UserManager.userId,nickName.text,imageBitmap!!.toBlob())
                    val userRepository: UserRepositoryLocal = UserRepositoryLocal()
                    GlobalScope.launch {
                        userRepository.isNickNameExist(nickName.text).ifSuccess {
                            if (it) {
                                nickName.errorText = "昵称已存在，请更换"
                                return@launch
                            }
                        }.ifError {
                            if(it !is NilException){
                                return@launch
                            }
                        }
                        userRepository.addMember(member).ifSuccess {
                            UserManager.addMember(member)
                            editing = false
                        }.ifError {
                            toast("添加失败")
                        }
                    }
                }
            }

        }


    }

}