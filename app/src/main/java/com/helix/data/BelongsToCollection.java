package com.helix.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass public class BelongsToCollection extends RealmObject {

  @SerializedName("id") @Expose public int id;
  @SerializedName("name") @Expose public String name;
  @SerializedName("poster_path") @Expose public String posterPath;
  @SerializedName("backdrop_path") @Expose public String backdropPath;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPosterPath() {
    return posterPath;
  }

  public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
  }

  public String getBackdropPath() {
    return backdropPath;
  }

  public void setBackdropPath(String backdropPath) {
    this.backdropPath = backdropPath;
  }
}
