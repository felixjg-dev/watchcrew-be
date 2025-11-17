package com.watchcrew.domain.model;

import java.util.List;

public class User {
  String id;
  String username;
  String password;
  List<String> friendsIds;
  List<Genre> preferences;
  List<String> myMovies;
  List<MovieList> customLists;
}