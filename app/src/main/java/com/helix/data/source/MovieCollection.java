package com.helix.data.source;

import com.helix.data.MovieDetail;
import com.helix.data.Upcoming;
import java.util.List;

public class MovieCollection {

  List<Upcoming> upcoming;
  List<MovieDetail> movieDetails;

  public MovieCollection(List<Upcoming> upcoming, List<MovieDetail> movieDetails) {
    this.upcoming = upcoming;
    this.movieDetails = movieDetails;
  }

  public List<Upcoming> getUpcoming() {
    return upcoming;
  }

  public void setUpcoming(List<Upcoming> upcoming) {
    this.upcoming = upcoming;
  }

  public List<MovieDetail> getMovieDetails() {
    return movieDetails;
  }

  public void setMovieDetails(List<MovieDetail> movieDetails) {
    this.movieDetails = movieDetails;
  }
}
