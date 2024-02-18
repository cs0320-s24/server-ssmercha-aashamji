package edu.brown.cs.student.main.server.census;

import java.util.List;
import java.util.Map;

/**
 * a record that describes each entry of the census data.
 * @param entry is a string list to reflect each entry from the census dataset
 */
public record CensusData(Map<String, Object> entry) {}
