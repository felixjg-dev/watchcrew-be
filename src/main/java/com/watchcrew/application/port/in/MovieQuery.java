package com.watchcrew.application.port.in;

import com.watchcrew.domain.model.Genre;
import com.watchcrew.domain.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieQuery {
  List<Movie> getLatestMoviesByPreferences(List<Genre> preferences, int page, int size);
  Optional<Movie> getMovieById(String movieId);
  Optional<Movie> getMovieByExternalId(String externalId);
  List<Movie> searchMovies(String query, int page, int size);
  List<Movie> getRecommendedMoviesBasedOnGenres(List<Genre> genres, int page, int size);
  List<Movie> getSimilarMovies(String movieId, int page, int size);
  Movie saveOrUpdateMovie(Movie movie);
}
