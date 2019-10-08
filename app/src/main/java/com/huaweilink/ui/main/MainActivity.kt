package com.huaweilink.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.adapter.BRecyclerAdapter
import com.huaweilink.R
import com.huaweilink.util.SPHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: BRecyclerAdapter<JSONObject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvLinks.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        adapter = BRecyclerAdapter<JSONObject>(this, AppItemVHFactory())
                .bindRecyclerView(rvLinks)
                .setItemClickListener { _, item, _ -> }

        vgMainRoot.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        adapter.setItems(SPHelper.getAppItems()).notifyDataSetChanged()
    }
}
