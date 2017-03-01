package com.neopi.gankio.api;

import com.google.gson.Gson;
import com.neopi.gankio.model.GankMeiziData;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Created by neopi on 17-3-1.
 */

public class MeiziApi {


  // http://gank.io/api/data/数据类型/请求个数/第几页

  private static final String BASE_MEIZI_URL = "http://gank.io/api/data/福利/%d/%d";
  public static Observable<GankMeiziData> getMeitu(int offset,int page){

    return GankApi.rxGet(buildUrl(offset,page))
        .map(new Function<ResponseBody, GankMeiziData>() {
          @Override public GankMeiziData apply(@NonNull ResponseBody responseBody)
              throws Exception {

            Gson gson = new Gson();
            return gson.fromJson(responseBody.string(),GankMeiziData.class);
          }
        });
  }


  private static String buildUrl (int offset,int page){
    return String.format(BASE_MEIZI_URL,offset,page);
  }
}
