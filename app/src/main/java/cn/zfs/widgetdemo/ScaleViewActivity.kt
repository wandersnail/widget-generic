package cn.zfs.widgetdemo

import android.os.Bundle
import com.snail.commons.utils.UiUtils
import com.snail.widget.scale.ScaleView
import kotlinx.android.synthetic.main.activity_scale_view.*
import java.text.DecimalFormat

/**
 *
 *
 * date: 2019/4/6 08:59
 * author: zengfansheng
 */
class ScaleViewActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale_view)
        scaleView.setScope(20, 1700)
        scaleView.setLabelFormatter(object : ScaleView.TextFormatterCallback {
            override fun format(value: Float): String {
                return DecimalFormat("0.00").format((value / 100.0f).toDouble())
            }
        })
        scaleView.setOnValueUpdateCallback(object : ScaleView.OnValueUpdateCallback {
            override fun onValueUpdate(value: Float) {
                tv.text = value.toString()
            }
        })
        scaleView.setValue(80f)
        scaleView1.setScope(0, 10)
        scaleView1.setShortLongtScaleRatio(1f)
        scaleView1.setScaleSpace(UiUtils.dp2px(30f))
        scaleView1.setLabelFormatter(object : ScaleView.TextFormatterCallback {
            override fun format(value: Float): String {
                return value.toInt().toString()
            }
        })
        scaleView1.setOnValueUpdateCallback(object : ScaleView.OnValueUpdateCallback {
            override fun onValueUpdate(value: Float) {
                tv.text = value.toString()
            }
        })
    }
}