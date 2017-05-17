package com.helix.data;

import java.util.List;

public class UpcomingGenreZip {

  Upcoming upcoming;
  List<Genre> genres;

  public UpcomingGenreZip(Upcoming upcoming, List<Genre> genres) {
    this.upcoming = upcoming;
    this.genres = genres;
  }

  public Upcoming getUpcoming() {
    return upcoming;
  }

  public void setUpcoming(Upcoming upcoming) {
    this.upcoming = upcoming;
  }

  public List<Genre> getGenres() {
    return genres;
  }

  public void setGenres(List<Genre> genres) {
    this.genres = genres;
  }
}
