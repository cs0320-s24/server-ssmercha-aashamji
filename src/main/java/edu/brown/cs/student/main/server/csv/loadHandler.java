package edu.brown.cs.student.main.server.csv;

import edu.brown.cs.student.main.server.Parser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/*
This class will load a specific csv file given a path
* */
public class loadHandler implements Route {

  private static Parser myParser;

  public loadHandler(Parser p) {
    myParser = p;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {

    String filepath = request.queryParams("filepath");
    Map<String, Object> responseMap = new HashMap<>();

    FileReader f = null;
    try {
      f = new FileReader("data/stardata.csv");
    } catch (FileNotFoundException e) {
      System.err.println("Sorry, that file doesn't exist.");
      responseMap.put("result", "failed");
    }

    Strat s = new Strat();

    try {
      myParser = new Parser<>(f, s, 1);
      responseMap.put("result", "success");
      //      responseMap.put("data", p.parsedData);
    } catch (IOException | FactoryFailureException e) {
      System.err.println("Sorry, that file cannot be parsed.");
      responseMap.put("result", "failed");
    }

    return responseMap;
  }
}
