package edu.brown.cs.student.main.server.csv;

import edu.brown.cs.student.main.server.OrganizedData;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

/*
This class sends back the entire CSV file's contents as a Json 2-dimensional array
* */
public class viewHandler implements Route {

  private static OrganizedData myData;

  public viewHandler(OrganizedData o) {
    myData = o;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Set<String> params = request.queryParams();
    Map<String, Object> responseMap = new HashMap<>();
    ;
    try {
      responseMap.put("data", myData.results);
      responseMap.put("result", "success");
    } catch (NullPointerException e) {
      System.err.println("cannot access data");
      responseMap.put("result", "failed");
    }
    return responseMap;
  }
}
