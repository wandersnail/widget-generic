package cn.zfs.widgetdemo

import android.os.Bundle
import android.util.Log
import android.view.View
import com.snail.commons.utils.ToastUtils
import com.snail.widget.dialog.BaseDialog
import com.snail.widget.dialog.DefaultAlertDialog
import com.snail.widget.dialog.DialogEventObserver
import kotlinx.android.synthetic.main.activity_default_alert_dialog.*

/**
 * 对话框
 *
 * date: 2019/4/9 15:11
 * author: zengfansheng
 */
class DefaultAlertDialogActivity : BaseActivity() {
    private var dialog: BaseDialog? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default_alert_dialog)
        btnWidthTitle.setOnClickListener { 
            if (dialog == null) {
                dialog = DefaultAlertDialog(this).setTitle("标题")
                        .setMessage("这是消息内容")
                        .setNegativeButton("Cancel", View.OnClickListener {
                            ToastUtils.showShort("点击了Cancel")
                        })
                        .setPositiveButton("Ok", View.OnClickListener { 
                            ToastUtils.showShort("点击了Ok")
                        })
                dialog!!.registerEventObserver(observer)
            }
            dialog!!.show()
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

    override fun onDestroy() {
        dialog?.unregisterEventAll()
        super.onDestroy()
    }
    
    private val observer = object : DialogEventObserver {
        override fun onContentChanged() {
            Log.d("DialogEventObserver", "onContentChanged")
        }

        override fun onWindowFocusChanged(hasFocus: Boolean) {
            Log.d("DialogEventObserver", "onWindowFocusChanged: $hasFocus")
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            Log.d("DialogEventObserver", "onCreate")
        }

        override fun onStart() {
            Log.d("DialogEventObserver", "onStart")
        }

        override fun onStop() {
            Log.d("DialogEventObserver", "onStop")
        }

        override fun onShow() {
            Log.d("DialogEventObserver", "onShow")
        }

        override fun onDismiss() {
            Log.d("DialogEventObserver", "onDismiss")
        }

        override fun onCancel() {
            Log.d("DialogEventObserver", "onCancel")
        }

        override fun onBackPressed() {
            Log.d("DialogEventObserver", "onBackPressed")
        }

        override fun onAttachedToWindow() {
            Log.d("DialogEventObserver", "onAttachedToWindow")
        }

        override fun onDetachedFromWindow() {
            Log.d("DialogEventObserver", "onDetachedFromWindow")
        }
    }
}