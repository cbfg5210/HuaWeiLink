package com.huaweilink.util

import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import com.huaweilink.constant.AppConst
import org.json.JSONArray
import org.json.JSONObject
import java.text.Collator
import java.util.*
import kotlin.collections.ArrayList

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/10/8 10:32
 * 功能描述：
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/10/8 10:32
 * 修改内容：
 */
object SPHelper {
    private const val KEY_ITEMS = "items"
    private val collator: Collator by lazy { Collator.getInstance(Locale.CHINA) }

    private val sp: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(
            AppHolder.app()
        )
    }

    private val appItems: ArrayList<JSONObject> by lazy {
        val items = ArrayList<JSONObject>()
        sp.getString(KEY_ITEMS, null)?.run {
            val array = JSONArray(this)
            for (i in 0 until array.length()) {
                items.add(array.getJSONObject(i))
            }
        }
        items
    }


    fun getAppItems(): List<JSONObject> = appItems

    fun removeAppItem(item: JSONObject) {
        appItems.remove(item)
        saveAppItems()
    }

    fun saveSelections(packageManager: PackageManager, selections: Set<PackageInfo>) {
        appItems.clear()

        selections.sortedWith { o1, o2 ->
            collator.compare(
                o1.applicationInfo.loadLabel(packageManager),
                o2.applicationInfo.loadLabel(packageManager)
            )
        }.forEach { item ->
            JSONObject().also { appItems.add(it) }
                .put(AppConst.APP_NAME, item.applicationInfo.loadLabel(packageManager))
                .put(AppConst.APP_PKG, item.packageName)
        }

        saveAppItems()
    }

    private fun saveAppItems() {
        sp.edit()
            .putString(KEY_ITEMS, appItems.toString())
            .apply()
    }
}