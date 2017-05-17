package com.helix.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass public class Crew extends RealmObject {

  @PrimaryKey @SerializedName("credit_id") @Expose public String creditId;
  @SerializedName("department") @Expose public String department;
  @SerializedName("id") @Expose public Integer id;
  @SerializedName("job") @Expose public String job;
  @SerializedName("name") @Expose public String name;
  @SerializedName("profile_path") @Expose public String profilePath;
  public static final String SIZE_PROFILE = "http://image.tmdb.org/t/p/w185/";

  public String getCreditId() {
    return creditId;
  }

  public void setCreditId(String creditId) {
    this.creditId = creditId;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getJob() {
    return job;
  }

  public void setJob(String job) {
    this.job = job;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getProfilePath() {
    return profilePath;
  }

  public void setProfilePath(String profilePath) {
    this.profilePath = profilePath;
  }

  public String getProfileUrl() {
    return SIZE_PROFILE + this.profilePath;
  }
}
