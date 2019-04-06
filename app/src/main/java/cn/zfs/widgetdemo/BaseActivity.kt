package cn.zfs.widgetdemo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity


/**
 * Created by zeng on 2016/6/16.
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val title = intent.getStringExtra("title")
        if (title != null) {
            setTitle(title)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
