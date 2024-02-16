package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.server.census.broadbandHandler;
import edu.brown.cs.student.main.server.csv.*;
import java.io.IOException;
import spark.Spark;

public class Server {

  public static void main(String[] args) throws IOException, FactoryFailureException {
    int port = 3232;
    Spark.port(port);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    Spark.get("/", (req, res) -> "welcome to the server.");

    OrganizedData o = new OrganizedData();

    Spark.get("loadcsv", new loadHandler(o));
    Spark.get("searchcsv", new searchHandler(o));
    Spark.get("viewcsv", new viewHandler(o));
    Spark.get("broadband", new broadbandHandler());

    Spark.init();
    Spark.awaitInitialization();

    //    System.out.println("Server started at http://localhost:" + port);
  }
}
