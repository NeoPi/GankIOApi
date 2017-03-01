package com.neopi.gankio;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;

/**
 * Created by neopi on 17-2-28.
 */

public class SplashActivity extends BaseActivity {

  private static final String TAG = "SplashActivity";

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_layout);

    Observable.just(0)
        .delay(2, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Integer>() {
          @Override public void onSubscribe(Disposable d) {
            Log.e(TAG,"onSubscribe");
          }

          @Override public void onNext(Integer s) {
            Log.e(TAG,"onNext");
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
          }

          @Override public void onError(Throwable e) {
            Log.e(TAG,"onError");
          }

          @Override public void onComplete() {
            Log.e(TAG,"onComplete");
            finish();
          }
        });

  }

}
