package com.helix.data;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass public class RealmInt extends RealmObject {
  private int value;

  public RealmInt() {
  }

  public RealmInt(int val) {
    this.value = val;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
