package com.huaweilink

import android.app.Application
import com.huaweilink.util.AppHolder

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/10/8 10:34
 * 功能描述：
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/10/8 10:34
 * 修改内容：
 */
class RelApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppHolder.get().init(this)
    }
}