package com.neopi.gankio.ui.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by neopi on 16-11-18.
 */

public abstract class BaseViewHolder<D> extends RecyclerView.ViewHolder {

  public BaseViewHolder(ViewGroup parent,@LayoutRes int layoutId) {
    super (LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false));
  }

  public <T extends View> T getView(@IdRes int idRes){
    return (T) itemView.findViewById(idRes);
  }

  public Context getContext(){
    return itemView.getContext();
  }

  public abstract void setData(D d,int position);

}
