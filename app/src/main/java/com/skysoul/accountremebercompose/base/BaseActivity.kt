package com.skysoul.accountremebercompose.base

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.skysoul.accountremebercompose.ui.theme.AppTheme
import com.skysoul.accountremebercompose.utils.ifValue
import java.lang.reflect.ParameterizedType

/**
 *@author shenqichao
 *Created on 2022/9/1
 *@Description
 */
open abstract class BaseActivity<VM : BaseViewModel> : FragmentActivity() {
    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        initViewModel()
        doStartObserver()
        setContent {
            val sys = rememberSystemUiController()
            sys.setSystemBarsColor(Color.Transparent, darkIcons = !isSystemInDarkTheme())
            AppTheme {
                Box {
                    getPage()
                    if (viewModel.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }

    private fun doStartObserver() {
        viewModel.finish.observe(this) {
            if (it)
                doFinish()
        }
        viewModel.toast.observe(this) {
            it.ifValue { s ->
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
            }
        }
        startObserver()
        initData()
    }

    open fun doFinish() {
        super.finish()
    }

    @Composable
    abstract fun getPage()
    abstract fun initData()

    abstract fun startObserver()

    open fun initViewModel() {
        viewModel = ViewModelProvider(this).get(getVmClazz(this) as Class<VM>)
    }


    private fun <VM> getVmClazz(obj: Any): VM {
        return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
    }
}