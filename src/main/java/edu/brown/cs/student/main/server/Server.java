package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.server.census.broadbandHandler;
import edu.brown.cs.student.main.server.csv.*;
import java.io.IOException;
import spark.Spark;

public class Server {

  public static void main(String[] args) throws IOException, FactoryFailureException {
    /**
     * This file is responsible for starting up the web server and handling different user requests:
     * we spark the port to address 3232
     * we give a welcome message upon visiting the blank api
     * we initialise the relevant handlers for all api calls
     * we call init.
     */
    int port = 3232;
    Spark.port(port);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // CensusDataSource directDatasource = new DirectCensus

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
