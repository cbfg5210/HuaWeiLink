package com.huaweilink.ui.add

import android.content.pm.PackageInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adapter.BRecyclerAdapter
import com.huaweilink.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/10/8 11:08
 * 功能描述：
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/10/8 11:08
 * 修改内容：
 */
class AllAppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_app)

        BRecyclerAdapter<PackageInfo>(this, AllVHFactory(packageManager))
                .bindRecyclerView(rvLinks)
                .setItems(packageManager.getInstalledPackages(0))
                .setItemClickListener { _, item, _ -> }
    }
}