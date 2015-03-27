package com.neu.elasticsearch;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;

public class SearchQueries implements Closeable {
    private final Node node;
    private final Client client;
    private final List<String> stopWords;

    public SearchQueries(File stopWordFile) throws FileNotFoundException, IOException {
        node = nodeBuilder().clusterName("sneha-elasticsearch").node();
        client = node.client();
        this.stopWords = new ArrayList<>();
        if (stopWordFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(stopWordFile))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stopWords.add((line.trim()));
                }
            }
        }
    }

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
                        SearchResponse result = executeSearch(terms[j]);
                        queryHits.frequency.add((int) result.getHits().getTotalHits());
                        for (SearchHit hit : result.getHits().getHits()) {
                            queryHits.docId.add(hit.getId());
                            queryHits.tf.add(hit.score());
                            queryHits.termNumber.add(termNo);
                            queryHits.queryNumber = queryNo;
                            String[] resultToString = result.getHits().getHits().toString().split("\\s+");
                            for (int s = 0; s < resultToString.length; s++) {
                                if (resultToString[s].contains("termFreq=")) {
                                    int tf = Integer.valueOf(resultToString[s].substring("termFreq=".length() + 1, resultToString[s].length() - 3).trim());
                                    queryHits.tf.add((float) tf);
                                }
                            }
                        }
                        termNo++;
                    }
                    allHits.qResult.add(queryHits);
                }
            }
        }
        return allHits;
    }

    public SearchResponse executeSearch(String term) {
        SearchResponse response = client.prepareSearch("ap_dataset").setTypes("documents").setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchQuery("text", term)).setSize(84680).setExplain(true).execute().actionGet();
        System.out.println("Analysed term: " + term);
        System.out.println(response.toString());
        return response;
    }

    @Override
    public void close() throws IOException {
        client.close();
        node.close();
    }

    public static void main(String[] args) throws IOException {

        try (SearchQueries qp = new SearchQueries(new File("C:/Users/snehanay/Google Drive/CS6200/AP89_DATA/AP_DATA/newStopList.txt"))) {
            File file = new File("C:/Users/snehanay/Google Drive/CS6200/AP89_DATA/AP_DATA/dummyQuery.txt");// query_desc.51-100.short.txt");
            QueryResult allHits = qp.parseFile(file);
        }
    }
}