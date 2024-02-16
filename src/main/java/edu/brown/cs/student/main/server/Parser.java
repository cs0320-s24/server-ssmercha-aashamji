package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.server.csv.CreatorFromRow;
import edu.brown.cs.student.main.server.csv.FactoryFailureException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Parser Class
 *
 * <p>This class is intended to parse a Reader object into a List<T> The type of object the list
 * contains is dependent on the implementation of the CreatorFromRow interface parameter
 *
 * <p>*
 */
public class Parser<T> {

  public List<T> parsedData = new ArrayList<T>();
  public HashMap<String, Integer> headerToIndex = new HashMap<>();
  public String error = "no error";

  /**
   * This is the Parser constructor
   *
   * @param r is the Reader object that the data will be obtained from
   * @param c is the CreatorFromRow implementation that dictates what kind of object will be created
   *     from each row
   * @param headers represents whether the first line of data represents headers (0 indicates no, 1
   *     indicates yes)
   */
  public Parser(Reader r, CreatorFromRow<T> c, int headers)
      throws IOException, FactoryFailureException {
    String line;
    int rowSize = 0;
    Pattern regexSplitCSVRow = Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

    BufferedReader b = new BufferedReader(r); // bufferedreader to wrap r
    if (headers == 0) { // if there are headers
      line = b.readLine();
      String[] result = regexSplitCSVRow.split(line);
      rowSize = result.length; // set the length of a row to the number of headers

      for (int i = 0; i < result.length; i++) {
        this.headerToIndex.put(result[i], i); // populate hashmap
      }
    }
    while ((line = b.readLine()) != null) { // continue reading
      String[] result = regexSplitCSVRow.split(line);
      if ((rowSize != 0) && rowSize != result.length) {
        error = "error_data_format";
      } else {
        rowSize = result.length;
        parsedData.add(c.create(List.of(result)));
      } // add created object to parsedData list
    }
  }
}
