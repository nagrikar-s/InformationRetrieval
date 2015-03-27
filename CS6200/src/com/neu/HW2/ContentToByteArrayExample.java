package com.neu.HW2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import com.google.common.base.Charsets;

public class ContentToByteArrayExample {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\snehanay\\Google Drive\\CS6200\\HW2\\index.txt");
        // String term = "term";
        ContentToByteArrayExample ex = new ContentToByteArrayExample();
        ex.readContentIntoByteArray(file);
    }

    public void readContentIntoByteArray(File indexFile) throws IOException {
        TokenizerTest tt = new TokenizerTest("C:\\Users\\snehanay\\Google Drive\\CS6200\\HW2\\index.txt");
        HashSet<String> indexedTerms = tt.getIndexedTerms();
        FileInputStream indexInputStream = null;
        String catalogFile = "C:\\Users\\snehanay\\Google Drive\\CS6200\\HW2\\catalog.txt";
        int startByte;
        int end;
        File cat = new File(catalogFile);
        if (!cat.exists() || cat.length() == 0) {
            startByte = 0;
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(catalogFile))) {
                String line = null;
                String lastOffset = null;
                String lastStartByte = null;
                while ((line = reader.readLine()) != null) {
                    String[] strings = line.split(" ");
                    lastOffset = strings[(strings.length) - 1];
                    lastStartByte = strings[(strings.length) - 2];
                }
                startByte = Integer.valueOf(lastOffset) + Integer.valueOf(lastStartByte) + 1;
            }
        }
        end = startByte;
        byte[] bFile = new byte[(int) indexFile.length()];
        try (FileOutputStream os = new FileOutputStream(new File(catalogFile), true)) {
            // convert file into array of bytes
            indexInputStream = new FileInputStream(indexFile);
            indexInputStream.read(bFile);
            indexInputStream.close();
            String term = "";
            int j;
            for (int i = 0; i < bFile.length; i++) {
                end = end + bFile[i];
                if((char) bFile[i] == '\n' && !indexedTerms.contains(term)){
                    int bytesToRead = end - startByte + 1;
                    String line = term + " " + startByte + " " + bytesToRead + "\n";
                    os.write(line.getBytes(Charsets.UTF_8));
                    startByte = end + 1;
                }
                if (i == 0 || (char) bFile[i - 1] == '\n') {
                    j = i;
                    term = "";
                    while ((char) bFile[j] != ' ') {
                        term = term + (char) bFile[j];
                        j++;
                    }           
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}