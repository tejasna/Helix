package com.helix.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import java.util.List;

@RealmClass public class Upcoming extends RealmObject {

  @SerializedName("page") @Expose public Integer page;
  @SerializedName("results") @Expose public List<Movie> results = null;
  @SerializedName("dates") @Expose public Dates dates;
  @SerializedName("total_pages") @Expose public Integer totalPages;
  @SerializedName("total_results") @Expose public Integer totalResults;

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public List<Movie> getResults() {
    return results;
  }

  public void setResults(List<Movie> results) {
    this.results = results;
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
