package com.helix.data.source.remote;

import com.helix.data.Credits;
import com.helix.data.Genres;
import com.helix.data.Images;
import com.helix.data.MovieDetail;
import com.helix.data.Upcoming;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {

  String API_KEY = "api_key";
  String PAGE = "page";

  @GET("/3/movie/upcoming") Observable<Upcoming> getUpcomingMovies(@Query(API_KEY) String apiKey,
      @Query(PAGE) int page);

  @GET("/3/movie/{id}") Observable<MovieDetail> getMovieDetail(@Path("id") int movieId,
      @Query(API_KEY) String apiKey);

  @GET("/3/genre/movie/list") Observable<Genres> getMovieGenres(@Query(API_KEY) String apiKey);

  @GET("/3/movie/{id}/images") Observable<Images> getMovieImages(@Path("id") int movieId,
      @Query(API_KEY) String apiKey);

  @GET("/3/movie/{id}/credits") Observable<Credits> getMovieCredits(@Path("id") int movieId,
      @Query(API_KEY) String apiKey);
}
