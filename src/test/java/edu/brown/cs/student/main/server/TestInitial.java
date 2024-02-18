package edu.brown.cs.student.main.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.testng.AssertJUnit.assertNotNull;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.census.broadbandHandler;
import edu.brown.cs.student.main.server.csv.loadHandler;
import edu.brown.cs.student.main.server.csv.searchHandler;
import edu.brown.cs.student.main.server.csv.viewHandler;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Spark;

public class TestInitial {
  private final Type listListString =
      Types.newParameterizedType(List.class, List.class, String.class);
  private JsonAdapter<List<List<String>>> adapter;

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

    Moshi moshi = new Moshi.Builder().build();
    adapter = moshi.adapter(listListString);
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints after each test
    Spark.stop();
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

  @Test
  public void testingLoadCSV() throws IOException {
    HttpURLConnection connection = tryRequest("loadcsv?filepath=data/RI.csv&headers=0");
    assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode(), "should be 200");

    String responseJson = new Buffer().readFrom(connection.getInputStream()).readUtf8();
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, String>> jsonAdapter = moshi.adapter(Types.newParameterizedType(Map.class, String.class, String.class));

    Map<String, String> responseMap = jsonAdapter.fromJson(responseJson);
    assertNotNull(responseMap);
    assertEquals("success", responseMap.get("result"));
    assertEquals("data/RI.csv", responseMap.get("filepath"));
    connection.disconnect();
  }

  @Test
  public void testingSearchCSV() throws IOException{
    HttpURLConnection loadConnection = tryRequest("loadcsv?filepath=data/RI.csv&headers=0");
    assertEquals(HttpURLConnection.HTTP_OK, loadConnection.getResponseCode(), "should be 200");
    loadConnection.disconnect();

    HttpURLConnection connection = tryRequest("searchcsv?filepath=data/RI.csv&headers=0&identType=N/A&colIdentifier=N/A&word=Providence");
    assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode(), "should be 200");

    String responseJson = new Buffer().readFrom(connection.getInputStream()).readUtf8();
    Moshi moshi = new Moshi.Builder().build();
    //Type listOfListsType = Types.newParameterizedType(List.class, List.class, String.class);
    JsonAdapter<SearchResult> jsonAdapter = moshi.adapter(SearchResult.class);
    SearchResult response = jsonAdapter.fromJson(responseJson);
    assertNotNull(response);
    assertNotNull(response.getData());
    assertFalse(response.getData().isEmpty());
    connection.disconnect();
  }

  @Test
  public void testingViewCSV() throws IOException{
    HttpURLConnection loadConnection = tryRequest("loadcsv?filepath=data/RI.csv&headers=0");
    assertEquals(HttpURLConnection.HTTP_OK, loadConnection.getResponseCode(), "should be 200");
    loadConnection.disconnect();

    HttpURLConnection connection = tryRequest("viewcsv?filepath=data/RI.csv");
    assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode(), "should be 200");

    String responseJson = new Buffer().readFrom(connection.getInputStream()).readUtf8();
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<Object, Object>> jsonAdapter = moshi.adapter(Types.newParameterizedType(Map.class, Object.class, Object.class));

    Map<Object, Object> responseMap = jsonAdapter.fromJson(responseJson);
    assertNotNull(responseMap);
    assertEquals("success", responseMap.get("result"));


    connection.disconnect();
  }
  @Test
  public void testingViewCSVThrows() throws IOException{

    HttpURLConnection connection = tryRequest("viewcsv?filepath=data/RI.csv");
    assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode(), "should not be 200");

    String responseJson = new Buffer().readFrom(connection.getInputStream()).readUtf8();
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<Object, Object>> jsonAdapter = moshi.adapter(Types.newParameterizedType(Map.class, Object.class, Object.class));

    Map<Object, Object> responseMap = jsonAdapter.fromJson(responseJson);
    assertNotNull(responseMap);
    assertEquals("error_file_not_loaded", responseMap.get("result"));
  }


}
