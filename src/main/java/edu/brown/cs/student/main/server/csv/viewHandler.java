package edu.brown.cs.student.main.server.csv;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.OrganizedData;
import java.lang.reflect.Type;
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
    String filepath = request.queryParams("filepath");
    Map<String, Object> responseMap = new HashMap<>();

    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, Object.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    if (filepath == null) {
      responseMap.put("result", "error_bad_request");
      return adapter.toJson(responseMap);
    }

    if (myData.isLoaded(filepath)) {
      try {
        responseMap.put("data", myData.results);
        responseMap.put("result", "success");
      } catch (NullPointerException e) {
        responseMap.put("result", "error_datasource");
      }
    } else {
      responseMap.put("result", "error_file_not_loaded");
    }
    return adapter.toJson(responseMap);
  }
}
