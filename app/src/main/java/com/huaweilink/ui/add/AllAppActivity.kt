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

/**
 * 添加人：  Tom Hawk
 * 添加时间：2019/10/8 11:08
 * 功能描述：
 * <p>
 * 修改人：  Tom Hawk
 * 修改时间：2019/10/8 11:08
 * 修改内容：
 */
class AllAppActivity : AppCompatActivity(R.layout.activity_all_app) {
    private lateinit var adapter: RVAdapter<PackageInfo>
    private var hasChanged = false
    private var currentFilter = R.id.actionAllApps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        PkgsHolder.setup(this)
        setupList()
    }

    private fun setupList() {
        adapter = RVAdapter<PackageInfo>(this, AllVHFactory(packageManager))
            .bind(rvApps)
            .setSelectable(PackageInfo::class.java, SelectStrategy.MULTI_SELECTABLE)
            .setItemClickListener { _, item, index ->
                if (adapter.getSelections().contains(item)) {
                    adapter.deselectAt(index)
                } else {
                    adapter.selectAt(index)
                }
                hasChanged = true
            }

        PkgsHolder.observe(lifecycle) { _, _ ->
            adapter.setItems(getAppsByFilter())
            refreshSelections()
        }
    }

    /**
     * 刷新选中项，
     * 如果还没有选中过，通过保存的数据刷新
     * 如果已有选中，从列表中取出选中的数据进行刷新
     */
    private fun refreshSelections() {
        val selections = adapter.getSelections()
        val pkgs = if (selections.isEmpty()) {
            val appItems = SPHelper.getAppItems()
            if (appItems.isEmpty()) {
                return
            }
            Array<String>(appItems.size) { i -> appItems[i].optString(AppConst.APP_PKG) }
        } else {
            Array<String>(selections.size) { i -> selections.elementAt(i).packageName }.also {
                //清空选中项，避免后续重复选中：
                adapter.setSelectable(selectable = true, clearSelections = true, needNotify = false)
            }
        }

        if (pkgs.isNotEmpty()) {
            PkgsHolder.getAllApps().forEach {
                if (pkgs.contains(it.packageName)) {
                    adapter.select(it)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_all_app, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.actionRefresh -> PkgsHolder.updateList()
            R.id.menuFilter -> return true
            else -> {
                item.isChecked = true
                currentFilter = item.itemId
                adapter.setItems(getAppsByFilter(), false)
            }
        }
        return true
    }

    private fun getAppsByFilter(): List<PackageInfo> {
        return when (currentFilter) {
            R.id.actionAllApps -> PkgsHolder.getAllApps()
            R.id.actionSimpleApps -> PkgsHolder.getInstalledApps()
            R.id.actionSystemApps -> PkgsHolder.getSystemApps()
            else -> throw IllegalArgumentException("Parameter 'currentFilter' is illegal!")
        }
    }

    override fun onBackPressed() {
        if (hasChanged) {
            SPHelper.saveSelections(packageManager, adapter.getSelections())
            setResult(Activity.RESULT_OK)
        }
        finish()
    }
}