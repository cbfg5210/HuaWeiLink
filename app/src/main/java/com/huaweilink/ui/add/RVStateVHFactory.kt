package com.huaweilink.ui.add

import android.view.ViewGroup
import cbfg.rvadapter.RVHolder
import cbfg.rvadapter.RVHolderFactory
import com.huaweilink.R

/**
 * author:  Tom Hawk
 * 添加时间: 2020/12/18 14:59
 * 功能描述:
 */
class RVStateVHFactory : RVHolderFactory() {
    override fun createViewHolder(parent: ViewGroup?, viewType: Int, item: Any): RVHolder<out Any> {
        return object : RVHolder<Any>(inflate(R.layout.layout_rv_loading, parent)) {
            override fun setContent(item: Any, isSelected: Boolean, payload: Any?) {}
        }
    }
}