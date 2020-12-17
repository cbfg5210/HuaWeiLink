package com.huaweilink.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import cbfg.rvadapter.RVAdapter
import com.huaweilink.R
import com.huaweilink.constant.AppConst
import com.huaweilink.ui.add.AllAppActivity
import com.huaweilink.util.SPHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var adapter: RVAdapter<JSONObject>

    companion object {
        private const val REQ_CODE = 11
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vgMainRoot.setOnClickListener { finish() }
        tvAddLink.setOnClickListener { startActivityForResult(Intent(this, AllAppActivity::class.java), REQ_CODE) }

        setupList()
    }

    private fun setupList() {
        rvLinks.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        adapter = RVAdapter<JSONObject>(this, MainVHFactory())
                .bind(rvLinks)
                .setItemLongClickListener { _, item, position ->
                    SPHelper.removeAppItem(item)
                    adapter.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
                .setItemClickListener { _, item, _ ->
                    packageManager.getLaunchIntentForPackage(item.optString(AppConst.APP_PKG))
                            ?.run {
                                this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(this)
                                finish()
                            }
                }

        refreshList()
    }

    private fun refreshList() {
        adapter.setItems(SPHelper.appItems)
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK) {
            refreshList()
        }
    }
}
