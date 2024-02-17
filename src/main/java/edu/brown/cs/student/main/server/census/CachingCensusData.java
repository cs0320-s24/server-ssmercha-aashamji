package edu.brown.cs.student.main.server.census;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

public class CachingCensusData implements CensusDataSource {

  private final CensusDataSource original;
  private final LoadingCache<String, CensusData> cache;

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

  @Override
  public CensusData getData(String query) throws IllegalArgumentException {
    try {
      return cache.get(query);
    } catch (ExecutionException e) {
      throw new RuntimeException("Error fetching data from cache.", e);
    }
  }

    public LoadingCache<String, CensusData> getCache() {
        return cache;
    }
}
