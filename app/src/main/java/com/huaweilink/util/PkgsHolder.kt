package com.huaweilink.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.text.Collator
import java.util.*
import kotlin.collections.ArrayList

/**
 * 添加人：  Tom Hawk
 * 添加时间：2020/1/3 14:45
 * 功能描述：
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2020/1/3 14:45
 * 修改内容：
 */
object PkgsHolder {
    private val allApps = ArrayList<PackageInfo>()
    private val installedApps = ArrayList<PackageInfo>()
    private val systemApps = ArrayList<PackageInfo>()
    private lateinit var packageManager: PackageManager

    private val livePkgUpdateEvent = PkgUpdateObservable()

    fun observe(lifecycle: Lifecycle, observer: Observer) {
        livePkgUpdateEvent.deleteObservers()
        livePkgUpdateEvent.addObserver(observer)
        lifecycle.addObserver(object : LifecycleObserver {
            //这里的 onCreated() 不是立即执行的，当外面数据变化了，这里还没执行到的话会导致数据没有更新:
            /*@OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreated() {
                livePkgUpdateEvent.addObserver(observer)
            }*/
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroyed() {
                livePkgUpdateEvent.deleteObserver(observer)
            }
        })
    }

    fun setup(context: Context) {
        packageManager = context.packageManager
        if (allApps.size == 0) {
            updateList()
        } else {
            livePkgUpdateEvent.notifyChanged()
        }
    }

    fun updateList() {
        AppExecutors.diskIO().execute {
            allApps.clear()
            installedApps.clear()
            systemApps.clear()

            val collator = Collator.getInstance(Locale.CHINA)
            val comparator = Comparator<PackageInfo> { o1, o2 ->
                collator.compare(
                    o1.applicationInfo.loadLabel(packageManager),
                    o2.applicationInfo.loadLabel(packageManager)
                )
            }
            allApps.addAll(packageManager.getInstalledPackages(0))
            allApps.sortWith(comparator)

            AppExecutors.mainThread().execute {
                livePkgUpdateEvent.notifyChanged()
            }
        }
    }

    fun getAllApps(): List<PackageInfo> = allApps

    fun getInstalledApps(): List<PackageInfo> {
        if (installedApps.isEmpty()) {
            sortApps()
        }
        return installedApps
    }

    fun getSystemApps(): List<PackageInfo> {
        if (systemApps.isEmpty()) {
            sortApps()
        }
        return systemApps
    }

    private fun sortApps() {
        allApps.forEach {
            if (it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM > 0) {
                systemApps.add(it)
            } else {
                installedApps.add(it)
            }
        }
    }

    private class PkgUpdateObservable : Observable() {
        fun notifyChanged() {
            setChanged()
            notifyObservers()
        }
    }
}