package com.huaweilink.ui.add

import android.app.Activity
import android.content.pm.PackageInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import cbfg.rvadapter.RVAdapter
import cbfg.rvadapter.SelectStrategy
import com.huaweilink.R
import com.huaweilink.constant.AppConst
import com.huaweilink.util.PkgsHolder
import com.huaweilink.util.SPHelper
import kotlinx.android.synthetic.main.activity_all_app.*
import org.json.JSONObject

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
    private lateinit var adapter: RVAdapter<PackageInfo>
    private var hasChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_app)

        title = "添加到快捷入口列表"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupList()
    }

    private fun setupList() {
        adapter = RVAdapter<PackageInfo>(this, AllVHFactory(packageManager))
                .bind(rvApps)
                .setSelectable(PackageInfo::class.java, SelectStrategy.MULTI_SELECTABLE)
                .setItems(PkgsHolder.getAllApps())
                .setItemClickListener { _, item, index ->
                    if (adapter.getSelections().contains(item)) {
                        adapter.deselectAt(index)
                    } else {
                        adapter.selectAt(index)
                    }
                    hasChanged = true
                }

        if (SPHelper.appItems.isEmpty()) {
            return
        }

        val pkgs = ArrayList<String>(SPHelper.appItems.size)
        SPHelper.appItems.forEach { item -> pkgs.add(item.optString(AppConst.APP_PKG)) }

        PkgsHolder.getAllApps().forEachIndexed { _, item ->
            if (pkgs.contains(item.packageName)) {
                adapter.select(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_all_app, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        val items = when (item.itemId) {
            R.id.actionAllApps -> PkgsHolder.getAllApps()
            R.id.actionSimpleApps -> PkgsHolder.getInstalledApps()
            R.id.actionSystemApps -> PkgsHolder.getSystemApps()
            else -> return true
        }
        item.isChecked = true
        adapter.setItems(items, false)

        return true
    }

    override fun onBackPressed() {
        if (hasChanged) {
            saveSelections()
            setResult(Activity.RESULT_OK)
        }
        finish()
    }

    private fun saveSelections() {
        SPHelper.appItems.clear()

        adapter.getSelections().forEach { selection ->
            JSONObject().put(AppConst.APP_NAME, selection.applicationInfo.loadLabel(packageManager))
                    .put(AppConst.APP_PKG, selection.packageName)
                    .run { SPHelper.appItems.add(this) }
        }

        SPHelper.saveAppItems()
    }
}