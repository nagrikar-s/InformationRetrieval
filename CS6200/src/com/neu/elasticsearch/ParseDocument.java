package com.neu.elasticsearch;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ParseDocument implements Closeable {
    // on startup
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final Node node;
    private final Client client;
    //String[] docName = new String[84678];
    public int length = 0;
    private Map<String, Integer> docLength = new HashMap<>();
    int i = 0;
    public ParseDocument() {
        node = nodeBuilder().clusterName("sneha-elasticsearch").node();
        client = node.client();
    }

    private void indexDocument(Document doc) throws Exception {
        String json = MAPPER.writeValueAsString(doc);
        System.out.println("Json: " + json);
        IndexResponse response = client.prepareIndex("ap_dataset", "documents", doc.id).setSource(json).execute().actionGet();
        System.out.println("Response: ID:" + response.getId() + " index: " + response.getIndex() + " type: " + response.getType());
    }

    public void parseFiles(File folder, String filePrefix) {
        if (!folder.exists()) {
            System.err.println("Folder " + folder.getAbsolutePath() + " does not exist");
            return;
        }
        File[] listOfFiles = folder.listFiles();
        System.out.println("Indexing " + listOfFiles.length + " files");
        
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().startsWith(filePrefix)) {
                System.out.println("indexing file name " + file.getAbsolutePath());
                parse(file.getAbsolutePath());
            }
        }
    }

    public void parse(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = null;
                StringBuilder builder = new StringBuilder();
                boolean isTextStarted = false;
                String id = null;
                String fileId = null;
                String text = null;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("</TEXT>")) {
                        isTextStarted = false;
                    }
                    if (isTextStarted) {
                        builder.append(" ");
                        builder.append(line.trim());
                        String[] words = line.split(" ");
                        length = length + words.length;
                    }
                    if (line.startsWith("<DOCNO>")) {
                        id = line.substring("<DOCNO>".length(), line.indexOf("</DOCNO>")).trim();
                    }
                    if (line.startsWith("<FILEID>")) {
                        fileId = line.substring("<FILEID>".length(), line.indexOf("</FILEID>")).trim();
                    }
                    if (line.startsWith("<TEXT>")) {
                        isTextStarted = true;
                    }
                    if (line.startsWith("</DOC>")) {
                        // finished the doc, now index it
                        text = builder.toString();
                        docLength.put(id, length);
                        length = 0;
                        builder.setLength(0);
                        try {
                            indexDocument(new Document(id, fileId, text));
                        } catch (Exception e) {
                            System.out.println("Failed indexing document: id: " + id + " fileId: " + fileId + " text: " + text);
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed parsing file: " + file + " " + Objects.toString(e.getStackTrace()));
            }
        }
    }

    public Map<String, Integer> documentLength() {
        return docLength;
    }

    @Override
    public void close() throws IOException {
        client.close();
        node.close();
    }
}
