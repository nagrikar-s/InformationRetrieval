package com.neu.HW2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenizerTest {
    // private int printingTermNo = 1;
    private int maxTerms = 2;
    public HashSet<String> tokens = new HashSet<>();
    public HashSet<String> indexedTerms = new HashSet<>();
    private Map<String, Index> termDetails = new HashMap<>();
    private HashSet<String> allTerms = new HashSet<>();
    private static final String vocab = "vocabulary";
    private static final String tcf = "totalTokens";
    private int vocabulary = 0;
    private int totalTokens = 0;
    private Map<String, Integer> stats = new HashMap<>();
    private String prefix = "ap";
    private HashSet<String> stopwords;
    private static File indexFile = new File("C:\\Users\\snehanay\\Google Drive\\CS6200\\HW2\\index.txt");
    private static File catalogFile = new File("C:\\Users\\snehanay\\Google Drive\\CS6200\\HW2\\catalog.txt");
    private static File folder = new File("C:\\Users\\snehanay\\Google Drive\\CS6200\\AP89_DATA\\AP_DATA\\ap89_collection");

    public TokenizerTest(String filename) throws IOException {
        //System.setOut(new PrintStream(indexFile));
        File file = new File(filename);
        this.stopwords = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                stopwords.add(line.trim());
            }
        }
    }

    public Map<String, Integer> parseFiles(File folder, String filePrefix) throws IOException {
        if (!folder.exists()) {
            // System.err.println("Folder " + folder.getAbsolutePath() +
            // " does not exist");
            return stats;
        }
        File[] listOfFiles = folder.listFiles();
        // System.out.println("Indexing " + listOfFiles.length + " files");
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().startsWith(filePrefix)) {
                // System.out.println("indexing file name " +
                // file.getAbsolutePath());
                indexer(file.getAbsolutePath());
                // break;
            }
        }
        stats.put(vocab, vocabulary);
        stats.put(tcf, totalTokens);
        return stats;
    }

    public void indexer(String file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;
            StringBuilder builder = new StringBuilder();
            boolean isTextStarted = false;
            String text = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("</TEXT>")) {
                    isTextStarted = false;
                }
                if (isTextStarted) {
                    builder.append(" ");
                    builder.append(line.trim());
                }
                if (line.startsWith("<TEXT>")) {
                    isTextStarted = true;
                }
                if (line.startsWith("</DOC>")) {
                    text = builder.toString();
                    builder.setLength(0);
                    matchText(text);
                }
            }
        }
    }

    public void matchText(String text) throws IOException {
        Pattern pattern = Pattern.compile("\\w+(\\.?\\w+)*");
        Matcher matcher = pattern.matcher(text.toLowerCase());
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                Porter obj = new Porter();
                String term = obj.stripAffixes(matcher.group(i));
                if (!stopwords.contains(term)) {
                    totalTokens++;
                    if (allTerms.add(term)) {
                        tokens.add(term);
                        vocabulary++;
                        // System.out.println("term no: " + vocabulary +
                        // " term: " + term);
                        // System.out.println(details.cf + " " + details.df +
                        // " " + details.id + " " + details.tf + " " +
                        // details.positions);
                        // System.out.println("termDetails.size: " +
                        // termDetails.size());
                        // System.out.println(tokens.size());
                        if (tokens.size() == maxTerms) {
                            ParseDocument p = new ParseDocument();
                            termDetails = p.parseFiles(folder, prefix, tokens, stopwords);
                            printIndexToFile();
                        }
                    }
                }
            }
        }
    }

    public void printIndexToFile() throws IOException {
        for (Entry<String, Index> entry : termDetails.entrySet()) {
            Index temp = entry.getValue();
            System.out.print(entry.getKey() + " cf: " + temp.cf + " df: " + temp.df);
            for (int doc = 0; doc < temp.id.size(); doc++) {
                System.out.print(" (" + temp.id.get(doc) + " " + temp.tf.get(doc) + " " + temp.positions.get(doc) + ")");
            }
            System.out.println("");
        }
        ContentToByteArrayExample catlogWriter = new ContentToByteArrayExample();
        // System.out.println("writing to catalog");
        catlogWriter.readContentIntoByteArray(indexFile);

        termDetails.clear();
        tokens.clear();
    }

    public HashSet<String> getIndexedTerms() throws IOException {

        if (catalogFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(catalogFile))) {
                String catLine = null;
                while ((catLine = reader.readLine()) != null) {
                    String[] words = catLine.split(" ");
                    indexedTerms.add(words[0]);
                }
            }
        }
        return indexedTerms;
    }

    public static void main(String[] args) throws IOException {
        TokenizerTest tester = new TokenizerTest("C:\\Users\\snehanay\\Google Drive\\CS6200\\HW2\\stopwords.txt");
        Map<String, Integer> finalStats = tester.parseFiles(folder, "ap");
        System.out.println("total tokens: " + finalStats.get(tcf) + " vocabulary:  " + finalStats.get(vocab));
    }
}