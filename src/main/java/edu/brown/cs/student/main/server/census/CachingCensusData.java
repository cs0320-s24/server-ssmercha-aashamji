package edu.brown.cs.student.main.server.census;



public class CachingCensusData implements CensusDataSource {

    private final CensusDataSource original;

    public CachingCensusData(CensusDataSource original){
        this.original = original;
    }




}
