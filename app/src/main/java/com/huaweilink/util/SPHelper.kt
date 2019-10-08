package com.huaweilink.util

import android.preference.PreferenceManager
import org.json.JSONArray
import org.json.JSONObject

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

    fun getAppItems(): List<JSONObject> {
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

    fun saveAppItems() {
        PreferenceManager.getDefaultSharedPreferences(AppHolder.get().app())
                .edit()
                .putString(KEY_ITEMS, getAppItems().toString())
                .apply()
    }
}