package com.helix.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass public class Dates extends RealmObject {

  @SerializedName("maximum") @Expose public String maximum;
  @SerializedName("minimum") @Expose public String minimum;

  public String getMaximum() {
    return maximum;
  }

  public void setMaximum(String maximum) {
    this.maximum = maximum;
  }

  public String getMinimum() {
    return minimum;
  }

  public void setMinimum(String minimum) {
    this.minimum = minimum;
  }
}
