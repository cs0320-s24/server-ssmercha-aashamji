package edu.brown.cs.student.main.server.csv;

import edu.brown.cs.student.main.server.OrganizedData;
import edu.brown.cs.student.main.server.Searcher;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/*
This class sends back rows matching the given search criteria.
* */
public class searchHandler implements Route {

  private static OrganizedData myData;

  public searchHandler(OrganizedData o) {
    myData = o;
  }

  // int headers, String identType, String colIdentifier, String word, String filepath
  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String, Object> responseMap = new HashMap<>();
    String filepath = request.queryParams("filepath");
    if (myData.isLoaded()) {
      Searcher look = new Searcher(myData.myParser, "data/stardata.csv");
      // TODO: multiple query parameters so we can actually search

      //
      String h = request.queryParams("headers");
      //      Integer headers = Integer.parseInt(h); //TODO: why does this work?
      responseMap.put("headers", h);

      String identType = request.queryParams("identType");
      responseMap.put("identType", identType);
      String colIdentifier = request.queryParams("colIdentifier");
      responseMap.put("colIdentifier", colIdentifier);
      String word = request.queryParams("word");
      responseMap.put("word", word);

      //      System.out.println(identType);
      //      System.out.println(identType.equals("index"));
      //      System.out.println("index".equals("index"));
      //      // search
      //      if (identType.equals("index")) {
      //        responseMap.put("Results", look.search(word, Integer.parseInt(colIdentifier)));
      //      } else if (identType.equals("N/A")) {
      responseMap.put("Results", look.search(word));
      //      } else if (identType.equals("header")) {
      //        responseMap.put("Results", look.search(word, colIdentifier));
      //      }
      //      //      else {
      //        responseMap.put("result", "failed");
      //      }
    } else {
      responseMap.put("result", "failed: cannot search file that has not been loaded");
    }

    return responseMap;
  }
}
