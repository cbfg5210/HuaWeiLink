package com.huaweilink.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.adapter.BRecyclerAdapter
import com.huaweilink.R
import com.huaweilink.constant.AppConst
import com.huaweilink.ui.add.AllAppActivity
import com.huaweilink.util.SPHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: BRecyclerAdapter<JSONObject>
    private val header = JSONObject()

    companion object {
        private const val REQ_CODE = 11
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vgMainRoot.setOnClickListener { finish() }

        setupList()
    }

    private fun setupList() {
        rvLinks.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        adapter = BRecyclerAdapter<JSONObject>(this, MainVHFactory())
                .bindRecyclerView(rvLinks)
                .setItemClickListener { _, item, position ->
                    if (position == 0) {
                        val intent = Intent(this, AllAppActivity::class.java)
                        startActivityForResult(intent, REQ_CODE)
                        return@setItemClickListener
                    }

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
        adapter.setItems(SPHelper.getAppItems())
        adapter.items.add(0, header)
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK) {
            refreshList()
        }
    }
}
