package com.neopi.gankio.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by neopi on 17-3-1.
 */

public class GankMeiziData {

  @SerializedName("error")
  @Expose
  private Boolean error;
  @SerializedName("results")
  @Expose
  private List<GankResult> results = null;

  public Boolean getError() {
    return error;
  }

  public void setError(Boolean error) {
    this.error = error;
  }

  public List<GankResult> getResults() {
    return results;
  }

  public void setResults(List<GankResult> results) {
    this.results = results;
  }

  @Override public String toString() {
    return "GankMeiziData{" +
        "error=" + error +
        ", results=" + results +
        '}';
  }
}
