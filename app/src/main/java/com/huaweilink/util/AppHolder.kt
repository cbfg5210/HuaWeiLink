package com.huaweilink.util

import android.app.Application

/**
 * 添加人：  Tom Hawk
 * 添加时间：2018/9/12 17:06
 * 功能描述：保存Application对象,提供给有需要的地方使用
 *
 * 修改人：  Tom Hawk
 * 修改时间：2018/9/12 17:06
 * 修改内容：
 */
object AppHolder {
    private lateinit var application: Application

    /**
     * 将Application传入
     *
     * @param application Application
     */
    fun init(application: Application) {
        this.application = application
    }

    fun app(): Application {
        return application
    }
}