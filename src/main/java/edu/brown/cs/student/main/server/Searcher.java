package edu.brown.cs.student.main.server;

import java.util.ArrayList;
import java.util.List;

/** This is the Searcher class It is intended to return the rows a given input is present in */
public class Searcher {
  public List<List<String>> searchResults = new ArrayList<>();
  private Parser<List<String>> myParser;

  /**
   * This is the Searcher constructor
   *
   * @param p represents the parser that parsed the file to be searched
   * @param path represents the filepath to the file containing the data to be searched
   */
  public Searcher(Parser<List<String>> p, String path) {
    this.myParser = p;
    if (!path.startsWith("data/")) { // if the file is in the wrong directory
      System.out.println(path);
      System.out.println(path.startsWith("data/"));
      p.error = "You do not have access to that data.";
      System.err.println("You do not have access to that data.");
      return;
    }
    if (!this.myParser.error.equals("no error")) { // if there was a malformed row
      System.err.println(myParser.error + " Incorrectly formatted rows of data have been omitted");
    }
    //    search(s, myParser);
  }

  /**
   * This is the basic search method intended to search the entire data file
   *
   * @param s represents the word to search for
   * @return a list of the rows containing s
   */
  public List<List<String>> search(String s) {

    for (int i = 0; i < myParser.parsedData.size(); i++) {
      if (myParser.parsedData.get(i).contains(s)) { // if the row has the word
        this.searchResults.add(myParser.parsedData.get(i)); // add to list
      }
    }
    return searchResults;
  }

  /**
   * This is the search method that uses a column's index to search in a specific column
   *
   * @param s represents the word to search for
   * @param colIndex the index of the column to search in
   * @return list of the rows containing s in column colIndex
   */
  public List<List<String>> search(String s, Integer colIndex) {
    int colCount = myParser.parsedData.get(0).size();
    if (colIndex >= colCount) { // if the column doesn't exist
      this.myParser.error = "That column doesn't exist";
      System.err.println("That column doesn't exist");
      return searchResults;
    }
    for (int i = 0; i < myParser.parsedData.size(); i++) {
      if (myParser.parsedData.get(i).get(colIndex).equals(s)) { // if the row has the word
        this.searchResults.add(myParser.parsedData.get(i)); // add to list
      }
    }
    return searchResults;
  }

  /**
   * This is the search method that uses header names
   *
   * @param s represents the word to search for
   * @param colHeader the index of the column to search in
   * @return a list of the rows containing s
   */
  public List<List<String>> search(String s, String colHeader) {
    if (!myParser.headerToIndex.containsKey(colHeader)) { // if the column doesn't exist
      myParser.error = "That column does not exist.";
      System.err.println("That column does not exist.");
      return searchResults;
    }
    return search(s, myParser.headerToIndex.get(colHeader));
  }
}
