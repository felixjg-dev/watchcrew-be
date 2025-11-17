package com.watchcrew.domain.model;

import java.time.LocalDate;
import java.util.List;

class Movie {
  String externalId;
  String title;
  List<Genre> genres;
  double rating;
  LocalDate releaseDate;
  String overview;
}
