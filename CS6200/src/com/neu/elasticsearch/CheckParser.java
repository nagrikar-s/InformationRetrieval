package com.neu.elasticsearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CheckParser {
    public void parser(File file) throws FileNotFoundException, IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("termFreq=")) {
                    String[] terms = line.split("\\s+");
                    for (int i = 0; i < terms.length; i++) {
                        if (terms[i].contains("termFreq=")) {
                            int tf = Integer.valueOf(terms[i].substring("termFreq=".length() + 1, terms[i].length() - 3).trim());
                            System.out.println("tf = " + tf);
                            break;
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file = new File("C:/Users/snehanay/Google Drive/CS6200/HW1/parseFile.txt");
        CheckParser newsample = new CheckParser();
        newsample.parser(file);
    }
}