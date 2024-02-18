package edu.brown.cs.student.main.server.census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** This class implements our handler for Census API requests */
public class broadbandHandler implements Route {
  Map<String, Object> responseMap = new HashMap<>();
  CachingCensusData cache;

  /**
   * This is our handle method which interacts with the API to return the wanted information
   *
   * @param request the request object providing information about the HTTP request
   * @param response the response object providing functionality for modifying the response
   * @return a serialized json of the responseMap
   * @throws Exception
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    String useCache = request.queryParams("useCache");
    String query = state + "_" + county;

    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);

    if("true".equalsIgnoreCase(useCache)) {
      CensusData cachedData = cache.getData(query);
      if (cachedData != null) {
        //return adapter.toJson(cachedData);
      } else {
        //search regularly
        //add search result to cache
        //return regular search result
      }
    } else{
      //regular search
    }

    String states =
        this.sendStateCodesRequest(); // to get a String representing all states and their codes

    // deserialize into a list
    List<List<String>> stateCodesList = CensusAPIUtilities.deserialize(states);
    // convert list to map
    Map<String, String> stateCodesMap = CensusAPIUtilities.listToMap(stateCodesList, 0);

    // get the state code for the specific state
    String sCode = stateCodesMap.get(state);
    // get all county codes
    String counties = this.sendCountyCodesRequest(sCode);

    try {
      // deserialize county codes into list
      List<List<String>> countyCodesList = CensusAPIUtilities.deserialize(counties);
      // convert list to string
      Map<String, String> countyCodesMap = CensusAPIUtilities.listToMap(countyCodesList, 1);
      // get specific county code
      String countyCode = countyCodesMap.get(county + ", " + state);

      // make broadband request
      String broadBandPercentage = this.sendBroadBandRequest(sCode, countyCode);
      // deserialize broadband request into list
      List<List<String>> broadBandList = CensusAPIUtilities.deserialize(broadBandPercentage);

      // enter results into responseMap
      responseMap.put("state", state);
      responseMap.put("county", county);
      responseMap.put("broadband Access Percentage", broadBandList.get(1).get(1));
      responseMap.put("result", "success");
    } catch (EOFException e) { // missing any input
      responseMap.put("result", "error_bad_request");
    }

    return adapter.toJson(responseMap); // return json
  }

  /**
   * This sends a request to get the broadband percentage given a state code and county code
   *
   * @param sCode state code
   * @param countyCode county code
   * @return percentage
   * @throws IOException for send()
   * @throws InterruptedException for send()
   * @throws URISyntaxException for uri()
   */
  private String sendBroadBandRequest(String sCode, String countyCode)
      throws IOException, URISyntaxException, InterruptedException {
    HttpRequest buildBoredApiRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                        + countyCode
                        + "&in=state:"
                        + sCode))
            .GET()
            .build();
    HttpResponse<String> sentBoredApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());

    LocalDateTime myDateObj = LocalDateTime.now();
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String formattedDate = myDateObj.format(myFormatObj);
    responseMap.put("time all data received", formattedDate);

    return sentBoredApiResponse.body();
  }

  /**
   * This sends a request to get the broadband percentage given a state code and county code
   *
   * @param state state name
   * @return state code
   * @throws IOException for send()
   * @throws InterruptedException for send()
   * @throws URISyntaxException for uri()
   */
  private String sendCountyCodesRequest(String state)
      throws URISyntaxException, IOException, InterruptedException {
    HttpRequest buildBoredApiRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:"
                        + state))
            .GET()
            .build();

    HttpResponse<String> sentBoredApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());

    LocalDateTime myDateObj = LocalDateTime.now();
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String formattedDate = myDateObj.format(myFormatObj);
//    responseMap.put("time county code info received", formattedDate);

    return sentBoredApiResponse.body();
  }

  /**
   * This sends a request to get the broadband percentage given a state code and county code
   *
   * @return all states and their codes
   * @throws IOException for send()
   * @throws InterruptedException for send()
   * @throws URISyntaxException for uri()
   */
  private String sendStateCodesRequest()
      throws URISyntaxException, IOException, InterruptedException {
    HttpRequest buildBoredApiRequest =
        HttpRequest.newBuilder()
            .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"))
            .GET()
            .build();

    HttpResponse<String> sentBoredApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());

    LocalDateTime myDateObj = LocalDateTime.now();
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String formattedDate = myDateObj.format(myFormatObj);
//    responseMap.put("time state code info received", formattedDate);

    return sentBoredApiResponse.body();
  }
}
