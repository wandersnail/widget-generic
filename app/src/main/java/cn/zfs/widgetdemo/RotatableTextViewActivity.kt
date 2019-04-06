package cn.zfs.widgetdemo

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_rotatable_text_view.*

/**
 * 可旋转TextView
 *
 * date: 2018/12/29 22:40
 * author: zengfansheng
 */
class RotatableTextViewActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rotatable_text_view)
        ivFavor.setColorFilter(0xffFF9843.toInt())
        btn.setOnClickListener {
            tvRotate.text = et.text?.toString()
        }
    }
}