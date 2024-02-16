package edu.brown.cs.student.main.server.census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class broadbandHandler implements Route {
  @Override
  public Object handle(Request request, Response response) throws Exception {
    String state = request.queryParams("state");
    String county = request.queryParams("county");

    String states = this.sendStateCodesRequest();

    List<List<String>> stateCodesList = CensusAPIUtilities.deserialize(states);
    Map<String, String> stateCodesMap = CensusAPIUtilities.listToMap(stateCodesList, 0);

    String sCode = stateCodesMap.get(state);
    String counties = this.sendCountyCodesRequest(sCode);
    List<List<String>> countyCodesList = CensusAPIUtilities.deserialize(counties);
    Map<String, String> countyCodesMap = CensusAPIUtilities.listToMap(countyCodesList, 1);
    String countyCode = countyCodesMap.get(county + ", " + state);

    String broadBandPercentage = this.sendBroadBandRequest(sCode, countyCode);
    List<List<String>> broadBandList = CensusAPIUtilities.deserialize(broadBandPercentage);

    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("state", state);
    responseMap.put("county", county);
    responseMap.put("broadband Access Percentage", broadBandList.get(1).get(1));
    responseMap.put("result", "success");

    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);

    return adapter.toJson(responseMap);
  }

  private String sendBroadBandRequest(String sCode, String countyCode)
      throws IOException, InterruptedException, URISyntaxException {
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

    return sentBoredApiResponse.body();
  }

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

    return sentBoredApiResponse.body();
  }

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

    return sentBoredApiResponse.body();
  }
}
