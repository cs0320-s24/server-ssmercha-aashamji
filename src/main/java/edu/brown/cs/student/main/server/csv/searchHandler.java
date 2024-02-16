package edu.brown.cs.student.main.server.csv;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.OrganizedData;
import edu.brown.cs.student.main.server.Searcher;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/*
This class sends back rows matching the given search criteria.
* */
public class searchHandler implements Route {

  private static OrganizedData myData;

  public searchHandler(OrganizedData o) {
    myData = o;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String, Object> responseMap = new HashMap<>();
    String filepath = request.queryParams("filepath");
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);

    if (myData.isLoaded(filepath)) {
      Searcher look = new Searcher(myData.myParser, filepath);
      if (!myData.myParser.error.equals("no error")) {
        responseMap.put("result", myData.myParser.error);
      } else {
        String h = request.queryParams("headers");
        responseMap.put("headers", h);

        String identType = request.queryParams("identType");
        responseMap.put("identType", identType);
        String colIdentifier = request.queryParams("colIdentifier");
        responseMap.put("colIdentifier", colIdentifier);
        String word = request.queryParams("word");
        responseMap.put("word", word);

        if ((h == null)
            || (identType == null)
            || (colIdentifier == null)
            || (word == null)
            || ((!h.equals("0")) && (!h.equals("1")))) {
          responseMap.put("result", "error_bad_request");
          return adapter.toJson(responseMap);
        }

        if (identType.equals("index")) {
          responseMap.put("data", look.search(word, Integer.parseInt(colIdentifier)));
        } else if (identType.equals("N/A")) {
          responseMap.put("data", look.search(word));
        } else if (identType.equals("header")) {
          responseMap.put("data", look.search(word, colIdentifier));
        } else {
          responseMap.put("result", "error_bad_request");
        }
      }
    } else {
      responseMap.put("result", "error_file_not_loaded");
    }

    return adapter.toJson(responseMap);
  }
}
