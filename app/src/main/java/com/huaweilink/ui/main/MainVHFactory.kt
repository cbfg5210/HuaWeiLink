package com.huaweilink.ui.main

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import com.huaweilink.R
import com.huaweilink.constant.AppConst
import kotlinx.android.synthetic.main.item_main.view.*
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
internal class MainVHFactory : RVHolderFactory() {

    override fun createViewHolder(parent: ViewGroup?, viewType: Int, item: Any): RVHolder<out Any> {
        return AppItemVH(inflate(R.layout.item_main, parent))
    }

    private inner class AppItemVH(itemView: View) : RVHolder<JSONObject>(itemView) {
        private val tvAppName = itemView.tvAppName
        private val ivDelete = itemView.ivDelete

        @SuppressLint("SetTextI18n")
        override fun setContent(item: JSONObject, isSelected: Boolean, payload: Any?) {
            tvAppName.text = "$adapterPosition、${item.optString(AppConst.APP_NAME)}"
        }

        override fun setListeners(itemClickListener: View.OnClickListener?, itemLongClickListener: View.OnLongClickListener?) {
            super.setListeners(itemClickListener, itemLongClickListener)
            ivDelete.setOnClickListener(itemClickListener)
        }
    }
}