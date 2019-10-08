package com.huaweilink.ui

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.adapter.BRecyclerAdapter
import com.huaweilink.R
import com.huaweilink.model.AppItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvLinks.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        BRecyclerAdapter<AppItem>(this, AppItemVHFactory())
                .bindRecyclerView(rvLinks)
                .setItems(getItems())
                .setItemClickListener { _, item, _ -> }

        vgMainRoot.setOnClickListener { finish() }
    }

    private fun getItems(): List<AppItem> {
        return arrayListOf(
                AppItem("Test", "Test"),
                AppItem("Test", "Test"),
                AppItem("Test", "Test")
        )
    }
}
