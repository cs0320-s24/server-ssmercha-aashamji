package edu.brown.cs.student.main.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is our proxy class for shared state information between the handlers, it is passed in to
 * each handler in Server.java
 */
public class OrganizedData {
  public List results;
  public Parser myParser;
  public Map<String, Boolean> pathLoaded = new HashMap<>();

  /** This is our constructor */
  public OrganizedData() {
    results = new ArrayList<>();
  }

  /**
   * This updates results to a copy of a list
   *
   * @param given is the list to make a copy of
   */
  public void updateList(List given) {
    this.results = List.copyOf(given);
  }

  /**
   * This sets the parser field to a given parser
   *
   * @param p the parser to reference
   */
  public void parserRef(Parser p) {
    myParser = p;
  }

  /**
   * This updates a state in the pathLoaded map, meant to tore whether file has been loaded
   *
   * @param filepath is the path of the file to set the status of
   */
  public void updateState(String filepath) {
    pathLoaded.put(filepath, true);
  }

  /**
   * This returns a boolean representing whether a file has been loaded
   *
   * @param filepath is the file whose load status we're checking
   * @return
   */
  public boolean isLoaded(String filepath) {
    if (pathLoaded.containsKey(filepath)) {
      if (pathLoaded.get(filepath)) {
        return true;
      }
      return false;
    }
    return false;
  }
}
