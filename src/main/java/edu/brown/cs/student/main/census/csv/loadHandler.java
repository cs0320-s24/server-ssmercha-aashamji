package edu.brown.cs.student.main.census.csv;

import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static java.lang.System.exit;

/*
This class will load a specific csv file given a path
* */
public class loadHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String path = request.queryParams("filepath");

        FileReader f = null;
        try {
            f = new FileReader(path);
        } catch (FileNotFoundException e) {
            System.err.println("Sorry, that file doesn't exist.");
        }

        return null;
    }
}
