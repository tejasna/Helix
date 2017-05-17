package com.helix.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass public class Cast extends RealmObject {

  @PrimaryKey @SerializedName("cast_id") @Expose public Integer castId;
  @SerializedName("character") @Expose public String character;
  @SerializedName("credit_id") @Expose public String creditId;
  @SerializedName("id") @Expose public Integer id;
  @SerializedName("name") @Expose public String name;
  @SerializedName("order") @Expose public Integer order;
  @SerializedName("profile_path") @Expose public String profilePath;

  public static final String SIZE_PROFILE = "http://image.tmdb.org/t/p/w185/";

  public Integer getCastId() {
    return castId;
  }

  public void setCastId(Integer castId) {
    this.castId = castId;
  }

  public String getCharacter() {
    return character;
  }

  public void setCharacter(String character) {
    this.character = character;
  }

  public String getCreditId() {
    return creditId;
  }

  public void setCreditId(String creditId) {
    this.creditId = creditId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }

  public String getProfilePath() {
    return profilePath;
  }

  public String getProfileUrl() {
    return SIZE_PROFILE + this.profilePath;
  }

  public void setProfilePath(String profilePath) {
    this.profilePath = profilePath;
  }
}
