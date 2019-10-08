package com.huaweilink.ui.add

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
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

        private val cbOption: CheckBox = itemView.findViewById(R.id.cbOption)

        override fun setContents(item: PackageInfo, isSelected: Boolean, payload: Any?) {
            if (payload != null) {
                cbOption.isChecked = isSelected
                return
            }

            cbOption.text = item.applicationInfo.loadLabel(packageManager)
            cbOption.isChecked = isSelected
        }
    }
}