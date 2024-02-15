package edu.brown.cs.student.main.server.csv;

import edu.brown.cs.student.main.server.OrganizedData;
import edu.brown.cs.student.main.server.Parser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

/*
This class will load a specific csv file given a path
* */
public class loadHandler implements Route {

  private static OrganizedData myData;

  public loadHandler(OrganizedData o) {
    myData = o;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Set<String> params = request.queryParams();
    String filepath = request.queryParams("filepath");
    Map<String, Object> responseMap = new HashMap<>();

    try {
      System.out.println(filepath);
      FileReader f = new FileReader(filepath); // TODO: fix this

      Strat s = new Strat();
      Parser<List<String>> p = new Parser<>(f, s, 1);

      myData.updateList(p.parsedData);
      myData.parserRef(p);
      myData.updateState();
      responseMap.put("result", "success");
    } catch (FileNotFoundException | FactoryFailureException e) {
      System.err.println("Sorry, you cannot load that file.");
      responseMap.put("result", "failed");
    }

    return responseMap;
  }
}

/*
* try {
      System.out.println(filepath);
      FileReader f = new FileReader("data/star.csv"); // TODO: fix this

      Strat s = new Strat();
      Parser p = new Parser<>(f, s, 1);
      responseMap.put("result", "success");

      myData.updateList(p.parsedData);
      myData.parserRef(p);
    } catch (FileNotFoundException | FactoryFailureException e) {
      System.err.println("Sorry, you cannot load that file.");
      responseMap.put("result", "failed");
    }
* */
