package com.neopi.gankio.model;

import okhttp3.MediaType;

/**
 * Created by NeoPi on 2017/03/04.
 */

public class DownLoadInfo {

  private String fileName ;
  private String filePath ;
  private MediaType type ;
  private long total;
  private long loaded;

  @Override public String toString() {
    return "DownLoadInfo{"
        + "fileName='"
        + fileName
        + '\''
        + ", filePath='"
        + filePath
        + '\''
        + ", type="
        + type
        + ", total="
        + total
        + ", loaded="
        + loaded
        + '}';
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public MediaType getType() {
    return type;
  }

  public void setType(MediaType type) {
    this.type = type;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public long getLoaded() {
    return loaded;
  }

  public void setLoaded(long loaded) {
    this.loaded = loaded;
  }
}
