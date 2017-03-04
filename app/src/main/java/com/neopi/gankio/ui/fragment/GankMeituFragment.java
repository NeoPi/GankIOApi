package com.neopi.gankio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.neopi.gankio.R;
import com.neopi.gankio.api.GankApi;
import com.neopi.gankio.api.MeiziApi;
import com.neopi.gankio.model.GankMeiziData;
import com.neopi.gankio.ui.adapter.GankMeituRecyAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by neopi on 17-3-1.
 */

public class GankMeituFragment extends Fragment {

  private View rootView = null;
  private RecyclerView mRecyclerView ;
  private GankMeituRecyAdapter mGankMeituRecyAdapter ;
  private GankMeiziData mData = null;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (rootView == null) {
      rootView = inflater.inflate(R.layout.fragment_gank_layout,container,false);

      initView();


    }
    return rootView;

  }

  private void initView() {

    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.gank_recycle_view);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(linearLayoutManager);
    mGankMeituRecyAdapter = new GankMeituRecyAdapter(getActivity());
    mRecyclerView.setAdapter(mGankMeituRecyAdapter);

  }

  @Override public void onResume() {
    super.onResume();

    loadNetData();

  }


  private void loadNetData() {

    MeiziApi.getMeitu(10,1)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<GankMeiziData>() {
          @Override public void onSubscribe(Disposable d) {
            Log.e("1111","load data onSubscribe");
          }

          @Override public void onNext(GankMeiziData gankMeiziData) {
            mData = gankMeiziData ;
            mGankMeituRecyAdapter.updateData(mData.getResults());
          }

          @Override public void onError(Throwable e) {
            e.printStackTrace();
          }

          @Override public void onComplete() {

          }
        });


  }

}
