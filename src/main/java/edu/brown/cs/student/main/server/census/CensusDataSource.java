package edu.brown.cs.student.main.server.census;

public interface CensusDataSource {
  CensusData getData(String query) throws IllegalArgumentException;
}