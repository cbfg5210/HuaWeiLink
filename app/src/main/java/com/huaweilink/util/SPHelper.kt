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
    private var appItems: ArrayList<JSONObject>? = null
    private val collator: Collator by lazy { Collator.getInstance(Locale.CHINA) }

    fun getAppItems(): ArrayList<JSONObject> {
        val items = appItems ?: ArrayList()

        if (appItems == null) {
            appItems = items

            PreferenceManager.getDefaultSharedPreferences(AppHolder.get().app())
                    .getString(KEY_ITEMS, null)
                    ?.run {
                        val array = JSONArray(this)
                        for (i in 0 until array.length()) {
                            items.add(array.getJSONObject(i))
                        }
                    }
        }

        return items
    }

    fun removeAppItem(item: JSONObject) {
        getAppItems().remove(item)
        saveAppItems()
    }

    fun saveAppItems() {
        val items = getAppItems()
        if (items.size > 1) {
            items.sortWith { o1, o2 -> collator.compare(o1.optString(AppConst.APP_NAME), o2.optString(AppConst.APP_NAME)) }
        }
        PreferenceManager.getDefaultSharedPreferences(AppHolder.get().app())
                .edit()
                .putString(KEY_ITEMS, items.toString())
                .apply()
    }
}