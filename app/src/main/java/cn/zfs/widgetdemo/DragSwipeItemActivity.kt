package cn.zfs.widgetdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.snail.widget.recyclerview.BaseItemTouchAdapter
import com.snail.widget.recyclerview.BaseItemTouchViewHolder
import com.snail.widget.recyclerview.SimpleItemTouchCallback
import kotlinx.android.synthetic.main.activity_draggable_item.*

/**
 *
 *
 * date: 2019/3/9 23:49
 * author: zengfansheng
 */
class DragSwipeItemActivity : BaseActivity() {
    private var helper: ItemTouchHelper? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draggable_item)
        val list = mutableListOf("Java", "Kotlin", "PHP", "Html", "Python", "JavaScript", "C++", "C", "C#", "Swift", 
                "Objective-C", "Go", "SQL", "Ruby", "MATLAB", "Perl", "Delphi", "Visual Basic", "Visual Basic .NET")
        val adapter = ItemTouchAdapter(list)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val callback = SimpleItemTouchCallback(adapter)
        helper = ItemTouchHelper(callback)
        helper!!.attachToRecyclerView(recyclerView)
    }
    
    private inner class ViewHolder(itemView: View) : BaseItemTouchViewHolder(itemView) {
        val tv: TextView = itemView.findViewById(R.id.text)
        val ivHandle: ImageView = itemView.findViewById(R.id.handle)
        
        init {
            ivHandle.setOnTouchListener { v, event -> 
                if (event.action == MotionEvent.ACTION_DOWN) {
                    helper?.startDrag(this)
                }
                return@setOnTouchListener true
            }
        }
        
        override fun onSwipe() {
        }

        override fun onDrag() {
        }

        override fun onClear() {
        }
    }

    private inner class ItemTouchAdapter(list: MutableList<String>) : BaseItemTouchAdapter<String, ViewHolder>(list) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, item: String, position: Int) {
            holder.tv.text = item
        }
    }
}