package com.helix.data;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass public class RealmString extends RealmObject {
  private String value;

  public RealmString() {
  }

  public RealmString(String val) {
    this.value = val;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
