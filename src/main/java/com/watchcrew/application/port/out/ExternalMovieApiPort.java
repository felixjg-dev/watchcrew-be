package com.watchcrew.application.port.out;

import com.watchcrew.domain.model.Genre;
import com.watchcrew.domain.model.Movie;

import java.util.List;

public interface ExternalMovieApiPort {
  List<Movie> fetchLatestMoviesByGenres(List<Genre> genres, int page);
  Movie fetchMovieByExternalId(String externalId);
  List<Movie> searchMovies(String query, int page);
  List<Movie> fetchSimilarMovies(String externalId, int page);
}
