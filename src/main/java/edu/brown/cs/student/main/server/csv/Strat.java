package edu.brown.cs.student.main.server.csv;

import java.util.List;

/**
 * This is the Strat class that implements the CreatorFromRow interface. It is used when parsing a
 * file that a user will search
 */
public class Strat implements CreatorFromRow<List<String>> {
  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }
}
