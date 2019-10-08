package com.huaweilink.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.adapter.BViewHolder
import com.adapter.BViewHolderFactory
import com.huaweilink.R
import com.huaweilink.model.AppItem

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/10/8 9:59
 * 功能描述：
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/10/8 9:59
 * 修改内容：
 */
internal class AppItemVHFactory : BViewHolderFactory() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, item: Any): BViewHolder<out Any> {
        return AppItemVH(inflater, parent)
    }

    private inner class AppItemVH(inflater: LayoutInflater, parent: ViewGroup?)
        : BViewHolder<AppItem>(inflater, parent, R.layout.item_app) {

        private val tvAppName: TextView = itemView.findViewById(R.id.tvAppName)

        override fun setContents(item: AppItem, isSelected: Boolean, payload: Any?) {
            tvAppName.text = item.name
        }
    }
}