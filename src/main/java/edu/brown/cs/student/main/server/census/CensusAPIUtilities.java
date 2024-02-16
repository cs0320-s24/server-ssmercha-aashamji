package edu.brown.cs.student.main.server.census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CensusAPIUtilities {
  //    public CensusAPIUtilities(){}

  public static List<List<String>> deserialize(String json) throws IOException {
    try {
      // Initializes Moshi
      Moshi moshi = new Moshi.Builder().build();

      Type listStringObject = Types.newParameterizedType(List.class, List.class, String.class);
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(listStringObject);
      //      System.out.println(json);
      return adapter.fromJson(json);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  public static Map<String, String> listToMap(List<List<String>> l, int stateOrCounty) {
    Map<String, String> nameToCode = new HashMap<>();
    if (stateOrCounty == 0) {
      for (int x = 0; x < l.size(); x++) {
        String key = l.get(x).get(0);
        String value = l.get(x).get(1);
        nameToCode.put(key, value);
      }
    } else {
      for (int x = 0; x < l.size(); x++) {
        String key = l.get(x).get(0);
        String value = l.get(x).get(2);
        nameToCode.put(key, value);
      }
    }

    return nameToCode;
  }
}
