package edu.brown.cs.student.main.server.census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains helpers for broadbandHandler
 */
public class CensusAPIUtilities {

  /**
   * This method deserializes a given json into  a list of list of strings
   * @param json given json
   * @return List<List<String>> containing the contents of the json
   * @throws IOException
   */
  public static List<List<String>> deserialize(String json) throws IOException {
    try {
      // Initializes Moshi
      Moshi moshi = new Moshi.Builder().build();
      Type listStringObject = Types.newParameterizedType(List.class, List.class, String.class);
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(listStringObject);
      return adapter.fromJson(json);
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * This method converts a List<List<String>> into a Map<String,String>
   * @param l is the given list
   * @param stateOrCounty indicates if these are state codes or county codes (0 if state, 1 if county)
   * @return Map<String, String> storing location names and their codes 
   */
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
