package edu.brown.cs.student.main.server;

import java.util.ArrayList;
import java.util.List;

public class OrganizedData {
  public List results;

  public OrganizedData() {
    results = new ArrayList<>();
  }

  public void updateList(List given) {
    this.results = given;
  }
}
