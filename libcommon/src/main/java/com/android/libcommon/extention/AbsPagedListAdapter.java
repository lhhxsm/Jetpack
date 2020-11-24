package com.android.libcommon.extention;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 一个能够添加HeaderView,FooterView的PagedListAdapter。
 * 解决了添加HeaderView和FooterView时 RecyclerView定位不准确的问题
 *
 * @param <T>  Java Bean
 * @param <VH>
 */
public abstract class AbsPagedListAdapter<T, VH extends RecyclerView.ViewHolder> extends PagedListAdapter<T, VH> {
    private final SparseArray<View> mHeaders = new SparseArray<>();
    private final SparseArray<View> mFooters = new SparseArray<>();

    private int BASE_ITEM_TYPE_HEADER = 100000;
    private int BASE_ITEM_TYPE_FOOTER = 200000;
    private OnItemClickListener<T> mOnItemClickListener;
    private OnItemLongClickListener<T> mOnItemLongClickListener;

    protected AbsPagedListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }


    public void addHeaderView(View view) {
        //判断给View对象是否还没有处在mHeaders数组里面
        if (mHeaders.indexOfValue(view) < 0) {
            mHeaders.put(BASE_ITEM_TYPE_HEADER++, view);
            notifyDataSetChanged();
        }
    }

    public void addFooterView(View view) {
        //判断给View对象是否还没有处在mFooters数组里面
        if (mFooters.indexOfValue(view) < 0) {
            mFooters.put(BASE_ITEM_TYPE_FOOTER++, view);
            notifyDataSetChanged();
        }
    }

    // 移除头部
    public void removeHeaderView(View view) {
        int index = mHeaders.indexOfValue(view);
        if (index < 0) return;
        mHeaders.removeAt(index);
        notifyDataSetChanged();
    }

    // 移除底部
    public void removeFooterView(View view) {
        int index = mFooters.indexOfValue(view);
        if (index < 0) return;
        mFooters.removeAt(index);
        notifyDataSetChanged();
    }

    public int getHeaderCount() {
        return mHeaders.size();
    }

    public int getFooterCount() {
        return mFooters.size();
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        return itemCount + mHeaders.size() + mFooters.size();
    }

    public int getOriginalItemCount() {
        return getItemCount() - mHeaders.size() - mFooters.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            //返回该position对应的headerview的  viewType
            return mHeaders.keyAt(position);
        }

        if (isFooterPosition(position)) {
            //footer类型的，需要计算一下它的position实际大小
            position = position - getOriginalItemCount() - mHeaders.size();
            return mFooters.keyAt(position);
        }
        position = position - mHeaders.size();
        return getItemViewType2(position);
    }

    protected int getItemViewType2(int position) {
        return 0;
    }

    private boolean isFooterPosition(int position) {
        return position >= getOriginalItemCount() + mHeaders.size();
    }

    private boolean isHeaderPosition(int position) {
        return position < mHeaders.size();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaders.indexOfKey(viewType) >= 0) {
            View view = mHeaders.get(viewType);
            return (VH) new RecyclerView.ViewHolder(view) {
            };
        }

        if (mFooters.indexOfKey(viewType) >= 0) {
            View view = mFooters.get(viewType);
            return (VH) new RecyclerView.ViewHolder(view) {
            };
        }
        VH vh = onCreateViewHolder2(parent, viewType);
        vh.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                int position = vh.getBindingAdapterPosition();
                mOnItemClickListener.onItemClick(getItem(position), position);
            }
        });
        vh.itemView.setOnLongClickListener(v -> {
            if (mOnItemLongClickListener != null) {
                int position = vh.getBindingAdapterPosition();
                mOnItemLongClickListener.onItemLongClick(getItem(position), position);
                return true;
            }
            return false;
        });
        return vh;
    }

    protected abstract VH onCreateViewHolder2(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (isHeaderPosition(position) || isFooterPosition(position))
            return;
        //列表中正常类型的itemView的 position 咱们需要减去添加headerView的个数
        position = position - mHeaders.size();
        onBindViewHolder2(holder, position);
    }

    protected abstract void onBindViewHolder2(VH holder, int position);

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (!isHeaderPosition(holder.getAdapterPosition()) && !isFooterPosition(holder.getAdapterPosition())) {
            this.onViewAttachedToWindow2((VH) holder);
        }
    }

    public void onViewAttachedToWindow2(VH holder) {

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (!isHeaderPosition(holder.getAdapterPosition()) && !isFooterPosition(holder.getAdapterPosition())) {
            this.onViewDetachedFromWindow2((VH) holder);
        }
    }

    public void onViewDetachedFromWindow2(VH holder) {

    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(new AdapterDataObserverProxy(observer));
    }

    public interface OnItemClickListener<M> {
        void onItemClick(M item, int position);
    }

    public interface OnItemLongClickListener<M> {
        void onItemLongClick(M item, int position);
    }

    //如果我们先添加了headerView,而后网络数据回来了再更新到列表上
    //由于Paging在计算列表上item的位置时 并不会顾及我们有没有添加headerView，就会出现列表定位的问题
    //实际上 RecyclerView#setAdapter方法，它会给Adapter注册了一个AdapterDataObserver
    //咱么可以代理registerAdapterDataObserver()传递进来的observer。在各个方法的实现中，把headerView的个数算上，再中转出去即可
    private class AdapterDataObserverProxy extends RecyclerView.AdapterDataObserver {
        private final RecyclerView.AdapterDataObserver mObserver;

        public AdapterDataObserverProxy(RecyclerView.AdapterDataObserver observer) {
            mObserver = observer;
        }

        public void onChanged() {
            mObserver.onChanged();
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            mObserver.onItemRangeChanged(positionStart + mHeaders.size(), itemCount);
        }

        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            mObserver.onItemRangeChanged(positionStart + mHeaders.size(), itemCount, payload);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            mObserver.onItemRangeInserted(positionStart + mHeaders.size(), itemCount);
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mObserver.onItemRangeRemoved(positionStart + mHeaders.size(), itemCount);
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mObserver.onItemRangeMoved(fromPosition + mHeaders.size(), toPosition + mHeaders.size(), itemCount);
        }
    }
}
