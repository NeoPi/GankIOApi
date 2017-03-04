package com.neopi.gankio.api;

import android.os.Environment;
import com.google.gson.Gson;
import com.neopi.gankio.model.GankMeiziData;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.ResponseBody;

/**
 * Created by neopi on 17-3-1.
 */

public class MeiziApi {


  // http://gank.io/api/data/数据类型/请求个数/第几页

  private static final String BASE_MEIZI_URL = "http://gank.io/api/data/福利/%d/%d";

  /**
   *
   * @param offset 请求个数
   * @param page 页数
   * @return
   */
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


  public static Observable<File> rxDownLoad(String url) {
    String fileName = System.currentTimeMillis()+""+url.substring( url.lastIndexOf("."));

    return GankApi.rxDownLoad(url)
        .map(new Function<ResponseBody, File>() {
          @Override public File apply(@NonNull ResponseBody responseBody){
            FileOutputStream fos = null;
            File saveFile = getSavePath(fileName);
            try {
              InputStream inputStream = responseBody.byteStream();
              fos = new FileOutputStream(saveFile);
              byte[] reads = new byte[1024];
              int len = 0;
              while ( (len = inputStream.read(reads)) != -1){
                fos.write(reads,0,len);
              }
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
              responseBody.close();
            }
            return saveFile;
          }
        });
  }

  public static File getSavePath(String name){

    File externalStorageDirectory = Environment.getExternalStorageDirectory();
    String savePath = externalStorageDirectory.getAbsolutePath()+File.separator+"gankio" ;
    File savaDir = new File(savePath);
    if (!savaDir.exists()) {
      savaDir.mkdir();
    }

    File saveFile = new File(savaDir.getAbsoluteFile()+File.separator+name);
    if (!saveFile.exists()){
      try {
        saveFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return saveFile;
  }


}
