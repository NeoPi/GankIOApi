package com.neopi.gankio.ui.activity;

import android.app.ProgressDialog;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.neopi.gankio.BaseActivity;
import com.neopi.gankio.R;
import com.neopi.gankio.api.GankApi;
import com.neopi.gankio.api.MeiziApi;
import com.neopi.gankio.utils.DeviceUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.io.File;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by neopi on 17-3-2.
 */

public class MeiziDetaileActivity extends BaseActivity {

  public static final String EXTRA_IMG_URL = "extra_img_url";

  private SimpleDraweeView mSimpleDraweeView ;
  private Button mSaveButton = null;
  private String imageUrl = null;
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
    //getWindow().setExitTransition(new Explode());//new Slide()  new Fade()
    setContentView(R.layout.activity_meizi_detail_layout);

    initView();

  }

  private void initView() {
    mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.meizi_detail_img);
    mSaveButton= (Button) findViewById(R.id.btn_save);
    imageUrl = getIntent().getStringExtra(EXTRA_IMG_URL);

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
        .setUri(imageUrl)
        .build();
    mSimpleDraweeView.setController(mControl);


    mSaveButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
      //  MeiziApi.rxDownLoad(imageUrl)
      //      .observeOn(AndroidSchedulers.mainThread())
      //      .subscribe(new Observer<File>() {
      //        @Override public void onSubscribe(Disposable disposable) {
      //
      //        }
      //
      //        @Override public void onNext(File file) {
      //          Log.e("1111",file.getAbsolutePath());
      //        }
      //
      //        @Override public void onError(Throwable throwable) {
      //          throwable.printStackTrace();
      //        }
      //
      //        @Override public void onComplete() {
      //
      //        }
      //      });

        imageUrl = "http://a1.bbs.xiaomi.cn/download/apk/xiaomibbs_20161110.apk";
        GankApi.rxDownLoadWithProgress(imageUrl)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Integer>() {
              Subscription sub = null;
              @Override public void onSubscribe(Subscription subscription) {
                Log.e("1111","onSubscribe :"+Thread.currentThread());
                sub = subscription;
                showDownLoadProgress(0);
                sub.request(Long.MAX_VALUE);
              }

              @Override public void onNext(Integer integer) {
                //Log.e("1111","onNext:"+integer.intValue()+""+Thread.currentThread());
                showDownLoadProgress(integer);
                sub.request(Long.MAX_VALUE);
              }

              @Override public void onError(Throwable throwable) {
                throwable.printStackTrace();
                sub.cancel();
              }

              @Override public void onComplete() {
                Log.e("1111","onComplete:"+Thread.currentThread());
                if (mProgressDialog != null) {
                  mProgressDialog.dismiss();
                }
              }
            });
      }
    });
  }

  ProgressDialog mProgressDialog = null;
  private void showDownLoadProgress(int progress) {
    if (mProgressDialog == null) {
      mProgressDialog = new ProgressDialog(this);
      mProgressDialog.setMax(100);
      mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
      mProgressDialog.setProgressNumberFormat("%1d / %2d");
      mProgressDialog.setCanceledOnTouchOutside(false);
    }


    if (!mProgressDialog.isShowing()) {
      mProgressDialog.show();
    }
    mProgressDialog.setProgress(progress);

  }
}
