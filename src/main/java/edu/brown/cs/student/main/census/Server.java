package edu.brown.cs.student.main.census;

import spark.Request;
import spark.Response;
import spark.Route;

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

            Spark.get("/", (req, res) -> "welcome to the census server.");

            Spark.get("load", new loadHandler());
            Spark.get("search", new searchHandler());
            Spark.get("view", new viewHandler());

            Spark.init();
            Spark.awaitInitialization();

            System.out.println("Server started at http://localhost:" + port);

            }
}
