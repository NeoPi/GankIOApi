package com.neopi.gankio.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.neopi.gankio.R;
import com.neopi.gankio.model.GankResult;

/**
 * Created by neopi on 17-3-1.
 */

public class GankMeituRecyAdapter extends BaseRecycleViewAdapter<GankResult,GankMeituViewHolder> {


  public GankMeituRecyAdapter(Context context) {
    super(context);

  }

  @Override public void onItemClick(View itemView, int position, GankResult data) {

  }

  @Override public GankMeituViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new GankMeituViewHolder(parent, R.layout.item_gank_meizi_layout);
  }
}
