package com.huaweilink.ui.add

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.view.View
import android.view.ViewGroup
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import com.huaweilink.R
import kotlinx.android.synthetic.main.item_all.view.*

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/10/8 9:59
 * 功能描述：
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/10/8 9:59
 * 修改内容：
 */
internal class AllVHFactory(private val packageManager: PackageManager) : RVHolderFactory() {

    override fun createViewHolder(parent: ViewGroup?, viewType: Int, item: Any): RVHolder<out Any> {
        return AppItemVH(inflate(R.layout.item_all, parent))
    }

    private inner class AppItemVH(itemView: View) : RVHolder<PackageInfo>(itemView) {
        private val cbOption = itemView.cbOption

        override fun setContent(item: PackageInfo, isSelected: Boolean, payload: Any?) {
            cbOption.isChecked = isSelected
            if (payload == null) {
                cbOption.text = item.applicationInfo.loadLabel(packageManager)
            }
        }

        override fun setListeners(itemClickListener: View.OnClickListener?, itemLongClickListener: View.OnLongClickListener?) {
            super.setListeners(itemClickListener, itemLongClickListener)
            cbOption.setOnClickListener(itemClickListener)
        }
    }
}