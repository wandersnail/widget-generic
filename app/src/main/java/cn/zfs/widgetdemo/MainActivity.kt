package cn.zfs.widgetdemo

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.snail.commons.helper.PermissionsRequester
import com.snail.commons.utils.Logger
import com.snail.commons.utils.ToastUtils
import com.snail.widget.listview.BaseListAdapter
import com.snail.widget.listview.BaseViewHolder
import com.snail.widget.listview.InflatedViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var requester: PermissionsRequester? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val data = arrayListOf("倾斜TextView", "SwitchButton", "可滑动标签选择", "可拖拽条目列表", "圆角图片", "圆角按钮", "刻度尺", "文本选择器", "对话框")
        val clsArr = arrayListOf(RotatableTextViewActivity::class.java, SwitchButtonActivity::class.java, HorizontalLabelPickerActivity::class.java,
                DragSwipeItemActivity::class.java, RoundImageActivity::class.java, RoundButtonActivity::class.java, ScaleViewActivity::class.java,
                StringPickerActivity::class.java, DefaultAlertDialogActivity::class.java)
        lv.adapter = object : BaseListAdapter<String>(this, data) {
            override fun createViewHolder(position: Int): BaseViewHolder<String> {
                return object : InflatedViewHolder<String>(context, android.R.layout.simple_list_item_1) {
                    private var tv: TextView = getView(android.R.id.text1)

                    override fun onBind(item: String, position: Int) {
                        tv.text = item
                    }
                }
            }
        }
        lv.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, clsArr[position])
            intent.putExtra("title", data[position])
            startActivity(intent)
        }
        Logger.setPrintLevel(Logger.ALL)
        requester = PermissionsRequester(this)
        val list = ArrayList<String>()
        list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        list.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        list.add(Manifest.permission.ACCESS_FINE_LOCATION)
        list.add(Manifest.permission.ACCESS_NETWORK_STATE)
        requester?.checkAndRequest(list)
        requester?.setOnRequestResultListener(object : PermissionsRequester.OnRequestResultListener {
            override fun onRequestResult(refusedPermissions: MutableList<String>) {
                if (refusedPermissions.isNotEmpty()) {
                    ToastUtils.showShort("部分权限被拒绝，可能造成某些功能无法使用")
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        requester?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        requester?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
