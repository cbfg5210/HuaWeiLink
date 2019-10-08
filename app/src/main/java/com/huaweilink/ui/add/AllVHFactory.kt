package com.huaweilink.ui.add

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.adapter.BViewHolder
import com.adapter.BViewHolderFactory
import com.huaweilink.R

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/10/8 9:59
 * 功能描述：
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/10/8 9:59
 * 修改内容：
 */
internal class AllVHFactory(private val packageManager: PackageManager) : BViewHolderFactory() {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, item: Any): BViewHolder<out Any> {
        return AppItemVH(inflater, parent)
    }

    private inner class AppItemVH(inflater: LayoutInflater, parent: ViewGroup?)
        : BViewHolder<PackageInfo>(inflater, parent, R.layout.item_all) {

        private val tvAppName: TextView = itemView.findViewById(R.id.tvAppName)

        override fun setContents(item: PackageInfo, isSelected: Boolean, payload: Any?) {
            tvAppName.text = item.applicationInfo.loadLabel(packageManager)
        }
    }
}