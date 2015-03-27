package com.neu.elasticsearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DocumentLength {
    public int length = 0;
    int i = 0;

    public Map<String, Integer> parseFiles(File folder, String filePrefix) {
        Map<String, Integer> tmpLength = new HashMap<>();
        Map<String, Integer> length = new HashMap<>();
        if (!folder.exists()) {
            System.err.println("Folder " + folder.getAbsolutePath() + " does not exist");
            return tmpLength;
        }
        File[] listOfFiles = folder.listFiles();
        System.out.println("calculating document lengths of " + listOfFiles.length + " files");

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().startsWith(filePrefix)) {
                tmpLength = parse(file.getAbsolutePath());
                for (Map.Entry<String, Integer> entry : tmpLength.entrySet()) {
                    length.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return length;
    }

    public Map<String, Integer> parse(String fileName) {
        Map<String, Integer> docLength = new HashMap<>();
        File file = new File(fileName);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = null;
                boolean isTextStarted = false;
                String id = null;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("</TEXT>")) {
                        isTextStarted = false;
                    }
                    if (isTextStarted) {
                        String[] words = line.split(" ");
                        length = length + words.length;
                    }
                    if (line.startsWith("<DOCNO>")) {
                        id = line.substring("<DOCNO>".length(), line.indexOf("</DOCNO>")).trim();
                    }
                    if (line.startsWith("<TEXT>")) {
                        isTextStarted = true;
                    }

                    if (line.startsWith("</DOC>")) {
                        docLength.put(id, length);
                        length = 0;

                    }
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return docLength;
    }
}
