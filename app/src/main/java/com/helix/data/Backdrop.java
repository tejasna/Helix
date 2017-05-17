package com.helix.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Backdrop {

  @SerializedName("aspect_ratio") @Expose public Float aspectRatio;
  @SerializedName("file_path") @Expose public String filePath;
  @SerializedName("height") @Expose public Integer height;
  @SerializedName("iso_639_1") @Expose public Object iso6391;
  @SerializedName("vote_average") @Expose public Float voteAverage;
  @SerializedName("vote_count") @Expose public Integer voteCount;
  @SerializedName("width") @Expose public Integer width;

  public static final String SIZE_BACKDROP = "http://image.tmdb.org/t/p/w500/";

  public Float getAspectRatio() {
    return aspectRatio;
  }

  public void setAspectRatio(Float aspectRatio) {
    this.aspectRatio = aspectRatio;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public Object getIso6391() {
    return iso6391;
  }

  public void setIso6391(Object iso6391) {
    this.iso6391 = iso6391;
  }

  public Float getVoteAverage() {
    return voteAverage;
  }

  public void setVoteAverage(Float voteAverage) {
    this.voteAverage = voteAverage;
  }

  public Integer getVoteCount() {
    return voteCount;
  }

  public void setVoteCount(Integer voteCount) {
    this.voteCount = voteCount;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public String getBackDropUrl() {
    return SIZE_BACKDROP + this.filePath;
  }
}
