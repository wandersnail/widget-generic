package cn.wandersnail.widget.listener;

import android.view.View;
import android.widget.AdapterView;

/**
 * 可以控制是否执行点击事件回调
 * <p>
 * date: 2019/8/22 16:40
 * author: zengfansheng
 */
public interface RejectableItemClickCallback {
    default void onReject(AdapterView<?> parent, View view, int position, long id) {}
    
    void onAccept(AdapterView<?> parent, View view, int position, long id);
}
