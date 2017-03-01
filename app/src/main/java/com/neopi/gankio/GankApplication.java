package com.neopi.gankio;

import android.app.Application;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.neopi.gankio.api.GankApi;
import com.neopi.gankio.utils.DeviceUtils;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by neopi on 17-2-28.
 */

public class GankApplication extends Application {


  ImagePipelineConfig imagePipelineConfig ;
  @Override public void onCreate() {
    super.onCreate();

    DeviceUtils.init(getApplicationContext());
    initFresco();
    initOkHttp();

  }

  private void initOkHttp() {
    OkHttpClient.Builder build = new OkHttpClient.Builder();
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    build.addInterceptor(loggingInterceptor);
    build.readTimeout(10, TimeUnit.SECONDS);
    GankApi.initOkHttpClient(build.build());
  }

  private void initFresco() {
    DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this)
        .setBaseDirectoryPath(getExternalCacheDir())
        .build();

    ProgressiveJpegConfig pjpegConfig = new SimpleProgressiveJpegConfig();

    imagePipelineConfig = ImagePipelineConfig.newBuilder(this)
        .setProgressiveJpegConfig(pjpegConfig)
        .setMainDiskCacheConfig(diskCacheConfig)
        .build();
    Fresco.initialize(this, imagePipelineConfig);
  }


}
