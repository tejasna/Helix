package com.helix.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass public class Credits extends RealmObject {

  @PrimaryKey @SerializedName("id") @Expose public Integer id;
  @SerializedName("cast") @Expose public RealmList<Cast> cast = null;
  @SerializedName("crew") @Expose public RealmList<Crew> crew = null;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public RealmList<Cast> getCast() {
    return cast;
  }

  public void setCast(RealmList<Cast> cast) {
    this.cast = cast;
  }

  public RealmList<Crew> getCrew() {
    return crew;
  }

  public void setCrew(RealmList<Crew> crew) {
    this.crew = crew;
  }

  public Crew getDirector() {
    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    Credits credits = realm.where(Credits.class).equalTo("id", this.id).findFirst();
    Crew director = credits.getCrew().where().equalTo("job", "Director").findFirst();
    realm.commitTransaction();
    realm.close();
    return director;
  }
}
