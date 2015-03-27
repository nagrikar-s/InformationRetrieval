package com.neu.HW2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * tf- total number of appearance of term in a document
 * cf- total number of appearance of term in a document collection
 * df- total number of documents in which the team appeared
 */
public class ParseDocument {
    // private final int maxTerms = 1000;
    private int termIndex;
    private Map<String, Index> termIndices = new HashMap<>();

    public Map<String, Index> parseFiles(File folder, String filePrefix, HashSet<String> terms, HashSet<String> stopwords) throws IOException {
        if (!folder.exists()) {
            // System.err.println("Folder " + folder.getAbsolutePath() +
            // " does not exist");
        }
        File[] listOfFiles = folder.listFiles();
        // System.out.println("Indexing " + listOfFiles.length + " files");
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().startsWith(filePrefix)) {
                // System.out.println("checking in file " +
                // file.getAbsolutePath());
                parse(file.getAbsolutePath(), terms, stopwords);
            }
        }
        // System.setOut(new
        // PrintStream("C:\\Users\\snehanay\\Google Drive\\CS6200\\HW2\\index.txt"));
        System.out.println(termIndices.size());
        for (Entry<String, Index> entry : termIndices.entrySet()) {
            Index temp = entry.getValue();
            System.out.println("term: " + entry.getKey() + " cf: " + temp.cf + " df: " + temp.df + " " + temp.id + " " + temp.tf + " " + temp.positions);
        }
        return termIndices;
    }

    public void parse(String fileName, HashSet<String> term, HashSet<String> stopwords) throws IOException {
        // System.out.println("entering parse");
        File file = new File(fileName);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = null;
                String text;
                String newTerm;
                String id = null;
                StringBuilder builder = new StringBuilder();
                boolean isTextStarted = false;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("</TEXT>")) {
                        isTextStarted = false;
                    }
                    if (line.startsWith("<DOCNO>")) {
                        id = line.substring("<DOCNO>".length(), line.indexOf("</DOCNO>")).trim();
                    }
                    if (isTextStarted) {
                        builder.append("\n");
                        builder.append(line.trim());
                    }
                    if (line.startsWith("<TEXT>")) {
                        isTextStarted = true;
                    }
                    if (line.startsWith("</DOC>")) {
                        // System.out.println("entered doc end");
                        // finished the doc, now index it
                        text = builder.toString();
                        builder.setLength(0);
                        Pattern pattern = Pattern.compile("\\w+(\\.?\\w+)*");
                        Matcher matcher = pattern.matcher(text.toLowerCase());
                        termIndex = 1;
                        Map<String, List<Integer>> docPositions = new HashMap<>();
                        int tempTF = 0;
                        Map<String, Integer> TFs = new HashMap<>();
                        while (matcher.find()) {
                            // USUALLY ZERO
                            // System.out.println("matching text");
                            Map<String, String> updated = new HashMap<>();
                            List<Integer> pos = new ArrayList<>();
                            for (int i = 0; i < matcher.groupCount(); i++) {
                                Porter obj = new Porter();
                                newTerm = obj.stripAffixes(matcher.group(i));
                                if (!stopwords.contains(newTerm) && term.contains(newTerm)) {
                                    // System.out.println(newTerm);
                                    updated.put(newTerm, "yes");
                                    // System.out.println("updated");
                                    if (docPositions.containsKey(newTerm)) {
                                        pos = docPositions.get(newTerm);
                                        tempTF = TFs.get(newTerm);
                                    }
                                    pos.add(termIndex);
                                    docPositions.put(newTerm, pos);
                                    tempTF++;
                                    TFs.put(newTerm, tempTF);
                                    // System.out.println(pos);
                                }
                                termIndex++;
                            }
                            // System.out.println("upadating indices");
                            for (Entry<String, String> entry : updated.entrySet()) {
                                // System.out.println(entry.getKey() + " " +
                                // entry.getValue());
                                if (updated.get(entry.getKey()) == "yes") {
                                    Index updatedIndex = new Index();
                                    int newDF = 1;
                                    int newCF = TFs.get(entry.getKey());
                                    List<String> newDocIds = new ArrayList<>();
                                    List<Integer> newTFs = new ArrayList<>();
                                    List<List<Integer>> newPos = new ArrayList<>();
                                    if (termIndices.containsKey(entry.getKey())) {
                                        updatedIndex = termIndices.get(entry.getKey());
                                        newDF = updatedIndex.df + newDF;
                                        newCF = updatedIndex.cf + newCF;
                                        newDocIds = updatedIndex.id;
                                        newTFs = updatedIndex.tf;
                                        newPos = updatedIndex.positions;
                                    }
                                    newPos.add(docPositions.get(entry.getKey()));
                                    newTFs.add(TFs.get(entry.getKey()));
                                    newDocIds.add(id);
                                    updatedIndex.df = newDF;
                                    updatedIndex.cf = newCF;
                                    updatedIndex.id = newDocIds;
                                    updatedIndex.tf = newTFs;
                                    updatedIndex.positions = newPos;
                                    termIndices.put(entry.getKey(), updatedIndex);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static HashSet<String> tokenizerTest(String filename) throws IOException {
        File file = new File(filename);
        HashSet<String> stopwords = new HashSet<>();
        // List<String> stopwords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                stopwords.add(line.trim());
            }
        }
        return stopwords;
    }

    public static void main(String[] args) throws IOException {
        String term1 = "translat";
        String term2 = "democrat";

        File file = new File("C:\\Users\\snehanay\\Google Drive\\CS6200\\AP89_DATA\\AP_DATA\\ap89_collection");
        ParseDocument parser = new ParseDocument();
        HashSet<String> stopwords = tokenizerTest("C:\\Users\\snehanay\\Google Drive\\CS6200\\HW2\\stopwords.txt");
        HashSet<String> tokens = new HashSet<>();
        tokens.add(term1);
        tokens.add(term2);
        parser.parseFiles(file, "ap", tokens, stopwords);
        // parser.parse("C:\\Users\\snehanay\\Google Drive\\CS6200\\AP89_DATA\\AP_DATA\\ap89_collection\\AP890102",
        // tokens, stopwords);
    }
}
