package edu.brown.cs.student.main.census;

import static spark.Spark.after;
import edu.brown.cs.student.main.census.csv.loadHandler;
import edu.brown.cs.student.main.census.csv.searchHandler;
import edu.brown.cs.student.main.census.csv.viewHandler;
import edu.brown.cs.student.main.census.census.broadbandHandler;
import java.util.ArrayList;
import java.util.List;
import spark.Spark;

public class Server {
        //this is the only method in server it should use Spark for the following: port, handlers, init, awaitInitialization
        //reference gear up code
        public static void main(String[] args) {
            int port = 3232;
            Spark.port(port);

            after(
                    (request, response) -> {
                        response.header("Access-Control-Allow-Origin", "*");
                        response.header("Access-Control-Allow-Methods", "*");
                    });

            Spark.get("/", (req, res) -> "welcome to the server!");

            Spark.get("load", new loadHandler());
            Spark.get("search", new searchHandler());
            Spark.get("view", new viewHandler());
            Spark.get("broadband", new broadbandHandler());

            Spark.init();
            Spark.awaitInitialization();

            System.out.println("Server started at http://localhost:" + port);



            }
}
