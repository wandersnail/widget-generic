package cn.zfs.widgetdemo

import android.os.Bundle
import android.view.View
import com.snail.commons.utils.ToastUtils
import com.snail.widget.dialog.DefaultAlertDialog
import kotlinx.android.synthetic.main.activity_default_alert_dialog.*

/**
 * 对话框
 *
 * date: 2019/4/9 15:11
 * author: zengfansheng
 */
class DefaultAlertDialogActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default_alert_dialog)
        btnWidthTitle.setOnClickListener { 
            DefaultAlertDialog(this).setTitle("标题")
                    .setMessage("这是消息内容")
                    .setNegativeButton("Cancel", View.OnClickListener {
                        ToastUtils.showShort("点击了Cancel")
                    })
                    .setPositiveButton("Ok", View.OnClickListener { 
                        ToastUtils.showShort("点击了Ok")
                    })
                    .setCancelable(false)
                    .show()
        }
        btnSingleNoTitle.setOnClickListener {
            DefaultAlertDialog(this)
                    .setMessage("这是消息内容")
                    .setPositiveButton("Ok", View.OnClickListener {
                        ToastUtils.showShort("点击了Ok")
                    })
                    .setCancelable(false)
                    .show()
        }
        btnAutoDismiss.setOnClickListener {
            DefaultAlertDialog(this)
                    .setMessage("这是消息内容")
                    .setAutoDismissDelayMillis(2000)
                    .setAutoDismiss(true)
                    .setCancelable(false)
                    .show()
        }
    }
}