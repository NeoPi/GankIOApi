package com.neopi.gankio.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecycleViewAdapter<D, H extends BaseViewHolder<D>>
    extends RecyclerView.Adapter<H> {
  protected Context mContext;
  protected ArrayList<D> mData;
  private final static String TAG = "BaseDataAdapter";

  public BaseRecycleViewAdapter(Context context) {
    mContext = context;
    mData = new ArrayList<>();
  }

  @Override public void onBindViewHolder(H holder, int position) {
    D data = getItem(position);
    if (null != data){
      holder.setData(data,position);
      holder.itemView.setOnClickListener(v ->
          onItemClick(v,position,data));
    }
  }

  @Override public int getItemCount() {
    return mData.size();
  }

  /**
   *
   * @param position
   * @return
   */
  public D getItem(int position){
    D data = null;
    if (position < getItemCount()){
      data = mData.get(position);
    }
    return data;
  }

  /**
   * 更新item数据
   */
  public void updateItem(H holder, D data) {
    mData.set(holder.getLayoutPosition(), data);
    notifyDataSetChanged();
  }

  /**
   * 更新列表的数据
   * @param mDatas
   */
  public void updateData(List<D> mDatas){
    mData.clear();
    mData.addAll(mDatas);
    notifyDataSetChanged();
  }

  /**
   * 追加一条数据
   * @param data
   */
  public void appendData(D data){
    mData.add(data);
    notifyDataSetChanged();
  }

  /**
   * 追加列表数据
   * @param mLists
   */
  public void appendLists(List<D> mLists){
    mData.addAll(mLists);
    notifyDataSetChanged();
  }

  public abstract void onItemClick(View itemView,int position,D data);

  //public abstract View newView(Context context, int position, D data, ViewGroup parent);
  //
  //public abstract void bindView(View view, int position, D data);
}