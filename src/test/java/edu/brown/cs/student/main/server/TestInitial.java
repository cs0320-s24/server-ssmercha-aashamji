package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.server.census.broadbandHandler;
import edu.brown.cs.student.main.server.csv.loadHandler;
import edu.brown.cs.student.main.server.csv.searchHandler;
import edu.brown.cs.student.main.server.csv.viewHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import spark.Spark;

public class TestInitial {
  @BeforeEach
  public void setup() {
    // In fact, restart the entire Spark server for every test!
    OrganizedData o = new OrganizedData();

    Spark.get("loadcsv", new loadHandler(o));
    Spark.get("searchcsv", new searchHandler(o));
    Spark.get("viewcsv", new viewHandler(o));
    Spark.get("broadband", new broadbandHandler());
    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints after each test
    Spark.unmap("loadcsv");
    Spark.unmap("searchcsv");
    Spark.unmap("viewcsv");
    Spark.unmap("broadband");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /**
   * Helper to start a connection to a specific API endpoint/params
   *
   * @param apiCall the call string, including endpoint (NOTE: this would be better if it had more
   *     structure!)
   * @return the connection for the given URL, just after connecting
   * @throws IOException if the connection fails for some reason
   */
  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

    // The default method is "GET", which is what we're using here.
    // If we were using "POST", we'd need to say so.
    clientConnection.setRequestMethod("GET");

    clientConnection.connect();
    return clientConnection;
  }

  //  @Test
  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type
  // checker
  //  public void testAPINoRecipes() throws IOException {
  //    HttpURLConnection clientConnection = tryRequest("loadcsv");
  //    // Get an OK response (the *connection* worked, the *API* provides an error response)
  //    assertEquals(200, clientConnection.getResponseCode());
  //
  //    // Now we need to see whether we've got the expected Json response.
  //    // SoupAPIUtilities handles ingredient lists, but that's not what we've got here.
  //    Moshi moshi = new Moshi.Builder().build();
  //    // We'll use okio's Buffer class here
  //    OrderHandler.SoupNoRecipesFailureResponse response =
  //            moshi
  //                    .adapter(OrderHandler.SoupNoRecipesFailureResponse.class)
  //                    .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
  //
  //    System.out.println(response);
  //    // ^ If that succeeds, we got the expected response. Notice that this is *NOT* an exception,
  // but
  //    // a real Json reply.
  //
  //    clientConnection.disconnect();
  //  }
}
