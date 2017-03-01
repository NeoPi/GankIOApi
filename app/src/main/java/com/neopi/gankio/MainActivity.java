package com.neopi.gankio;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.neopi.gankio.ui.fragment.Fragment1;
import com.neopi.gankio.ui.fragment.GankMeituFragment;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {


  private ViewPager mViewPager ;
  private Toolbar mToolBar ;
  private TabLayout mBottomLayout = null;

  private String[] BOTTOMS = {"首页","美图","发现"};
  private List<Fragment> mFragments = new ArrayList<>();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initView();


  }

  private void initView() {
    mToolBar = (Toolbar) findViewById(R.id.public_toolbar);
    setSupportActionBar(mToolBar);

    mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
    mBottomLayout = (TabLayout) findViewById(R.id.main_bottom_bar);
    mBottomLayout.setupWithViewPager(mViewPager);
    mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

      @Override public Fragment getItem(int position) {
        return mFragments.get(position);
      }

      @Override public int getCount() {
        return BOTTOMS.length;
      }


    });
    initBottomView();



  }

  private void initBottomView() {
    mFragments.add(new Fragment1());
    mFragments.add(new GankMeituFragment());
    mFragments.add(new Fragment1());
    for (int i = 0; i < mBottomLayout.getTabCount(); i++) {
      View child = LayoutInflater.from(this).inflate(R.layout.bottom_item,null);
      TextView title  = (TextView) child.findViewById(R.id.item_title);
      TabLayout.Tab tab = mBottomLayout.getTabAt(i);
      if (tab != null) {
        title.setText(BOTTOMS[i]);
        tab.setCustomView(child);
      }
    }
  }
}
