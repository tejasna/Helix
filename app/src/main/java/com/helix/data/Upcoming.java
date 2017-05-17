package com.helix.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass public class Upcoming extends RealmObject {

  @PrimaryKey @SerializedName("page") @Expose public int page;
  @SerializedName("results") @Expose public RealmList<Movie> movies;
  @SerializedName("dates") @Expose public Dates dates;
  @SerializedName("total_pages") @Expose public Integer totalPages;
  @SerializedName("total_results") @Expose public Integer totalResults;

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public RealmList<Movie> getMovies() {
    return movies;
  }

  public void setMovies(RealmList<Movie> movies) {
    this.movies = movies;
  }

  public Dates getDates() {
    return dates;
  }

  public void setDates(Dates dates) {
    this.dates = dates;
  }

  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public Integer getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(Integer totalResults) {
    this.totalResults = totalResults;
  }
}
