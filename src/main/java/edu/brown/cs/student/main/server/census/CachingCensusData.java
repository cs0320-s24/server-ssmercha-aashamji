package edu.brown.cs.student.main.server.census;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

/**
 * A caching layer for Census data retrieval, which utilizes a LoadingCache to store
 * and retrieve CensusData instances. This class aims to reduce the number of calls
 * to the underlying CensusDataSource by caching the results of previous queries.
 */
public class CachingCensusData implements CensusDataSource {

  private final CensusDataSource original;
  private final LoadingCache<String, CensusData> cache;

    /**
     * constructor
     * @param original is the original data source intended to be wrapped with caching functionality.
     */
  public CachingCensusData(CensusDataSource original) {

    this.original = original;
    this.cache =
        CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(
                new CacheLoader<String, CensusData>() {
                  @NotNull
                  @Override
                  public CensusData load(String query) throws Exception {
                    return original.getData(query);
                  }
                });
  }

    /**
     * retrieves CensusData for a given query.
     * @param query - the string being retrieved
     * @return the query string from the cache it if exists
     * @throws IllegalArgumentException if the query cannot be found in the cache.
     */
  @Override
  public CensusData getData(String query) throws IllegalArgumentException {
    try {
      return cache.get(query);
    } catch (ExecutionException e) {
      throw new RuntimeException("Error fetching data from cache.", e);
    }
  }

    /**
     * getCache Method
     * @return the cache to bypass private declaration.
     */
  public LoadingCache<String, CensusData> getCache() {
    return cache;
  }
}
