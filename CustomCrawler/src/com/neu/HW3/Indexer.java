package com.neu.HW3;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Indexer {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final Node node;
    private final Client client;
    public Indexer() {
        node = nodeBuilder().clusterName("sneha-elasticsearch").node();
        client = node.client();
    }

    public void indexDocument(List<Document> docs) throws Exception{
        for (Document doc : docs) {
            String json = MAPPER.writeValueAsString(doc);
            System.out.println("Json: " + json);
            IndexResponse response = client.prepareIndex("ap_dataset", "documents", doc.urlId).setSource(json).execute().actionGet();
            System.out.println("Response: ID:" + response.getId() + " index: " + response.getIndex() + " type: " + response.getType());
        }
    }

   // @Override
    public void close() throws IOException {
        client.close();
        node.close();
    }
}
