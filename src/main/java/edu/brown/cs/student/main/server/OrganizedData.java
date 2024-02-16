package edu.brown.cs.student.main.server;

import java.util.ArrayList;
import java.util.List;

public class OrganizedData {
  public List results;
  public Parser myParser;
  boolean loaded;

  public OrganizedData() {
    results = new ArrayList<>();
  }

  public void updateList(List given) {
    this.results = List.copyOf(given);
  }

  public void parserRef(Parser p) {
    myParser = p;
  }

  public void updateState() {
    loaded = true;
  }

  public boolean isLoaded() {
    if (loaded) {
      return true;
    }
    return false;
  }
}
