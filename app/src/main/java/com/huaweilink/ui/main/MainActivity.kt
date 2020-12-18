package com.huaweilink.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import cbfg.rvadapter.RVAdapter
import com.huaweilink.R
import com.huaweilink.constant.AppConst
import com.huaweilink.ui.add.AllAppActivity
import com.huaweilink.util.PkgsHolder
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
        ivEditLinks.setOnClickListener { startActivityForResult(Intent(this, AllAppActivity::class.java), REQ_CODE) }

        setupList()
    }

    private fun setupList() {
        rvLinks.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        adapter = RVAdapter<JSONObject>(this, MainVHFactory())
                .bind(rvLinks)
                .setItems(SPHelper.appItems)
                .setItemLongClickListener { _, item, _ ->
                    val pkgName = item.optString(AppConst.APP_PKG)
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .setData(Uri.fromParts("package", pkgName, null))
                    startActivity(intent)
                }
                .setItemClickListener { view, item, position ->
                    if (view.id == R.id.ivDelete) {
                        SPHelper.removeAppItem(item)
                        adapter.removeAt(position)
                        return@setItemClickListener
                    }
                    packageManager.getLaunchIntentForPackage(item.optString(AppConst.APP_PKG))
                            ?.run {
                                this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(this)
                                finish()
                            }
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK) {
            adapter.setItems(SPHelper.appItems)
        }
    }
}
