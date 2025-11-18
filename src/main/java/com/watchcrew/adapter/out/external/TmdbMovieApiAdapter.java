package com.watchcrew.adapter.out.external;

import com.watchcrew.application.port.out.ExternalMovieApiPort;
import com.watchcrew.domain.model.Genre;
import com.watchcrew.domain.model.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TmdbMovieApiAdapter implements ExternalMovieApiPort {
  
  private static final String API_KEY_PARAM = "api_key";
  private static final String API_KEY_NOT_CONFIGURED = "TMDB API key not configured. Returning empty list.";
  private static final Map<Genre, Integer> GENRE_MAP = createGenreMap();
  
  private final RestTemplate restTemplate;
  
  @Value("${tmdb.api.key:}")
  private String apiKey;
  
  @Value("${tmdb.api.base-url:https://api.themoviedb.org/3}")
  private String baseUrl;
  
  @Override
  public List<Movie> fetchLatestMoviesByGenres(List<Genre> genres, int page) {
    if (apiKey == null || apiKey.isEmpty()) {
      log.warn(API_KEY_NOT_CONFIGURED);
      return Collections.emptyList();
    }
    
    String genreIds = (genres != null && !genres.isEmpty())
        ? genres.stream()
            .map(GENRE_MAP::get)
            .filter(Objects::nonNull)
            .map(String::valueOf)
            .collect(Collectors.joining(","))
        : "";
    
    String url = UriComponentsBuilder.fromUriString(baseUrl + "/discover/movie")
        .queryParam(API_KEY_PARAM, apiKey)
        .queryParam("sort_by", "release_date.desc")
        .queryParam("with_genres", genreIds)
        .queryParam("page", page + 1)
        .build()
        .toUriString();
    
    try {
      TmdbResponse response = restTemplate.getForObject(url, TmdbResponse.class);
      return response != null && response.results != null
          ? response.results.stream().map(this::mapToMovie).toList()
          : Collections.emptyList();
    } catch (Exception e) {
      log.error("Error fetching movies from TMDB", e);
      return Collections.emptyList();
    }
  }
  
  @Override
  public Movie fetchMovieByExternalId(String externalId) {
    if (apiKey == null || apiKey.isEmpty()) {
      log.warn("TMDB API key not configured. Returning null.");
      return null;
    }
    
    String url = UriComponentsBuilder.fromUriString(baseUrl + "/movie/" + externalId)
        .queryParam(API_KEY_PARAM, apiKey)
        .build()
        .toUriString();
    
    try {
      TmdbMovieDetail response = restTemplate.getForObject(url, TmdbMovieDetail.class);
      return response != null ? mapDetailToMovie(response) : null;
    } catch (Exception e) {
      log.error("Error fetching movie by ID from TMDB", e);
      return null;
    }
  }
  
  @Override
  public List<Movie> searchMovies(String query, int page) {
    if (apiKey == null || apiKey.isEmpty()) {
      log.warn(API_KEY_NOT_CONFIGURED);
      return Collections.emptyList();
    }
    
    String url = UriComponentsBuilder.fromUriString(baseUrl + "/search/movie")
        .queryParam(API_KEY_PARAM, apiKey)
        .queryParam("query", query)
        .queryParam("page", page + 1)
        .build()
        .toUriString();
    
    try {
      TmdbResponse response = restTemplate.getForObject(url, TmdbResponse.class);
      return response != null && response.results != null
          ? response.results.stream().map(this::mapToMovie).toList()
          : Collections.emptyList();
    } catch (Exception e) {
      log.error("Error searching movies from TMDB", e);
      return Collections.emptyList();
    }
  }
  
  @Override
  public List<Movie> fetchSimilarMovies(String externalId, int page) {
    if (apiKey == null || apiKey.isEmpty()) {
      log.warn(API_KEY_NOT_CONFIGURED);
      return Collections.emptyList();
    }
    
    String url = UriComponentsBuilder.fromUriString(baseUrl + "/movie/" + externalId + "/similar")
        .queryParam(API_KEY_PARAM, apiKey)
        .queryParam("page", page + 1)
        .build()
        .toUriString();
    
    try {
      TmdbResponse response = restTemplate.getForObject(url, TmdbResponse.class);
      return response != null && response.results != null
          ? response.results.stream().map(this::mapToMovie).toList()
          : Collections.emptyList();
    } catch (Exception e) {
      log.error("Error fetching similar movies from TMDB", e);
      return Collections.emptyList();
    }
  }
  
  private Movie mapToMovie(TmdbMovieResult result) {
    return Movie.builder()
        .externalId(String.valueOf(result.id))
        .title(result.title)
        .originalTitle(result.originalTitle)
        .genres(mapGenreIds(result.genreIds))
        .voteAverage(result.voteAverage)
        .voteCount(result.voteCount)
        .releaseDate(parseDate(result.releaseDate))
        .overview(result.overview)
        .posterPath(result.posterPath)
        .backdropPath(result.backdropPath)
        .originalLanguage(result.originalLanguage)
        .build();
  }
  
  private Movie mapDetailToMovie(TmdbMovieDetail detail) {
    return Movie.builder()
        .externalId(String.valueOf(detail.id))
        .title(detail.title)
        .originalTitle(detail.originalTitle)
        .genres(mapGenreObjects(detail.genres))
        .voteAverage(detail.voteAverage)
        .voteCount(detail.voteCount)
        .releaseDate(parseDate(detail.releaseDate))
        .overview(detail.overview)
        .posterPath(detail.posterPath)
        .backdropPath(detail.backdropPath)
        .runtime(detail.runtime)
        .originalLanguage(detail.originalLanguage)
        .build();
  }
  
  private List<Genre> mapGenreIds(List<Integer> genreIds) {
    if (genreIds == null) return new ArrayList<>();
    
    return genreIds.stream()
        .map(this::findGenreByTmdbId)
        .filter(Objects::nonNull)
        .toList();
  }
  
  private List<Genre> mapGenreObjects(List<TmdbGenre> genres) {
    if (genres == null) return new ArrayList<>();
    
    return genres.stream()
        .map(g -> findGenreByTmdbId(g.id))
        .filter(Objects::nonNull)
        .toList();
  }
  
  private Genre findGenreByTmdbId(Integer tmdbId) {
    return GENRE_MAP.entrySet().stream()
        .filter(entry -> entry.getValue().equals(tmdbId))
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(null);
  }
  
  private LocalDate parseDate(String dateString) {
    if (dateString == null || dateString.isEmpty()) {
      return null;
    }
    try {
      return LocalDate.parse(dateString);
    } catch (Exception e) {
      return null;
    }
  }
  
  private static Map<Genre, Integer> createGenreMap() {
    Map<Genre, Integer> map = new EnumMap<>(Genre.class);
    map.put(Genre.ACTION, 28);
    map.put(Genre.ADVENTURE, 12);
    map.put(Genre.ANIMATION, 16);
    map.put(Genre.COMEDY, 35);
    map.put(Genre.CRIME, 80);
    map.put(Genre.DOCUMENTARY, 99);
    map.put(Genre.DRAMA, 18);
    map.put(Genre.FAMILY, 10751);
    map.put(Genre.FANTASY, 14);
    map.put(Genre.HISTORY, 36);
    map.put(Genre.HORROR, 27);
    map.put(Genre.MUSIC, 10402);
    map.put(Genre.MYSTERY, 9648);
    map.put(Genre.ROMANCE, 10749);
    map.put(Genre.SCI_FI, 878);
    map.put(Genre.THRILLER, 53);
    map.put(Genre.WAR, 10752);
    map.put(Genre.WESTERN, 37);
    return map;
  }
  
  // Inner classes for TMDB response mapping
  // Public fields are intentional for JSON deserialization by RestTemplate
  @SuppressWarnings({"java:S1104", "java:S1068"})
  private static class TmdbResponse {
    public List<TmdbMovieResult> results;
  }
  
  @SuppressWarnings("java:S1104")
  private static class TmdbMovieResult {
    public Integer id;
    public String title;
    public String originalTitle;
    public List<Integer> genreIds;
    public Double voteAverage;
    public Integer voteCount;
    public String releaseDate;
    public String overview;
    public String posterPath;
    public String backdropPath;
    public String originalLanguage;
  }
  
  @SuppressWarnings("java:S1104")
  private static class TmdbMovieDetail {
    public Integer id;
    public String title;
    public String originalTitle;
    public List<TmdbGenre> genres;
    public Double voteAverage;
    public Integer voteCount;
    public String releaseDate;
    public String overview;
    public String posterPath;
    public String backdropPath;
    public Integer runtime;
    public String originalLanguage;
  }
  
  @SuppressWarnings({"java:S1104", "java:S1068"})
  private static class TmdbGenre {
    public Integer id;
    public String name;
  }
}
