package com.huaweilink.ui

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.adapter.BRecyclerAdapter
import com.huaweilink.R
import com.huaweilink.util.SPHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvLinks.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        BRecyclerAdapter<JSONObject>(this, AppItemVHFactory())
                .bindRecyclerView(rvLinks)
                .setItems(SPHelper.getAppItems())
                .setItemClickListener { _, item, _ -> }

        vgMainRoot.setOnClickListener { finish() }
    }
}
