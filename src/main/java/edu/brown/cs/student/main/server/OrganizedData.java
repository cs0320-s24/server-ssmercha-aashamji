  package edu.brown.cs.student.main.server;

  import java.util.ArrayList;
  import java.util.HashMap;
  import java.util.List;
  import java.util.Map;

  public class OrganizedData {
    public List results;
    public Parser myParser;
    public Map<String, Boolean> pathLoaded = new HashMap<>();

    public OrganizedData() {
      results = new ArrayList<>();
    }

    public void updateList(List given) {
      this.results = List.copyOf(given);
    }

    public void parserRef(Parser p) {
      myParser = p;
    }

    public void updateState(String filepath) {
      pathLoaded.put(filepath, true);
    }

    public boolean isLoaded(String filepath) {
      if (pathLoaded.containsKey(filepath)) {
        if (pathLoaded.get(filepath)) {
          return true;
        }
        return false;
      }
      return false;
    }
  }
