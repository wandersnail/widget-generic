package cn.zfs.widgetdemo

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.snail.commons.utils.UiUtils
import com.snail.widget.StringPicker
import kotlinx.android.synthetic.main.activity_string_picker.*


/**
 * 文本选择器
 *
 * date: 2019/4/6 21:01
 * author: zengfansheng
 */
class StringPickerActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_string_picker)
        picker.setOnSelectListener(object : StringPicker.OnSelectListener {
            override fun onSelect(text: String) {
                tvValue.text = text
            }
        })
        val list = mutableListOf("黑龙江省", "辽宁省", "吉林省", "河北省", "河南省", "湖北省", "湖南省", "山东省", "山西省", "陕西省", "安徽省", 
                "浙江省", "江苏省", "福建省", "广东省", "海南省", "四川省", "云南省", "贵州省", "青海省", "甘肃省", "江西省", "台湾省")
        picker.setData(list)
        picker.setLoopEnable(true)
        picker.setTextSize(UiUtils.dp2pxF(14f), UiUtils.dp2pxF(34f))
        picker.setTextColor(Color.GRAY, ContextCompat.getColor(this, R.color.colorAccent))
        picker.setTextSpace(UiUtils.dp2pxF(30f))
        picker.select("安徽省")
    }
}