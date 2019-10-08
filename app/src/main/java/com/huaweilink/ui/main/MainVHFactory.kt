package com.huaweilink.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.adapter.BViewHolder
import com.adapter.BViewHolderFactory
import com.huaweilink.R
import com.huaweilink.constant.AppConst
import org.json.JSONObject

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/10/8 9:59
 * 功能描述：
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/10/8 9:59
 * 修改内容：
 */
internal class MainVHFactory : BViewHolderFactory() {
    override fun getItemViewType(item: Any): Int {
        item as JSONObject
        return if (item.has(AppConst.APP_NAME)) 2 else 1
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, item: Any): BViewHolder<out Any> {
        return if (getItemViewType(item) == 1) HeaderVH(inflater, parent) else AppItemVH(inflater, parent)
    }

    private inner class HeaderVH(inflater: LayoutInflater, parent: ViewGroup?)
        : BViewHolder<JSONObject>(inflater, parent, R.layout.item_main_header) {

        private val tvAddLink: View = itemView.findViewById(R.id.tvAddLink)

        override fun setContents(item: JSONObject, isSelected: Boolean, payload: Any?) {
        }

        override fun setListeners(clickListener: View.OnClickListener, longClickListener: View.OnLongClickListener) {
            tvAddLink.setOnClickListener(clickListener)
        }
    }

    private inner class AppItemVH(inflater: LayoutInflater, parent: ViewGroup?)
        : BViewHolder<JSONObject>(inflater, parent, R.layout.item_main) {

        private val tvAppName: TextView = itemView.findViewById(R.id.tvAppName)

        override fun setContents(item: JSONObject, isSelected: Boolean, payload: Any?) {
            tvAppName.text = item.optString(AppConst.APP_NAME)
        }
    }
}