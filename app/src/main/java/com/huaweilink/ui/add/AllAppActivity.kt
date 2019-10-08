package com.huaweilink.ui.add

import android.app.Activity
import android.content.pm.PackageInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.adapter.BRecyclerAdapter
import com.huaweilink.R
import com.huaweilink.constant.AppConst
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
    private var isAllSelected: Boolean = false
    private lateinit var adapter: BRecyclerAdapter<PackageInfo>
    private var hasChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_app)

        title = "所有应用"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupList()
    }

    private fun setupList() {
        val items = packageManager.getInstalledPackages(0)

        adapter = BRecyclerAdapter<PackageInfo>(this, AllVHFactory(packageManager))
                .bindRecyclerView(rvApps)
                .setItemInfo(PackageInfo::class.java, selectable = true, multiSelectable = true)
                .setItems(items)
                .setItemClickListener { _, _, _ -> hasChanged = true }

        val array = SPHelper.getAppItems()

        if (array.isEmpty()) {
            return
        }

        val pkgs = ArrayList<String>(array.size)

        array.forEach { item ->
            pkgs.add(item.optString(AppConst.APP_PKG))
        }

        items.forEachIndexed { index, item ->
            if (pkgs.contains(item.packageName)) {
                adapter.selections.add(item)
                adapter.notifyItemChanged(index, BRecyclerAdapter.FLAG_PAYLOADS_SELECT)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_all_app, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.menuToggleSelectAll -> {
                isAllSelected = !isAllSelected
                hasChanged = true

                if (isAllSelected) {
                    item.title = "全不选"
                    adapter.selectAll()
                } else {
                    item.title = "全选"
                    adapter.deselectAll()
                }

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (hasChanged) {
            saveSelections()
            setResult(Activity.RESULT_OK)
        }

        finish()
    }

    private fun saveSelections() {
        val items = SPHelper.getAppItems()
        items.clear()

        adapter.selections.forEach { selection ->
            val item = JSONObject().put(AppConst.APP_NAME, selection.applicationInfo.loadLabel(packageManager))
                    .put(AppConst.APP_PKG, selection.packageName)

            items.add(item)
        }

        SPHelper.saveAppItems()
    }
}