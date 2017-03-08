package com.neopi.gankio.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewCompat;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.neopi.gankio.BaseActivity;
import com.neopi.gankio.R;
import com.neopi.gankio.api.GankApi;
import com.neopi.gankio.utils.DeviceUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by neopi on 17-3-2.
 */

public class MeiziDetaileActivity extends BaseActivity {

  public static final String EXTRA_IMG_URL = "extra_img_url";
  public static final String EXTRA_AUTHOR = "extra_author";
  public static final String EXTRA_IMAGE_WIDTH = "extra_img_width";
  public static final String EXTRA_IMAGE_HEIGHT = "extra_img_height";

  private SimpleDraweeView mSimpleDraweeView;
  private Button mSaveButton = null;
  private String imageUrl = null;
  private String mAuthor = null;
  private int width = 0 ;
  private int height = 0 ;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TransitionSet mtransitionset=new TransitionSet();//制定过度动画set
    //mtransitionset.addTransition(new ChangeBounds());//改变表框大小
    //mtransitionset.addTransition(new ChangeImageTransform());//图片移动，还可以是其他的，要什么效果自己添加
    //mtransitionset.setDuration(250);
    //getWindow().setEnterTransition(mtransitionset);//注意，下面是必须的
    //getWindow().setExitTransition(mtransitionset);
    //getWindow().setSharedElementEnterTransition(mtransitionset);
    //getWindow().setSharedElementExitTransition(mtransitionset);
    setContentView(R.layout.activity_meizi_detail_layout);
    startPostponedEnterTransition();
    initView();
  }

  private void initView() {
    mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.meizi_detail_img);
    mSaveButton = (Button) findViewById(R.id.btn_save);
    imageUrl = getIntent().getStringExtra(EXTRA_IMG_URL);
    mAuthor = getIntent().getStringExtra(EXTRA_AUTHOR);
    width = getIntent().getIntExtra(EXTRA_IMAGE_WIDTH,DeviceUtils.SCREEN_WIDTH);
    height = getIntent().getIntExtra(EXTRA_IMAGE_HEIGHT,200);


    ViewCompat.setTransitionName(mSaveButton,"mAuthor");
    ViewCompat.setTransitionName(mSimpleDraweeView,"image");
    mSaveButton.setText(mAuthor);
    ViewGroup.LayoutParams layoutParams = mSimpleDraweeView.getLayoutParams();
    layoutParams.width = width;
    layoutParams.height = height;
    mSimpleDraweeView.setLayoutParams(layoutParams);

    mSaveButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        //imageUrl = "http://a1.bbs.xiaomi.cn/download/apk/xiaomibbs_20161110.apk";

        if (ActivityCompat.checkSelfPermission(MeiziDetaileActivity.this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
          // TODO: 2017/03/04  no write external storage permission

          if (ActivityCompat.shouldShowRequestPermissionRationale(MeiziDetaileActivity.this,
              Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

          } else {
            ActivityCompat.requestPermissions(MeiziDetaileActivity.this,
                new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 100);
          }
        } else {
          startDownload();
        }
      }
    });

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {
      // If we're running on Lollipop and we have added a listener to the shared element
      // transition, load the thumbnail. The listener will load the full-size image when
      // the transition is complete.
      //loadThumbnail();
      loadFullSizeImage();
    } else {
      // If all other cases we should just load the full-size image now
      loadFullSizeImage();
    }
  }

  private void startDownload() {
    GankApi.rxDownLoadWithProgress(imageUrl)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Integer>() {
          Subscription sub = null;

          @Override public void onSubscribe(Subscription subscription) {
            Log.e("1111", "onSubscribe :" + Thread.currentThread());
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
            Log.e("1111", "onComplete:" + Thread.currentThread());
            if (mProgressDialog != null) {
              mProgressDialog.dismiss();
            }
          }
        });
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == 100) {
      if (grantResults.length > 0 && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
        startDownload();
      } else {
        Toast.makeText(MeiziDetaileActivity.this, "无法获取存储sdcard权限，下载失败", Toast.LENGTH_SHORT).show();
      }
    }

    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

  @Override public void onBackPressed() {
    super.onBackPressed();
  }


  private boolean addTransitionListener() {
    final Transition transition = getWindow().getSharedElementEnterTransition();

    if (transition != null) {
      // There is an entering shared element transition so add a listener to it
      transition.addListener(new Transition.TransitionListener() {
        @Override
        public void onTransitionEnd(Transition transition) {
          // As the transition has ended, we can now load the full-size image
          loadFullSizeImage();

          // Make sure we remove ourselves as a listener
          transition.removeListener(this);
        }

        @Override
        public void onTransitionStart(Transition transition) {
          // No-op
        }

        @Override
        public void onTransitionCancel(Transition transition) {
          // Make sure we remove ourselves as a listener
          transition.removeListener(this);
        }

        @Override
        public void onTransitionPause(Transition transition) {
          // No-op
        }

        @Override
        public void onTransitionResume(Transition transition) {
          // No-op
        }
      });
      return true;
    }

    // If we reach here then we have not added a listener
    return false;
  }

  private void loadFullSizeImage() {
    ViewGroup.LayoutParams layoutParams = mSimpleDraweeView.getLayoutParams();
    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
      @Override public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
        if (imageInfo == null) {
          return;
        }
        layoutParams.width = DeviceUtils.SCREEN_WIDTH;
        layoutParams.height = (int) ((float) (DeviceUtils.SCREEN_WIDTH * imageInfo.getHeight()
            / imageInfo.getWidth()));
        mSimpleDraweeView.setLayoutParams(layoutParams);
      }

      @Override public void onIntermediateImageSet(String id, ImageInfo imageInfo) {

      }

      @Override public void onFailure(String id, Throwable throwable) {
        throwable.printStackTrace();
      }


    };

    DraweeController mControl = Fresco.newDraweeControllerBuilder()
        //.setControllerListener(controllerListener)
        .setUri(imageUrl)
        .build();
    mSimpleDraweeView.setController(mControl);

  }
}
