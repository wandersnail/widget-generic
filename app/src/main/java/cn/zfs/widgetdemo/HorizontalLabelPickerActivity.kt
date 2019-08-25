package cn.zfs.widgetdemo

import android.os.Bundle
import cn.wandersnail.commons.util.UiUtils
import cn.wandersnail.widget.HorizontalLabelPicker
import kotlinx.android.synthetic.main.activity_horizontal_label_picker.*

/**
 *
 *
 * date: 2019/3/2 20:29
 * author: zengfansheng
 */
class HorizontalLabelPickerActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_label_picker)        
        val builder = HorizontalLabelPicker.Builder()
                .setLabels(listOf(Label("Java"), Label("Kotlin"), Label("Html5"), Label("Python")))
                .setTextSize(UiUtils.dp2px(16f))
        labelPicker.setBuilder(builder)
    }
    
    data class Label(val label: String) : HorizontalLabelPicker.ILabel {
        override fun getText(): String {
            return label
        }
    }
}