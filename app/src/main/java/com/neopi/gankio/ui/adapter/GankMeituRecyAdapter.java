package com.neopi.gankio.ui.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import com.neopi.gankio.MainActivity;
import com.neopi.gankio.R;
import com.neopi.gankio.model.GankResult;
import com.neopi.gankio.ui.activity.MeiziDetaileActivity;

/**
 * Created by neopi on 17-3-1.
 */

public class GankMeituRecyAdapter extends BaseRecycleViewAdapter<GankResult,GankMeituViewHolder> {

  private Context mContext = null;
  public GankMeituRecyAdapter(Context context) {
    super(context);
    this.mContext = context ;
  }

  @Override public void onItemClick(View itemView, int position, GankResult data) {
    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((MainActivity)mContext,itemView,"meizi");
    Intent intent = new Intent(mContext, MeiziDetaileActivity.class);
    intent.putExtra(MeiziDetaileActivity.EXTRA_IMG_URL,data.getUrl());
    mContext.startActivity(intent);
  }

  @Override public GankMeituViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new GankMeituViewHolder(parent, R.layout.item_gank_meizi_layout);
  }
}
