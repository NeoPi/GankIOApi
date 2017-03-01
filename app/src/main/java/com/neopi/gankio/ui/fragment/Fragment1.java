package com.neopi.gankio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.neopi.gankio.R;

/**
 * Created by neopi on 17-3-1.
 */

public class Fragment1 extends Fragment {

  private View rootView;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.single_text_layout,container,false);

    return rootView;
  }
}
