package com.watchcrew.adapter.out.external;

import com.watchcrew.domain.model.Genre;
import com.watchcrew.domain.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TmdbMovieApiAdapterTest {
  
  @Mock
  private RestTemplate restTemplate;
  
  @InjectMocks
  private TmdbMovieApiAdapter tmdbMovieApiAdapter;
  
  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(tmdbMovieApiAdapter, "apiKey", "test-api-key");
    ReflectionTestUtils.setField(tmdbMovieApiAdapter, "baseUrl", "https://api.themoviedb.org/3");
  }
  
  @Test
  void fetchLatestMoviesByGenres_WithValidGenres_ShouldCallApi() {
    // Arrange
    List<Genre> genres = List.of(Genre.ACTION, Genre.COMEDY);
    when(restTemplate.getForObject(anyString(), any())).thenReturn(null);
    
    // Act
    List<Movie> movies = tmdbMovieApiAdapter.fetchLatestMoviesByGenres(genres, 0);
    
    // Assert
    assertNotNull(movies);
    verify(restTemplate).getForObject(anyString(), any());
  }
  
  @Test
  void fetchLatestMoviesByGenres_WithoutApiKey_ShouldReturnEmptyList() {
    // Arrange
    ReflectionTestUtils.setField(tmdbMovieApiAdapter, "apiKey", "");
    List<Genre> genres = List.of(Genre.ACTION);
    
    // Act
    List<Movie> movies = tmdbMovieApiAdapter.fetchLatestMoviesByGenres(genres, 0);
    
    // Assert
    assertNotNull(movies);
    assertTrue(movies.isEmpty());
    verify(restTemplate, never()).getForObject(anyString(), any());
  }
  
  @Test
  void fetchMovieByExternalId_WithValidId_ShouldCallApi() {
    // Arrange
    when(restTemplate.getForObject(anyString(), any())).thenReturn(null);
    
    // Act
    Movie movie = tmdbMovieApiAdapter.fetchMovieByExternalId("12345");
    
    // Assert
    verify(restTemplate).getForObject(anyString(), any());
  }
  
  @Test
  void fetchMovieByExternalId_WithoutApiKey_ShouldReturnNull() {
    // Arrange
    ReflectionTestUtils.setField(tmdbMovieApiAdapter, "apiKey", "");
    
    // Act
    Movie movie = tmdbMovieApiAdapter.fetchMovieByExternalId("12345");
    
    // Assert
    assertNull(movie);
    verify(restTemplate, never()).getForObject(anyString(), any());
  }
  
  @Test
  void searchMovies_WithValidQuery_ShouldCallApi() {
    // Arrange
    when(restTemplate.getForObject(anyString(), any())).thenReturn(null);
    
    // Act
    List<Movie> movies = tmdbMovieApiAdapter.searchMovies("Inception", 0);
    
    // Assert
    assertNotNull(movies);
    verify(restTemplate).getForObject(anyString(), any());
  }
  
  // @Disabled("Argument matcher issue with method mocking")
  @Test
  void fetchSimilarMovies_WithValidId_ShouldCallApi() {
    // Arrange
    when(restTemplate.getForObject(anyString(), any())).thenReturn(null);
    
    // Act
    List<Movie> movies = tmdbMovieApiAdapter.fetchSimilarMovies("12345", 0);
    
    // Assert
    assertNotNull(movies);
    verify(restTemplate, times(1)).getForObject(anyString(), any());
  }
  
  @Test
  void fetchLatestMoviesByGenres_WithException_ShouldReturnEmptyList() {
    // Arrange
    List<Genre> genres = List.of(Genre.ACTION);
    when(restTemplate.getForObject(anyString(), any()))
        .thenThrow(new RuntimeException("API error"));
    
    // Act
    List<Movie> movies = tmdbMovieApiAdapter.fetchLatestMoviesByGenres(genres, 0);
    
    // Assert
    assertNotNull(movies);
    assertTrue(movies.isEmpty());
  }
}
