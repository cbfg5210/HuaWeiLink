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
import com.huaweilink.util.SPHelper
import kotlinx.android.synthetic.main.activity_all_app.*
import org.json.JSONObject
import java.text.Collator
import java.util.*
import kotlin.collections.ArrayList

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
        val items = packageManager.getInstalledPackages(0)
        val collator = Collator.getInstance(Locale.CHINA)
        items.sortWith { o1, o2 -> collator.compare(o1.applicationInfo.loadLabel(packageManager), o2.applicationInfo.loadLabel(packageManager)) }

        adapter = RVAdapter<PackageInfo>(this, AllVHFactory(packageManager))
                .bind(rvApps)
                .setSelectable(PackageInfo::class.java, SelectStrategy.MULTI_SELECTABLE)
                .setItems(items)
                .setItemClickListener { _, item, index ->
                    if (adapter.getSelections().contains(item)) {
                        adapter.deselectAt(index)
                    } else {
                        adapter.selectAt(index)
                    }
                    hasChanged = true
                }

        val array = SPHelper.getAppItems()

        if (array.isEmpty()) {
            return
        }

        val pkgs = ArrayList<String>(array.size)

        array.forEach { item -> pkgs.add(item.optString(AppConst.APP_PKG)) }

        items.forEachIndexed { _, item ->
            if (pkgs.contains(item.packageName)) {
                adapter.select(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_all_app, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        /*if (isAllSelected) {
            menu.findItem(R.id.menuToggleSelectAll).title = "全不选"
        }*/
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.menuToggleSelectAll -> {
                /*isAllSelected = !isAllSelected
                hasChanged = true

                if (isAllSelected) {
                    item.title = "全不选"
                    adapter.selectAll()
                } else {
                    item.title = "全选"
                    adapter.deselectAll()
                }*/

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

        adapter.getSelections().forEach { selection ->
            JSONObject().put(AppConst.APP_NAME, selection.applicationInfo.loadLabel(packageManager))
                    .put(AppConst.APP_PKG, selection.packageName)
                    .run { items.add(this) }
        }

        SPHelper.saveAppItems()
    }
}