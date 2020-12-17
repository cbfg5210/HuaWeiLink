package com.huaweilink.util

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
    val appItems: ArrayList<JSONObject> by lazy {
        val items = ArrayList<JSONObject>()
        PreferenceManager.getDefaultSharedPreferences(AppHolder.get().app())
                .getString(KEY_ITEMS, null)
                ?.run {
                    val array = JSONArray(this)
                    for (i in 0 until array.length()) {
                        items.add(array.getJSONObject(i))
                    }
                }
        items
    }

    fun removeAppItem(item: JSONObject) {
        appItems.remove(item)
        saveAppItems()
    }

    fun saveAppItems() {
        if (appItems.size > 1) {
            appItems.sortWith { o1, o2 -> collator.compare(o1.optString(AppConst.APP_NAME), o2.optString(AppConst.APP_NAME)) }
        }
        PreferenceManager.getDefaultSharedPreferences(AppHolder.get().app())
                .edit()
                .putString(KEY_ITEMS, appItems.toString())
                .apply()
    }
}