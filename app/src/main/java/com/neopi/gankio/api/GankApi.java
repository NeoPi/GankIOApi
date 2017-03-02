package com.neopi.gankio.api;

import android.util.Log;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * Created by neopi on 17-3-1.
 */

public class GankApi {

  private static OkHttpClient mOkHttpClient = null;

  public static void initOkHttpClient(OkHttpClient build) {
    mOkHttpClient = build;
  }

  public static Observable<ResponseBody> rxGet(String url) {
    return Observable.just(url).map(new Function<String, ResponseBody>() {
      @Override public ResponseBody apply(@NonNull String s) throws Exception {

        Request request = new Request.Builder().method("GET", null).url(url).build();

        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute.body();
      }
    }).subscribeOn(Schedulers.newThread());
  }

  public static Observable<ResponseBody> rxDownLoad(String url) {

    return Observable.just(url).map(new Function<String, ResponseBody>() {
      @Override public ResponseBody apply(@NonNull String s) throws Exception {
        Request request = new Request.Builder().method("GET", null).url(url).build();

        Call call = mOkHttpClient.newCall(request);
        return call.execute().body();
      }
    }).subscribeOn(Schedulers.newThread());
  }

  public static Flowable<Integer> rxDownLoadWithProgress(String url) {
    String fileName = System.currentTimeMillis() + "" + url.substring(url.lastIndexOf("."));

    return Flowable.create(new FlowableOnSubscribe<Integer>() {
      @Override public void subscribe(FlowableEmitter<Integer> emitter) {
        try {
          Request request = new Request.Builder().method("GET", null).url(url).build();
          Call call = mOkHttpClient.newCall(request);
          Response response = call.execute();
          if (response.isSuccessful()) {
            FileOutputStream fos = null;
            File saveFile = MeiziApi.getSavePath(fileName);
            try {
              BufferedSource inputStream = response.body().source();
              long totalLen = response.body().contentLength();
              long readed = 0;
              fos = new FileOutputStream(saveFile);
              byte[] reads = new byte[1024];
              int len = 0;
              float precent ;
              while ((len = inputStream.read(reads)) != -1) {
                fos.write(reads, 0, len);
                readed += len;
                precent = readed * 1f / totalLen ;
                emitter.onNext( (int)(precent * 100) );
                //Log.e("1111",precent+",,,"+(int)(precent * 100));
              }
              emitter.onComplete();
              fos.flush();
              inputStream.close();
            } catch (FileNotFoundException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            } finally {
              if (fos != null) {
                try {
                  fos.close();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
              response.close();
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
          emitter.onError(e);
        }
      }
    }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io());

  }
}
