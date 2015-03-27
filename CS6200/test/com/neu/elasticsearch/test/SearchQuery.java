package com.neu.elasticsearch.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.elasticsearch.action.search.SearchResponse;
import org.junit.Test;

import com.neu.elasticsearch.SearchQueries;

public class SearchQuery {

    @Test
    public void test() throws FileNotFoundException, IOException {
        SearchQueries qp = new SearchQueries(new File("C:/Users/snehanay/Google Drive/CS6200/AP89_DATA/AP_DATA/newStopList.txt"));
        SearchResponse response = qp.executeSearch("translate");
        //System.out.println(response.getHits().getHits().toString());   
    }
}
