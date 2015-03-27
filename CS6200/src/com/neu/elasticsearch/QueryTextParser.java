package com.neu.elasticsearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

public class QueryTextParser {
    private final List<String> stopWords;

    public QueryTextParser(File stopWordFile) throws FileNotFoundException, IOException {
        this.stopWords = new ArrayList<>();
        if (stopWordFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(stopWordFile))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stopWords.add((line.trim()));
                }
            }
        }
        // System.out.println("stopWords " + stopWords);
    }

    /*
     * public Map<String, Float> parseFile(File file) throws IOException,
     * IOException { Map<String, Float> hits = new HashMap<>(); if
     * (file.exists()) { try (BufferedReader reader = new BufferedReader(new
     * FileReader(file))) { String line = reader.readLine(); // while ((line =
     * reader.readLine()) != null) { String[] terms = line.split("\\s+");
     * for(int j=1;j<terms.length;j++){ //for (String queryTerm : terms) { if
     * (stopWords.contains(terms[j])) { continue; } try (SearchQueries sq = new
     * SearchQueries()) { SearchResponse result = sq.executeSearch(terms[j]);
     * for (SearchHit hit: result.getHits().getHits()) { hits.put(hit.getId(),
     * Float.valueOf(hit.score())); } } } } } return hits; }
     */

    public QueryResult parseFile(File file) throws IOException, IOException {
        QueryResult allHits = new QueryResult();
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    int termNo = 0;
                    Hits queryHits = new Hits();
                    String[] terms = line.split("\\s+");
                    int queryNo = Integer.valueOf(terms[0].replace(".", " ").trim());
                    for (int j = 1; j < terms.length; j++) {
                        if (stopWords.contains(terms[j])) {
                            continue;
                        }
                        try (SearchQueries sq = new SearchQueries(file)) {
                            SearchResponse result = sq.executeSearch(terms[j]);
                            // try (BufferedReader resultReader =
                            // BufferedReader(resultToString.split("\\s+"))) {
                            queryHits.frequency.add((int) result.getHits().getTotalHits());
                            for (SearchHit hit : result.getHits().getHits()) {
                                queryHits.docId.add(hit.getId());
                                queryHits.termNumber.add(termNo);
                                queryHits.queryNumber = queryNo;
                            }
                            String[] resultToString = result.toString().split("\\s+");
                            for (int s = 0; s < resultToString.length; s++) {
                                if (resultToString[s].contains("termFreq=")) {
                                    int tf = Integer.valueOf(resultToString[s].substring("termFreq=".length() + 1, resultToString[s].length() - 3).trim());
                                    queryHits.tf.add((float) tf);
                                    System.out.println("tf = " + tf);
                                   // break;
                                }
                            }
                            termNo++;
                        }
                    }
                    allHits.qResult.add(queryHits);
                }
            }
        }
        // System.out.println("in QueryTextParser\n size of hits docId: " +
        // queryHits.docId.size() + "size of tf" + queryHits.tf.size());
        return allHits;
    }

    public static void main(String[] args) throws IOException {
        QueryTextParser qp = new QueryTextParser(new File("C:/Users/snehanay/Google Drive/CS6200/AP89_DATA/AP_DATA/newStopList.txt"));
        File file = new File("C:/Users/snehanay/Google Drive/CS6200/AP89_DATA/AP_DATA/dummyQuery.txt");// query_desc.51-100.short.txt");
        QueryResult allHits = qp.parseFile(file);
        // for (Entry<String, Float> entry: hits.entrySet()) {
        // System.out.println("DocId: " + entry.getKey() + " score: " +
        // entry.getValue());
        // }
        for (int q = 0; q < allHits.qResult.size(); q++) {
            System.out.println("**********query changed************");
            for (int l = 0; l < allHits.qResult.get(q).tf.size(); l++) {
                System.out.println("DocId: " + allHits.qResult.get(q).docId.get(l) + " score: " + allHits.qResult.get(q).tf.get(l) + " termNo: "
                        + allHits.qResult.get(q).termNumber.get(l) + " query number: " + allHits.qResult.get(q).queryNumber);
            }
        }
    }

}