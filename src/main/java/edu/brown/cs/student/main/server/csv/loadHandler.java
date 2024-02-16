package edu.brown.cs.student.main.server.csv;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.OrganizedData;
import edu.brown.cs.student.main.server.Parser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

/*
This class will load a specific csv file given a path
* */
public class loadHandler implements Route {

  private static OrganizedData myData;

  public loadHandler(OrganizedData o) {
    myData = o;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Set<String> params = request.queryParams();
    String filepath = request.queryParams("filepath");
    String headers = request.queryParams("headers");
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);

    Map<String, Object> responseMap = new HashMap<>();
    Integer h = 0;
    try {
      h = Integer.parseInt(headers);
    } catch (NumberFormatException e) {
      responseMap.put("result", "error_bad_request");
      return adapter.toJson(responseMap);
    }

    try {
      if (filepath != null) {
        FileReader f = new FileReader(filepath);
        responseMap.put("filepath", filepath);

        Strat s = new Strat();
        Parser<List<String>> p = new Parser<>(f, s, h);

        myData.updateList(p.parsedData);
        myData.parserRef(p);
        myData.updateState(filepath);
        responseMap.put("result", "success");
      } else {
        responseMap.put("result", "error_bad_request");
      }
    } catch (FileNotFoundException | FactoryFailureException e) {
      responseMap.put("result", "error_file_loading");
    }
    return adapter.toJson(responseMap);
  }
}
