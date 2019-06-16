package cn.zfs.widgetdemo

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.snail.commons.utils.ToastUtils
import com.snail.widget.listener.RejectFastViewClickListener
import com.snail.widget.listener.RejectableViewClickCallback

/**
 *
 *
 * date: 2019-06-16 12:46
 * author: zengfansheng
 */
class RejectFastClickActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val btn = Button(this)
        btn.text = "快速点击测试"
        setContentView(btn, ViewGroup.LayoutParams(-2, -2))
        btn.setOnClickListener(RejectFastViewClickListener(object : RejectableViewClickCallback {
            override fun onReject(v: View) {
                ToastUtils.showShort("Click frequency too fast")
            }

            override fun onAccept(v: View) {
                ToastUtils.showShort("Click accept")
            }
        }))
    }
}