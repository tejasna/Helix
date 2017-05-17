package com.helix.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Images {

  @SerializedName("id") @Expose public Integer id;
  @SerializedName("backdrops") @Expose public List<Backdrop> backdrops = null;
  @SerializedName("posters") @Expose public List<Poster> posters = null;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<Backdrop> getBackdrops() {
    return backdrops;
  }

  public void setBackdrops(List<Backdrop> backdrops) {
    this.backdrops = backdrops;
  }

  public List<Poster> getPosters() {
    return posters;
  }

  public void setPosters(List<Poster> posters) {
    this.posters = posters;
  }
}
