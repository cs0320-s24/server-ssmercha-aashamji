package edu.brown.cs.student.main.server;

import java.util.List;

public class SearchResult {
    private List<List<String>> data;
    private String result;

    public String getResult(){
        return result;
    }
    public List<List<String>> getData(){
        return data;
    }
}
