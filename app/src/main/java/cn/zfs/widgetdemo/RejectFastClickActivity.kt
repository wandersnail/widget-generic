package cn.zfs.widgetdemo

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import cn.wandersnail.widget.listener.RejectFastViewClickListener
import cn.wandersnail.widget.listener.RejectableViewClickCallback
import com.snail.commons.utils.ToastUtils

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