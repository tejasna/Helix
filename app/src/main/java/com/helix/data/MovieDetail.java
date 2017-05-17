package com.helix.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import java.text.DecimalFormat;

@RealmClass public class MovieDetail extends RealmObject {

  @PrimaryKey @SerializedName("id") @Expose public int id;
  @SerializedName("adult") @Expose public Boolean adult;
  @SerializedName("backdrop_path") @Expose public String backdropPath;
  @SerializedName("belongs_to_collection") @Expose public BelongsToCollection belongsToCollection;
  @SerializedName("budget") @Expose public Integer budget;
  @SerializedName("genres") @Expose public RealmList<Genre> genres;
  @SerializedName("homepage") @Expose public String homepage;
  @SerializedName("imdb_id") @Expose public String imdbId;
  @SerializedName("original_language") @Expose public String originalLanguage;
  @SerializedName("original_title") @Expose public String originalTitle;
  @SerializedName("overview") @Expose public String overview;
  @SerializedName("popularity") @Expose public Float popularity;
  @SerializedName("poster_path") @Expose public String posterPath;
  @SerializedName("production_companies") @Expose public RealmList<ProductionCompany>
      productionCompanies;
  @SerializedName("production_countries") @Expose public RealmList<ProductionCountry>
      productionCountries = null;
  @SerializedName("release_date") @Expose public String releaseDate;
  @SerializedName("revenue") @Expose public int revenue;
  @SerializedName("runtime") @Expose public int runtime;
  @SerializedName("spoken_languages") @Expose public RealmList<SpokenLanguage> spokenLanguages;
  @SerializedName("status") @Expose public String status;
  @SerializedName("tagline") @Expose public String tagline;
  @SerializedName("title") @Expose public String title;
  @SerializedName("video") @Expose public boolean video;
  @SerializedName("vote_average") @Expose public Float voteAverage;
  @SerializedName("vote_count") @Expose public int voteCount;

  public int getVoteCount() {
    return voteCount;
  }

  public void setVoteCount(int voteCount) {
    this.voteCount = voteCount;
  }

  public Boolean getAdult() {
    return adult;
  }

  public void setAdult(Boolean adult) {
    this.adult = adult;
  }

  public String getBackdropPath() {
    return backdropPath;
  }

  public void setBackdropPath(String backdropPath) {
    this.backdropPath = backdropPath;
  }

  public BelongsToCollection getBelongsToCollection() {
    return belongsToCollection;
  }

  public void setBelongsToCollection(BelongsToCollection belongsToCollection) {
    this.belongsToCollection = belongsToCollection;
  }

  public Integer getBudget() {
    return budget;
  }

  public void setBudget(Integer budget) {
    this.budget = budget;
  }

  public RealmList<Genre> getGenres() {
    return genres;
  }

  public void setGenres(RealmList<Genre> genres) {
    this.genres = genres;
  }

  public String getHomepage() {
    return homepage;
  }

  public void setHomepage(String homepage) {
    this.homepage = homepage;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getImdbId() {
    return imdbId;
  }

  public void setImdbId(String imdbId) {
    this.imdbId = imdbId;
  }

  public String getOriginalLanguage() {
    return originalLanguage;
  }

  public void setOriginalLanguage(String originalLanguage) {
    this.originalLanguage = originalLanguage;
  }

  public String getOriginalTitle() {
    return originalTitle;
  }

  public void setOriginalTitle(String originalTitle) {
    this.originalTitle = originalTitle;
  }

  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public Float getPopularity() {
    return popularity;
  }

  public void setPopularity(Float popularity) {
    this.popularity = popularity;
  }

  public String getPosterPath() {
    return posterPath;
  }

  public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
  }

  public RealmList<ProductionCompany> getProductionCompanies() {
    return productionCompanies;
  }

  public void setProductionCompanies(RealmList<ProductionCompany> productionCompanies) {
    this.productionCompanies = productionCompanies;
  }

  public RealmList<ProductionCountry> getProductionCountries() {
    return productionCountries;
  }

  public void setProductionCountries(RealmList<ProductionCountry> productionCountries) {
    this.productionCountries = productionCountries;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  public int getRevenue() {
    return revenue;
  }

  public String getFormatedRevenue() {
    DecimalFormat formatter = new DecimalFormat("#,###");
    return formatter.format(this.revenue) + " USD";
  }

  public void setRevenue(int revenue) {
    this.revenue = revenue;
  }

  public int getRuntime() {
    return runtime;
  }

  public void setRuntime(int runtime) {
    this.runtime = runtime;
  }

  public RealmList<SpokenLanguage> getSpokenLanguages() {
    return spokenLanguages;
  }

  public void setSpokenLanguages(RealmList<SpokenLanguage> spokenLanguages) {
    this.spokenLanguages = spokenLanguages;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTagline() {
    return tagline;
  }

  public void setTagline(String tagline) {
    this.tagline = tagline;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isVideo() {
    return video;
  }

  public void setVideo(boolean video) {
    this.video = video;
  }

  public Float getVoteAverage() {
    return voteAverage;
  }

  public void setVoteAverage(Float voteAverage) {
    this.voteAverage = voteAverage;
  }
}
