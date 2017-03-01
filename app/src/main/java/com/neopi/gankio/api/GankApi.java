package com.neopi.gankio.api;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by neopi on 17-3-1.
 */

public class GankApi {


  private static OkHttpClient mOkHttpClient = null;

  public static void initOkHttpClient(OkHttpClient build) {
    mOkHttpClient = build ;
  }

  public static Observable<ResponseBody> rxGet(String url){
    return  Observable.just(url)
        .map(new Function<String, ResponseBody>() {
          @Override public ResponseBody apply(@NonNull String s) throws Exception {

            Request request = new Request.Builder()
                .method("GET",null)
                .url(url)
                .build();

            Call call = mOkHttpClient.newCall(request);
            Response execute = call.execute();
            return execute.body();
          }
        })
        .subscribeOn(Schedulers.newThread());
  }


}
