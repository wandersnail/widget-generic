package cn.wandersnail.widget.recyclerview;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * date: 2019/8/22 19:58
 * author: zengfansheng
 */
public class SimpleItemTouchCallback extends ItemTouchHelper.Callback {
    protected ItemTouchAdapter adapter;
    private boolean longPressDragEnabled = true;
    private boolean itemViewSwipeEnabled = true;
    /**
     * 条目被拖拽或滑动时的阴影
     */
    private float elevation = 15f;
    private float translationZ = 6f;

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    public float getTranslationZ() {
        return translationZ;
    }

    public void setTranslationZ(float translationZ) {
        this.translationZ = translationZ;
    }

    public SimpleItemTouchCallback(@NonNull ItemTouchAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * 设置长按是否可拖拽
     */
    public void setLongPressDragEnabled(boolean enable) {
        longPressDragEnabled = enable;
    }

    /**
     * 设置是否可滑动删除条目
     */
    public void setItemViewSwipeEnabled(boolean enable) {
        itemViewSwipeEnabled = enable;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return longPressDragEnabled;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return itemViewSwipeEnabled;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, 0);
        } else {
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 
                    ItemTouchHelper.START | ItemTouchHelper.END);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        return adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemSwiped(viewHolder.getAdapterPosition(), direction);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            viewHolder.itemView.setElevation(elevation);
            viewHolder.itemView.setTranslationZ(translationZ);
        }
        if (viewHolder instanceof ItemTouchViewHolder) {
            switch(actionState) {
                case ItemTouchHelper.ACTION_STATE_DRAG:	
                    ((ItemTouchViewHolder) viewHolder).onDrag();
            		break;
                case ItemTouchHelper.ACTION_STATE_SWIPE:
                    ((ItemTouchViewHolder) viewHolder).onSwipe();
                    break;
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setTranslationZ(0);
        viewHolder.itemView.setElevation(0);
        if (viewHolder instanceof ItemTouchViewHolder) {
            ((ItemTouchViewHolder) viewHolder).onClear();
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        switch(actionState) {
            case ItemTouchHelper.ACTION_STATE_DRAG:
                viewHolder.itemView.setTranslationY(dY);
                break;
            case ItemTouchHelper.ACTION_STATE_SWIPE:
                viewHolder.itemView.setTranslationX(dX);
                break;
            default:
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                break;
        }        
    }
}
