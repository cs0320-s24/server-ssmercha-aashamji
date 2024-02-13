package edu.brown.cs.student.main.server.csv;

import spark.Request;
import spark.Response;
import spark.Route;

/*
This class sends back the entire CSV file's contents as a Json 2-dimensional array
* */
public class viewHandler implements Route {
  @Override
  public Object handle(Request request, Response response) throws Exception {
    return null;
  }

  /*
  This class sends back the broadband data from the ACS
  * */
  public static class broadbandHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      return null;
    }
  }
}
