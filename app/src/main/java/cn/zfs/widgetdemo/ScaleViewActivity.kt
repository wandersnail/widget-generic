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
        val params = scaleView.obtainParams()
        params.setScope(0, 1700)
        params.setLabelFormatter(object : ScaleView.TextFormatterCallback {
            override fun format(value: Float): String {
                return DecimalFormat("0.00").format((value / 100.0f).toDouble())
            }
        })
        params.setOnValueUpdateCallback(object : ScaleView.OnValueUpdateCallback {
            override fun onValueUpdate(value: Float) {
                tv.text = value.toString()
            }
        })
        params.apply()
        scaleView.setValue(800f)
        
        val params1 = scaleView1.obtainParams()
        params1.setScope(0, 100)
        params1.setShortLongtScaleRatio(1f)
        params1.setScaleSpace(UiUtils.dp2px(30f))
        params1.setLabelFormatter(object : ScaleView.TextFormatterCallback {
            override fun format(value: Float): String {
                return value.toInt().toString()
            }
        })
        params1.setOnValueUpdateCallback(object : ScaleView.OnValueUpdateCallback {
            override fun onValueUpdate(value: Float) {
                tv.text = value.toString()
            }
        })
        scaleView1.setValue(50f)
    }
}