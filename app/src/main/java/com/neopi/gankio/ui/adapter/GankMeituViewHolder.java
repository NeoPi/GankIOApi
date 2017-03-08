package com.neopi.gankio.ui.adapter;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.support.annotation.LayoutRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.neopi.gankio.MainActivity;
import com.neopi.gankio.R;
import com.neopi.gankio.model.GankResult;
import com.neopi.gankio.ui.activity.MeiziDetaileActivity;
import com.neopi.gankio.utils.DeviceUtils;
import javax.crypto.Mac;

/**
 * Created by neopi on 17-3-1.
 */

public class GankMeituViewHolder extends BaseViewHolder<GankResult> {

  private SimpleDraweeView mSimpleDraweeView = null;
  private TextView mAuthor = null;
  private TextView mDate = null;

  public GankMeituViewHolder(ViewGroup parent, @LayoutRes int layoutId) {
    super(parent, layoutId);

    mSimpleDraweeView = getView(R.id.meizi_image);
    mAuthor = getView(R.id.meizi_author);
    mDate = getView(R.id.meizi_date);

  }

  @Override public void setData(GankResult gankResult, int position) {

    mAuthor.setText(gankResult.getWho());
    mDate.setText(gankResult.getCreatedAt());

    ViewGroup.LayoutParams layoutParams = mSimpleDraweeView.getLayoutParams();
    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){
      @Override
      public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
        if (imageInfo == null) {
          return ;
        }

        layoutParams.width = DeviceUtils.SCREEN_WIDTH;
        layoutParams.height = (int)((float) (DeviceUtils.SCREEN_WIDTH * imageInfo.getHeight() / imageInfo.getWidth()));
        mSimpleDraweeView.setLayoutParams(layoutParams);
      }

      @Override public void onIntermediateImageSet(String id, ImageInfo imageInfo) {

      }

      @Override public void onFailure(String id, Throwable throwable) {
        throwable.printStackTrace();
      }
    };

    DraweeController mControl = Fresco.newDraweeControllerBuilder()
        .setControllerListener(controllerListener)
        .setUri(gankResult.getUrl())
        .build();
    mSimpleDraweeView.setController(mControl);


    mSimpleDraweeView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Pair<View,String> pairImage = new Pair<View, String>(mSimpleDraweeView,"image");
        Pair<View,String> pairTv = new Pair<View, String>(mAuthor,"mAuthor");

        ActivityOptionsCompat options =
            ActivityOptionsCompat.makeSceneTransitionAnimation((MainActivity)getContext(), pairImage,
                pairTv);
        Intent intent = new Intent(getContext(), MeiziDetaileActivity.class);
        intent.putExtra(MeiziDetaileActivity.EXTRA_IMG_URL,gankResult.getUrl());
        intent.putExtra(MeiziDetaileActivity.EXTRA_AUTHOR,gankResult.getWho());
        intent.putExtra(MeiziDetaileActivity.EXTRA_IMAGE_WIDTH,mSimpleDraweeView.getWidth());
        intent.putExtra(MeiziDetaileActivity.EXTRA_IMAGE_HEIGHT,mSimpleDraweeView.getHeight());
        ActivityCompat.startActivity(getContext(),intent,options.toBundle());
      }
    });

  }
}
