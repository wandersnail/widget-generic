package cn.wandersnail.widget.viewpager;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * date: 2019/8/22 23:28
 * author: zengfansheng
 */
public class BasePagerAdapter extends PagerAdapter {
    private List<Pager> pagers;

    public BasePagerAdapter(@NonNull List<Pager> pagers) {
        this.pagers = pagers;
    }

    @Override
    public int getCount() {
        return pagers.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = pagers.get(position).getContentView();
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public interface Pager {
        View getContentView();
    }
}
