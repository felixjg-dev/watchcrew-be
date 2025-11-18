package com.watchcrew.application.service;

import com.watchcrew.application.port.in.MovieQuery;
import com.watchcrew.application.port.out.ExternalMovieApiPort;
import com.watchcrew.application.port.out.MovieRepository;
import com.watchcrew.domain.model.Genre;
import com.watchcrew.domain.model.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService implements MovieQuery {
  
  private final MovieRepository movieRepository;
  private final ExternalMovieApiPort externalMovieApiPort;
  
  @Override
  public List<Movie> getLatestMoviesByPreferences(List<Genre> preferences, int page, int size) {
    return externalMovieApiPort.fetchLatestMoviesByGenres(preferences, page);
  }
  
  @Override
  public Optional<Movie> getMovieById(String movieId) {
    return movieRepository.findById(movieId);
  }
  
  @Override
  public Optional<Movie> getMovieByExternalId(String externalId) {
    Optional<Movie> existingMovie = movieRepository.findByExternalId(externalId);
    
    if (existingMovie.isPresent()) {
      return existingMovie;
    }
    
    // Fetch from external API if not in database
    Movie movie = externalMovieApiPort.fetchMovieByExternalId(externalId);
    if (movie != null) {
      movie = movieRepository.save(movie);
      return Optional.of(movie);
    }
    
    return Optional.empty();
  }
  
  @Override
  public List<Movie> searchMovies(String query, int page, int size) {
    return externalMovieApiPort.searchMovies(query, page);
  }
  
  @Override
  public List<Movie> getRecommendedMoviesBasedOnGenres(List<Genre> genres, int page, int size) {
    return externalMovieApiPort.fetchLatestMoviesByGenres(genres, page);
  }
  
  @Override
  public List<Movie> getSimilarMovies(String movieId, int page, int size) {
    Movie movie = movieRepository.findById(movieId)
        .orElseThrow(() -> new IllegalArgumentException("Movie not found"));
    
    return externalMovieApiPort.fetchSimilarMovies(movie.getExternalId(), page);
  }
  
  @Override
  public Movie saveOrUpdateMovie(Movie movie) {
    return movieRepository.save(movie);
  }
}
